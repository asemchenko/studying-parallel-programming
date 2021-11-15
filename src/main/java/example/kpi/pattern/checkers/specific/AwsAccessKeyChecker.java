package example.kpi.pattern.checkers.specific;

import example.kpi.model.result.Issue;
import example.kpi.pattern.checkers.IssueChecker;
import example.kpi.pattern.checkers.generic.RegexChecker;
import org.jetbrains.annotations.Nullable;

/**
 * <a href="https://gist.github.com/hsuh/88360eeadb0e8f7136c37fd46a62ee10">more here</a>
 */
public class AwsAccessKeyChecker implements IssueChecker {
    private static final Issue ISSUE = Issue.builder()
            .issueType("access_key_expose")
            .issueDescription("Potential amazon access credential expose")
            .build();

    private static final IssueChecker REGEX_CHECKER_ACCESS_KEY_ID = new RegexChecker(
            ISSUE,
            ".*(?<![A-Z0-9])[A-Z0-9]{20}(?![A-Z0-9]).*"
    );

    private static final IssueChecker REGEX_CHECKER_ACCESS_KEY = new RegexChecker(
            ISSUE,
            ".*(?<![A-Za-z0-9/+=])[A-Za-z0-9/+=]{40}(?![A-Za-z0-9/+=]).*"
    );


    @Override
    public @Nullable Issue check(String line) {
        final var accessKeyIdCheck = REGEX_CHECKER_ACCESS_KEY_ID.check(line);
        if (accessKeyIdCheck != null) {
            return accessKeyIdCheck;
        }

        return REGEX_CHECKER_ACCESS_KEY.check(line);

    }
}
