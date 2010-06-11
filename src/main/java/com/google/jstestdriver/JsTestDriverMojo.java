package com.google.jstestdriver;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.util.ArrayList;
import java.util.List;

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
     * @parameter expression="${plugin.artifacts}"
     */
    private List<Artifact> dependencies;

    /**
     * @parameter expression="${skipTests}" default-value=false
     */
    private boolean skipTests;

    /**
     * @parameter expression="${jsTestDriver.artifactId}" default-value="jstestdriver"
     */
    private String artifactId;

    /**
     * @parameter expression="${jsTestDriver.groupdId}" default-value="com.google.jstestdriver"
     */
    private String groupId;

    /**
     * @parameter expression="${jsTestDriver.jar}" default-value=""
     */
    private String jar;



    /**
     * JsTD Options:
     *   These should be kept in step with the JsTD command line options.
     */

    /**
     * @parameter expression="${jsTestDriver.basePath}" default-value=""
     */
    private String basePath;

    /**
     * @parameter expression="${jsTestDriver.runnerMode}" default-value=""
     */
    private String runnerMode;


    /**
     * @parameter expression="${jsTestDriver.browser}" default-value=""
     */
    private String browser;

    /**
     * @parameter expression="${jsTestDriver.browserTimeout}" default-value=""
     */
    private String browserTimeout;

    /**
     * @parameter expression="${jsTestDriver.captureConsole}" default-value=true
     */
    private boolean captureConsole;

    /**
     * @parameter expression="${jsTestDriver.config}" default-value="src/test/resources/jsTestDriver.conf"
     */
    private String config;

    /**
     * @parameter expression="${jsTestDriver.dryRunFor}" default-value=""
     */
    private String dryRunFor;

    /**
     * @parameter expression="${jsTestDriver.port}" default-value=""
     */
    private String port;

    /**
     * @parameter expression="${jsTestDriver.preloadFiles}" default-value=false
     */
    private boolean preloadFiles;

    /**
     * @parameter expression="${jsTestDriver.reset}" default-value=false
     */
    private boolean reset;

    /**
     * @parameter expression="${jsTestDriver.server}" default-value=""
     */
    private String server;

    /**
     * @parameter expression="${jsTestDriver.testOutput}" default-value=""
     */
    private String testOutput;

    /**
     * @parameter expression="${jsTestDriver.tests}" default-value="all"
     */
    private String tests;

    /**
     * @parameter expression="${jsTestDriver.verbose}" default-value=false
     */
    private boolean verbose;



    // internals
    private ProcessExecutor processExecutor;
    private ResultsProcessor resultsProcessor;

    public JsTestDriverMojo() {
        this(new StreamingProcessExecutor(), new ResultsProcessor());
    }

    public JsTestDriverMojo(ProcessExecutor processExecutor, ResultsProcessor resultsProcessor) {
        this.processExecutor = processExecutor;
        this.resultsProcessor = resultsProcessor;
    }

    public void execute() throws MojoExecutionException
    {
        MojoLogger.bindLog(getLog());

        if (skipTests)
        {
            getLog().info("Tests are skipped.");
            return;
        }

        printBanner();

        ProcessConfiguration config = buildProcessConfiguration();
        logProcessArguments(config);

        resultsProcessor.processResults(processExecutor.execute(config));
    }

    private ProcessConfiguration buildProcessConfiguration()
            throws MojoExecutionException
    {
        ProcessConfiguration configuration;
        if (StringUtils.isNotEmpty(jar))
        {
            configuration = buildLocalJarProcessConfig();
        }
        else
        {
            configuration = buildMavenJarProcessConfig();
        }

        buildArguments((JarProcessConfiguration) configuration);

        return configuration;
    }

    private ProcessConfiguration buildMavenJarProcessConfig() throws MojoExecutionException
    {
        Artifact artifact = new ArtifactLocator(mavenProject).findArtifact(groupId, artifactId);
        ProcessConfiguration jarConfig = new JarProcessConfiguration(artifact.getFile().getAbsolutePath());
        addClasspathArguments((JarProcessConfiguration) jarConfig);
        return jarConfig;
    }

    private ProcessConfiguration buildLocalJarProcessConfig() throws MojoExecutionException
    {
        return new JarProcessConfiguration(jar);
    }

    private void addClasspathArguments(JarProcessConfiguration jarConfig)
    {
        List<String> classpathArgs = new ArrayList<String>();
        for (Artifact artifact : dependencies)
        {
            classpathArgs.add(artifact.getFile().getAbsolutePath());
        }

        jarConfig.addClasspath(StringUtils.join(classpathArgs, ";"));
    }

    private void buildArguments(JarProcessConfiguration testRunner)
            throws MojoExecutionException
    {
        if (StringUtils.isNotEmpty(basePath)) {
            if (config.startsWith(basePath)) {
                config = StringUtils.stripStart(config, basePath);
                if (config.startsWith("/")) {
                    config = StringUtils.stripStart(config, "/");
                }
            }
        }

        if (StringUtils.isNotEmpty(basePath))
        {
            testRunner.addArgument("--basePath", basePath);
        }
        if (StringUtils.isNotEmpty(browser))
        {
            testRunner.addArgument("--browser", browser);
        }
        if (StringUtils.isNotEmpty(browserTimeout))
        {
            testRunner.addArgument("--browserTimeout", browserTimeout);
        }
        if (captureConsole)
        {
            testRunner.addArgument("--captureConsole");
        }
        testRunner.addArgument("--config", config);
        if (StringUtils.isNotEmpty(dryRunFor))
        {
            testRunner.addArgument("--dryRunFor", dryRunFor);
        }
        if (StringUtils.isNotEmpty(port))
        {
            testRunner.addArgument("--port", port);
        }
        if (preloadFiles)
        {
            testRunner.addArgument("--preloadFiles");
        }
        if (reset)
        {
            testRunner.addArgument("--reset");
        }
        if (StringUtils.isNotEmpty(runnerMode))
        {
            testRunner.addArgument("--runnerMode", runnerMode);
        }
        if (StringUtils.isNotEmpty(server))
        {
            testRunner.addArgument("--server", server);
        }
        if (StringUtils.isNotEmpty(testOutput))
        {
            if (testOutput != null && !testOutput.equals("."))
            {
                FileUtils.makeDirectoryIfNotExists(testOutput);
            }
            testRunner.addArgument("--testOutput", testOutput);
        }
        testRunner.addArgument("--tests", tests);
        if (verbose)
        {
            testRunner.addArgument("--verbose");
        }
    }

    private void logProcessArguments(ProcessConfiguration processedArgs)
    {
        if (verbose) {
            System.out.println(String.format("Running: %s %s",
                                             processedArgs.getExecutable(),
                                             StringUtils.join(processedArgs.getArguments(), " ")));
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
