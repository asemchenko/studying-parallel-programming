package com.kpi.worker.pattern.checkers.line.generic;


import com.kpi.worker.model.result.Issue;
import com.kpi.worker.pattern.checkers.line.LineChecker;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexLineChecker implements LineChecker {
    private final Issue issue;
    private final Pattern pattern;

    private Matcher matcher;

    public RegexLineChecker(Issue issue, String regex) {
        this.issue = issue;
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public @Nullable
    Issue check(String line) {
        return pattern.matcher(line).matches() ? this.issue : null;
    }
}
