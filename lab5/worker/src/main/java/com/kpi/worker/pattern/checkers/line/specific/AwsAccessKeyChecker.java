package com.kpi.worker.pattern.checkers.line.specific;

import com.kpi.worker.model.result.Issue;
import com.kpi.worker.pattern.checkers.line.LineChecker;
import com.kpi.worker.pattern.checkers.line.generic.RegexLineChecker;
import org.jetbrains.annotations.Nullable;

/**
 * <a href="https://gist.github.com/hsuh/88360eeadb0e8f7136c37fd46a62ee10">more here</a>
 */
public class AwsAccessKeyChecker implements LineChecker {
    private static final Issue ISSUE = Issue.builder()
            .issueType("access_key_expose")
            .issueDescription("Potential amazon access credential expose")
            .build();

    private static final LineChecker REGEX_CHECKER_ACCESS_KEY_ID = new RegexLineChecker(
            ISSUE,
            ".*(?<![A-Z0-9])[A-Z0-9]{20}(?![A-Z0-9]).*"
    );

    private static final LineChecker REGEX_CHECKER_ACCESS_KEY = new RegexLineChecker(
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
