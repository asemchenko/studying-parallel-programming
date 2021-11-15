package example.kpi.pattern.checkers.generic;

import example.kpi.model.result.Issue;
import example.kpi.pattern.checkers.IssueChecker;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexChecker implements IssueChecker {
    private final Issue issue;
    private final Pattern pattern;

    private Matcher matcher;

    public RegexChecker(Issue issue, String regex) {
        this.issue = issue;
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public @Nullable Issue check(String line) {
        return pattern.matcher(line).matches() ? this.issue : null;
    }
}
