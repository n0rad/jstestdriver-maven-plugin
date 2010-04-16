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
 * Copyright 2009-2010, Burke Webster (burke.webster@gmail.com)
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

    

    private boolean streamResults;
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

        String output = ProcessUtils.run(jarConfig, streamResults);

        new ResultsProcessor().processResults(output);
    }

    private JarProcessConfiguration buildProcessConfiguration()
            throws MojoExecutionException
    {
        Artifact artifact = new ArtifactLocator(mavenProject).findArtifact(GROUP_ID, ARTIFACT_ID);
        JarProcessConfiguration jarConfig = new JarProcessConfiguration(artifact.getFile().getAbsolutePath());
        addArguments(jarConfig);
        return jarConfig;
    }

    private void addArguments(JarProcessConfiguration testRunner)
            throws MojoExecutionException
    {
        if (StringUtils.isNotEmpty(port))
        {
            testRunner.addArgument("--port", port);
            streamResults = true;
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
                    FileUtils.makeDirectoryIfNotExists(testOutput);
                }
                testRunner.addArgument("--testOutput", testOutput);
            }
        }

        if (verbose)
        {
            testRunner.addArgument("--verbose");
        }
    }

    private void logProcessArguments(JarProcessConfiguration processedArgs)
    {
        if (debug)
        {
            System.out.println("Running: " + StringUtils.join(processedArgs.getArguments(), " "));
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