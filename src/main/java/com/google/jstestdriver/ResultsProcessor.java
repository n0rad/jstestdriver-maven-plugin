package com.google.jstestdriver;

import org.apache.maven.plugin.MojoExecutionException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright 2009-2010, Burke Webster (burke.webster@gmail.com)
 */
public class ResultsProcessor
{
    private static final String RESULTS_START_REGEXP = "Total ([0-9]+) tests \\(Passed: ([0-9]+); Fails: ([0-9]+); Errors: ([0-9]+)\\).*";
    private static final String NO_BROWSERS_CAPTURED_REGEXP = "No browsers were captured.*";

    public void processResults(String output) throws MojoExecutionException
    {
        TestRunnerResults testRunnerResults = extractResults(output);

        if (testRunnerResults == null || testRunnerResults.hasFailures())
        {
            StringBuffer errorString = new StringBuffer();
            errorString.append("Test failure:");
            if (testRunnerResults == null)
            {
                errorString.append(" unable to parse results");
            }
            else
            {
                int errors = testRunnerResults.getTotalTestsErrored();
                int failures = testRunnerResults.getTotalTestsFailed();
                if (errors > 0)
                {
                    errorString.append(" ").append(errors).append(" errors.");
                }
                if (failures > 0)
                {
                    errorString.append(" ").append(failures).append(" failures.");
                }
            }

            throw new MojoExecutionException(errorString.toString());
        }
    }

    private TestRunnerResults extractResults(String output) throws MojoExecutionException
    {
        Pattern pattern = Pattern.compile(RESULTS_START_REGEXP);

        for (String line : output.split("\\n"))
        {
            if (line.matches(NO_BROWSERS_CAPTURED_REGEXP))
            {
                throw new MojoExecutionException("Unable to capture any browsers");
            }

            Matcher matcher = pattern.matcher(line);
            if (matcher.matches())
            {
                return TestRunnerResults.buildResultsFromMatcher(matcher);
            }
        }
        throw new MojoExecutionException("Failed to parse results");
    }
}
