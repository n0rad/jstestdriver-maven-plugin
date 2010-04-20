package com.google.jstestdriver;

import org.apache.maven.plugin.MojoExecutionException;
import static org.testng.Assert.fail;
import org.testng.annotations.Test;

/**
 * Copyright 2009-2010, Burke Webster (burke.webster@gmail.com)
 */
@Test
public class ResultsProcessorTest
{
    public void testNoErrorsShouldNotThrowException()
    {
        String output = buildOutputString(2, 0, 0);
        try
        {
            new ResultsProcessor().processResults(output);
        }
        catch (MojoExecutionException e)
        {
            fail("Should not have thrown an exception : " + e.getMessage());
        }
    }

    public void testAtLeastOneErrorShouldThrowException()
    {
        String output = buildOutputString(1, 0, 1);
        try
        {
            new ResultsProcessor().processResults(output);
            fail("Should have thrown an exception");
        }
        catch (MojoExecutionException ignored)
        {
        }
    }

    public void testAtLeastOneFailureShouldThrowException()
    {
        String output = buildOutputString(1, 1, 0);
        try
        {
            new ResultsProcessor().processResults(output);
            fail("Should have thrown an exception");
        }
        catch (MojoExecutionException ignored)
        {
        }
    }

    public void testShouldThrowErrorWhenNoBrowsersCaptured()
    {
        String output = "No browsers were captured";
        try
        {
            new ResultsProcessor().processResults(output);
            fail("Should have thrown an exception");
        }
        catch (MojoExecutionException ignored)
        {
        }
    }

    private String buildOutputString(int passed, int failed, int error)
    {
        return String.format("......\\\n......\\\nTotal %s tests (Passed: %s; Fails: %s; Errors: %s) (100.00 ms)\n  Firefox",
                passed + failed + error, passed, failed, error);
    }
}
