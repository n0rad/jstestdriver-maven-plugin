package com.google.jstestdriver;

import java.util.regex.Matcher;

/**
 * Copyright 2009-2010, Burke Webster (burke.webster@gmail.com)
 */
public class TestRunnerResults
{
    private int totalTestsRun;
    private int totalTestsPassed;
    private int totalTestsFailed;
    private int totalTestsErrored;

    public TestRunnerResults(int totalTestsRun, int totalTestsPassed, int totalTestsFailed, int totalTestsErrored)
    {
        this.totalTestsRun = totalTestsRun;
        this.totalTestsPassed = totalTestsPassed;
        this.totalTestsFailed = totalTestsFailed;
        this.totalTestsErrored = totalTestsErrored;
    }

    public static TestRunnerResults buildResultsFromMatcher(Matcher matcher)
    {
        return new TestRunnerResults(
                Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2)),
                Integer.parseInt(matcher.group(3)),
                Integer.parseInt(matcher.group(4)));
    }

    public boolean hasFailures()
    {
        return totalTestsFailed > 0 || totalTestsErrored > 0;
    }

    public int getTotalTestsRun()
    {
        return totalTestsRun;
    }

    public int getTotalTestsPassed()
    {
        return totalTestsPassed;
    }

    public int getTotalTestsFailed()
    {
        return totalTestsFailed;
    }

    public int getTotalTestsErrored()
    {
        return totalTestsErrored;
    }
}