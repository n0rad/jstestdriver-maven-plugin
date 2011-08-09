package com.googlecode.jstdmavenplugin;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2009-2011, Burke Webster (burke.webster@gmail.com)
 *
 * @requiresDependencyResolution test
 * @goal test
 * @phase test
 */
public class JsTestDriverMojo extends AbstractMojo {
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
     * @parameter expression="${jstd.artifactId}" default-value="jstestdriver"
     */
    private String artifactId;

    /**
     * @parameter expression="${jstd.groupdId}" default-value="com.google.jstestdriver"
     */
    private String groupId;

    /**
     * @parameter expression="${jstd.jar}" default-value=""
     */
    private String jar;

    /**
     * @parameter expression="${jstd.jvmOpts}" default-value=""
     */
    private String jvmOpts;

    /**
     * Should we default in the basePath if none is specified? Defaults to true.
     *
     * @parameter expression="${jstd.defaultBasePath}" default-value="true"
     */
    private boolean defaultBasePath;

    

    /**
     * JsTD Options:
     *   These should be kept in step with the JsTD command line options.
     */

    /**
     * @parameter expression="${jstd.basePath}" default-value=""
     */
    private String basePath;

    /**
     * @parameter expression="${jstd.browser}" default-value=""
     */
    private String browser;

    /**
     * @parameter expression="${jstd.browserTimeout}" default-value=""
     */
    private String browserTimeout;

    /**
     * @parameter expression="${jstd.captureConsole}" default-value=true
     */
    private boolean captureConsole;

    /**
     * @parameter expression="${jstd.config}" default-value="src/test/resources/jsTestDriver.conf"
     */
    private String config;

    /**
     * @parameter expression="${jstd.dryRunFor}" default-value=""
     */
    private String dryRunFor;

    /**
     * @parameter expression="${jstd.plugins}" default-value=""
     */
    private String plugins;

    /**
     * @parameter expression="${jstd.port}" default-value=""
     */
    private String port;

    /**
     * @parameter expression="${jstd.preloadFiles}" default-value=false
     */
    private boolean preloadFiles;

    /**
     * @parameter expression="${jstd.requiredBrowsers}" default-value=""
     */
    private String requiredBrowsers;

    /**
     * @parameter expression="${jstd.reset}" default-value=false
     */
    private boolean reset;

    /**
     * @parameter expression="${jstd.runnerMode}" default-value=""
     */
    private String runnerMode;

    /**
     * @parameter expression="${jstd.server}" default-value=""
     */
    private String server;

    /**
     * @parameter expression="${jstd.serverHandlerPrefix}" default-value=""
     */
    private String serverHandlerPrefix;

    /**
     * @parameter expression="${jstd.testOutput}" default-value=""
     */
    private String testOutput;

    /**
     * @parameter expression="${jstd.tests}" default-value="all"
     */
    private String tests;

    /**
     * @parameter expression="${jstd.verbose}" default-value=false
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

    public void execute() throws MojoExecutionException {
        MojoLogger.bindLog(getLog());

        if (skipTests) {
            getLog().info("Tests are skipped.");
            return;
        }

        printBanner();

        ProcessConfiguration config = buildProcessConfiguration();
        logProcessArguments(config);

        resultsProcessor.processResults(processExecutor.execute(config));
    }

    private ProcessConfiguration buildProcessConfiguration()
            throws MojoExecutionException {
        ProcessConfiguration configuration;
        if (StringUtils.isNotEmpty(jar)) {
            configuration = buildLocalJarProcessConfig();
        } else {
            configuration = buildMavenJarProcessConfig();
        }

        buildArguments((JarProcessConfiguration) configuration);

        return configuration;
    }

    private ProcessConfiguration buildMavenJarProcessConfig() throws MojoExecutionException {
        Artifact artifact = new ArtifactLocator(mavenProject).findArtifact(groupId, artifactId);
        JarProcessConfiguration jarConfig = new JarProcessConfiguration(artifact.getFile().getAbsolutePath());
        addClasspathArguments(jarConfig);
        if (StringUtils.isNotEmpty(jvmOpts)) {
            jarConfig.addExecutableOptions(jvmOpts);
        }
        return jarConfig;
    }

    private ProcessConfiguration buildLocalJarProcessConfig() throws MojoExecutionException {
        JarProcessConfiguration processConfiguration = new JarProcessConfiguration(jar);
        if (StringUtils.isNotEmpty(jvmOpts)) {
            processConfiguration.addExecutableOptions(jvmOpts);
        }
        return processConfiguration;
    }

    private void addClasspathArguments(JarProcessConfiguration jarConfig) {
        List<String> classpathArgs = new ArrayList<String>();
        for (Artifact artifact : dependencies) {
            classpathArgs.add(artifact.getFile().getAbsolutePath());
        }

        jarConfig.addClasspath(StringUtils.join(classpathArgs, ";"));
    }

    private void buildArguments(JarProcessConfiguration testRunner)
            throws MojoExecutionException {
        String defaultedBasePath = StringUtils.defaultIfEmpty(basePath, mavenProject.getBasedir().getAbsolutePath()); 
        if (config != null) {
            File configFile = new File(config);
            if (!configFile.isAbsolute()) {
                configFile = new File(defaultedBasePath, config);
                config = configFile.getPath();
            }
        }

        if (defaultBasePath) {
            basePath = defaultedBasePath;
        }
        if (StringUtils.isNotEmpty(basePath)) {
            testRunner.addArgument("--basePath", basePath);
        }
        if (StringUtils.isNotEmpty(browser)) {
            testRunner.addArgument("--browser", browser);
        }
        if (StringUtils.isNotEmpty(browserTimeout)) {
            testRunner.addArgument("--browserTimeout", browserTimeout);
        }
        if (captureConsole) {
            testRunner.addArgument("--captureConsole");
        }
        testRunner.addArgument("--config", config);
        if (StringUtils.isNotEmpty(dryRunFor)) {
            testRunner.addArgument("--dryRunFor", dryRunFor);
        }
        if (StringUtils.isNotEmpty(plugins)) {
            testRunner.addArgument("--plugins", plugins);
        }
        if (StringUtils.isNotEmpty(port)) {
            testRunner.addArgument("--port", port);
        }
        if (preloadFiles) {
            testRunner.addArgument("--preloadFiles");
        }
        if (StringUtils.isNotEmpty(requiredBrowsers)) {
            testRunner.addArgument("--requiredBrowsers", requiredBrowsers);
        }
        if (reset) {
            testRunner.addArgument("--reset");
        }
        if (StringUtils.isNotEmpty(runnerMode)) {
            testRunner.addArgument("--runnerMode", runnerMode);
        }
        if (StringUtils.isNotEmpty(server)) {
            testRunner.addArgument("--server", server);
        }
        if (StringUtils.isNotEmpty(serverHandlerPrefix)) {
            testRunner.addArgument("--serverHandlerPrefix", serverHandlerPrefix);
        }
        if (StringUtils.isNotEmpty(testOutput)) {
            if (testOutput != null && !testOutput.equals(".")) {
                FileUtils.makeDirectoryIfNotExists(testOutput);
            }
            testRunner.addArgument("--testOutput", testOutput);
        }
        testRunner.addArgument("--tests", tests);
        if (verbose) {
            testRunner.addArgument("--verbose");
        }
    }

    private void logProcessArguments(ProcessConfiguration processConfiguration) {
        if (verbose) {
            System.out.println(String.format("Running: %s", StringUtils.join(processConfiguration.getFullCommand(), " ")));
        }
    }

    private void printBanner() {
        System.out.println("\n" +
                "-------------------------------------------\n" +
                " J S  T E S T  D R I V E R                 \n" +
                "-------------------------------------------\n");
    }
}
