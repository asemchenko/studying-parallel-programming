package example.kpi;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.kpi.model.result.RepoIssue;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.dfs.DfsRepositoryDescription;
import org.eclipse.jgit.internal.storage.dfs.InMemoryRepository;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;


public class App {

    public static class TokenizerMapper
            extends Mapper<Object, Text, Text, Text>{
        final PathAnalyzer pathAnalyzer = new PathAnalyzer();
        final ContentAnalyzer contentAnalyzer = new ContentAnalyzer();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {
                String repoUrl = itr.nextToken();
                processRepo(context, repoUrl);
            }
        }

        private void processRepo(Context context, String repoUrl) throws IOException, InterruptedException {
            DfsRepositoryDescription repoDesc = new DfsRepositoryDescription();
            InMemoryRepository repo = new InMemoryRepository(repoDesc);
            Git git = new Git(repo);
            try {
                git.fetch()
                        .setRemote(repoUrl)
                        .setRefSpecs(new RefSpec("+refs/heads/*:refs/heads/*"))
                        .call();
            } catch (GitAPIException e) {
                e.printStackTrace();
                return;
            }
            repo.getObjectDatabase();
            ObjectId lastCommitId = repo.resolve("refs/heads/master");
            RevWalk revWalk = new RevWalk(repo);
            RevCommit commit = revWalk.parseCommit(lastCommitId);
            RevTree tree = commit.getTree();
            TreeWalk treeWalk = new TreeWalk(repo);
            treeWalk.addTree(tree);
            treeWalk.setRecursive(true);
            while (treeWalk.next()) {
                ObjectId objectId = treeWalk.getObjectId(0);
                ObjectLoader loader = git.getRepository().open(objectId);
                String path = treeWalk.getPathString();
                String content = new String(loader.getBytes());
                processFile(context, repoUrl, path, content);
            }
        }

        private void processFile(Context context, String repoUrl, String path, String content) throws IOException, InterruptedException {
            List<RepoIssue> contentIssues = contentAnalyzer.processFile(path, content);
            contentIssues.addAll(pathAnalyzer.processFile(path));
            String issuesJson = new ObjectMapper().writeValueAsString(contentIssues);
            context.write(
                    new Text(repoUrl),
                    new Text(issuesJson)
            );
        }
    }

    public static class IntSumReducer
            extends Reducer<Text, Text, Text, Text> {

        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            ObjectMapper mapper = new ObjectMapper();
            List<RepoIssue> issues = new ArrayList<>();
            for (Text value : values) {
                String issuesJson = value.toString();
                issues.addAll(
                        Arrays.asList(mapper.readValue(issuesJson, RepoIssue[].class))
                );
            }
            String reportJson = new ObjectMapper().writeValueAsString(issues);
            context.write(key, new Text(reportJson));
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Lab3");
        job.setJarByClass(App.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path(args[1]));
        FileOutputFormat.setOutputPath(job, new Path(args[2]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
