package example.kpi.pattern.checkers;

import example.kpi.model.result.Issue;
import org.jetbrains.annotations.Nullable;

public interface IssueChecker {
    /**
     * Performs check on provided file line and returns RepoIssue if there is some problem in file.
     * Otherwise returns null.
     * @return issue if there is any. Otherwise - null.
     */
    @Nullable Issue check(String line);
}
