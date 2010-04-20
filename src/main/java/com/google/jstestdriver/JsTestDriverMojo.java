package com.google.jstestdriver;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

/**
 * Copyright 2009-2010, Burke Webster (burke.webster@gmail.com)
 *
 * @requiresDependencyResolution test
 * @goal test
 */
public class JsTestDriverMojo extends AbstractMojo
{

    /**
     * Mojo Options
     */

    /**
     * @parameter default-value="${project}"
     */
    private MavenProject mavenProject;

    /**
     * @parameter expression="${skipTests}" default-value=false
     */
    private boolean skipTests;


    /**
     * JsTD Options:
     *   These should be kept in step with the JsTD command line options.
     */

    /**
     * @parameter expression="${jsTestDriver.config}" default-value="jsTestDriver.conf"
     */
    private String config;

    /**
     * @parameter expression="${jsTestDriver.captureConsole}" default-value=true
     */
    private boolean captureConsole;

    /**
     * @parameter expression="${jsTestDriver.reset}" default-value=false
     */
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


    public void execute() throws MojoExecutionException
    {
        if (skipTests)
        {
            getLog().info("Tests are skipped.");
            return;
        }

        printBanner();

        JarProcessConfiguration jarConfig = buildProcessConfiguration();
        logProcessArguments(jarConfig);

        String output = new StreamingProcessExecutor().execute(jarConfig);

        new ResultsProcessor().processResults(output);
    }

    private JarProcessConfiguration buildProcessConfiguration()
            throws MojoExecutionException
    {
        Artifact artifact = new ArtifactLocator(mavenProject).findArtifact(GROUP_ID, ARTIFACT_ID);
        JarProcessConfiguration jarConfig = new JarProcessConfiguration(artifact.getFile().getAbsolutePath());
        buildArguments(jarConfig);
        return jarConfig;
    }

    private void buildArguments(JarProcessConfiguration testRunner)
            throws MojoExecutionException
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
        if (StringUtils.isNotEmpty(port))
        {
            testRunner.addArgument("--port", port);
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
                FileUtils.makeDirectoryIfNotExists(testOutput);
            }
            testRunner.addArgument("--testOutput", testOutput);
        }
        if (verbose)
        {
            testRunner.addArgument("--verbose");
        }
    }

    private void logProcessArguments(ProcessConfiguration processedArgs)
    {
        System.out.println(String.format("Running: %s %s",
                processedArgs.getExecutable(),
                StringUtils.join(processedArgs.getArguments(), " ")));
    }

    private void printBanner()
    {
        System.out.println("\n" +
                "-------------------------------------------\n" +
                " J S  T E S T  D R I V E R                 \n" +
                "-------------------------------------------\n");
    }
}