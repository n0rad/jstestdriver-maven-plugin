package com.google.jstestdriver;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright © 2010, Burke Webster (burke.webster@gmail.com)
 * 
 * @requiresDependencyResolution test
 * @goal test
 **/
public class JsTestDriverMojo extends AbstractMojo
{

    // Mojo Options

    /** @parameter default-value="${project}" */
    private MavenProject mavenProject;

    /**
     * @parameter expression="${jsTestDriver.debug}" default-value=false
     */
    private boolean debug;

    /**
     * @parameter expression="${skipTests}" default-value=false
     */
    private boolean skipTests;



    // JsTD Options

    /**
     * @parameter expression="${jsTestDriver.config}" default-value="jsTestDriver.conf"
     **/
    private String config;

    /**
     * @parameter expression="${jsTestDriver.captureConsole}" default-value=true
     **/
    private boolean captureConsole;

    /**
     * @parameter expression="${jsTestDriver.reset}" default-value=false
     **/
    private boolean reset;

    /**
     * @parameter expression="${jsTestDriver.tests}" default-value="all"
     */
    private String tests;

    /**
     * @parameter expression="${jsTestDriver.verbose}" default-value=false
     */
    private boolean verbose;


    /**
     * @parameter expression="${jsTestDriver.testOutput}" default-value=""
     */
    private String testOutput;

    /**
     * @parameter expression="${jsTestDriver.server}" default-value=""
     */
    private String server;

    /**
     * @parameter expression="${jsTestDriver.port}" default-value=""
     */
    private String port;

    /**
     * @parameter expression="${jsTestDriver.browser}" default-value=""
     */
    private String browser;


    private static final String GROUP_ID = "com.google.jstestdriver";
    private static final String ARTIFACT_ID = "jstestdriver";
    private static final String RESULTS_START_REGEXP = "Total ([0-9]+) tests \\(Passed: ([0-9]+); Fails: ([0-9]+); Errors: ([0-9]+)\\).*";
    private static final String NO_BROWSERS_CAPTURED_REGEXP = "No browsers were captured.*";

    public void execute() throws MojoExecutionException
    {
        if (skipTests)
        {
            getLog().info("Tests are skipped.");
            return;
        }

        printBanner();

        JarApplication testRunner = buildRunner();
        addArguments(testRunner);
        logProcessArguments(testRunner);

        Pattern pattern = Pattern.compile(RESULTS_START_REGEXP);
        StringBuffer buffer = new StringBuffer();
        boolean foundStartOfResults = false;
        TestRunnerResults testRunnerResults = null;
        try
        {
            Process process = ProcessFactory.create(testRunner);
            BufferedReader processInputStreamReader = ProcessUtils.getInputStream(process);
            for (String line = processInputStreamReader.readLine(); line != null; line = processInputStreamReader.readLine())
            {
                if (debug)
                {
                    System.out.println(line);
                }

                if (line.matches(NO_BROWSERS_CAPTURED_REGEXP))
                {
                    throw new MojoExecutionException("Unable to capture any browsers");
                }

                Matcher matcher = pattern.matcher(line);
                if (matcher.matches())
                {
                    foundStartOfResults = true;
                    testRunnerResults = TestRunnerResults.buildResultsFromMatcher(matcher);
                }

                if (foundStartOfResults)
                {
                    buffer.append(line).append("\n");
                }
            }

            process.waitFor();

            printResults(buffer);
            processResults(testRunnerResults);
        }
        catch (IOException e)
        {
            throw new MojoExecutionException(e.getMessage());
        }
        catch (InterruptedException e)
        {
            throw new MojoExecutionException(e.getMessage());
        }
    }

    private void addArguments(JarApplication testRunner)
            throws MojoExecutionException
    {
        if (StringUtils.isNotEmpty(port))
        {
            testRunner.addArgument("--port", port);
        }
        else
        {
            testRunner.addArgument("--config", config);
            testRunner.addArgument("--tests", tests);

            if (captureConsole)
            {
                testRunner.addArgument("--captureConsole");
            }
            if (reset)
            {
                testRunner.addArgument("--reset");
            }
            if (StringUtils.isNotEmpty(server))
            {
                testRunner.addArgument("--server", server);
            }
            if (StringUtils.isNotEmpty(browser))
            {
                testRunner.addArgument("--browser", browser);
            }
            if (StringUtils.isNotEmpty(testOutput))
            {
                if (testOutput != null && !testOutput.equals("."))
                {
                    makeDirectoryIfNotExists();
                }
                testRunner.addArgument("--testOutput", testOutput);
            }
        }

        if (verbose)
        {
            testRunner.addArgument("--verbose");
        }
    }

    private void makeDirectoryIfNotExists()
            throws MojoExecutionException
    {
        // we need to ensure this directory exists
        File directory = new File(testOutput);
        if (!directory.exists())
        {
            if (!directory.mkdirs())
            {
                throw new MojoExecutionException("Failed to create testOutput directory");
            }
        }
    }

    private void logProcessArguments(JarApplication processedArgs)
    {
        if (debug)
        {
            System.out.println("Running: " + StringUtils.join(processedArgs.getArguments(), " "));
        }
    }

    private JarApplication buildRunner() throws MojoExecutionException
    {
        for (Object object : mavenProject.getArtifacts())
        {
            Artifact artifact = (Artifact) object;
            if (artifact.getGroupId().equals(GROUP_ID) && artifact.getArtifactId().equals(ARTIFACT_ID))
            {
                return new JarApplication(artifact.getFile().getAbsolutePath());
            }
        }

        throw new MojoExecutionException(String.format("Failed to find %s:%s", GROUP_ID, ARTIFACT_ID));
    }

    private void printResults(StringBuffer buffer)
    {
        if (!debug)
        {
            System.out.println(buffer.toString());
        }
    }

    private void processResults(TestRunnerResults testRunnerResults)
            throws MojoExecutionException
    {
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

    private void printBanner()
    {
        System.out.println("\n" +
                "-------------------------------------------\n" +
                " J S  T E S T  D R I V E R                 \n" +
                "-------------------------------------------\n");
    }

}