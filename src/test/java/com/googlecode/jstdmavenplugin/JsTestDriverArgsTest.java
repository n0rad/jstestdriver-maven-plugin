package com.googlecode.jstdmavenplugin;

import static com.googlecode.jstdmavenplugin.matchers.ExecutorCalledWithMatcher.wasCalledWith;
import static com.googlecode.jstdmavenplugin.matchers.ExecutorNotCalledWithMatcher.wasNotCalledWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import org.apache.maven.project.MavenProject;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Copyright 2009-2011, Burke Webster (burke.webster@gmail.com)
 * 
 * This test should verify all command line arguments that JsTestDriver provide are
 * supported in the plugin.
 */
@Test
public class JsTestDriverArgsTest extends AbstractMojoTest {

    private StreamingProcessExecutor executor;
    private JsTestDriverMojo mojo;
    private ArgumentCaptor<ProcessConfiguration> projessConfigArgCaptor;

    @BeforeMethod
    public void setUp() throws Exception {
        ResultsProcessor processor = mock(ResultsProcessor.class);
        executor = mock(StreamingProcessExecutor.class);
        mojo = new JsTestDriverMojo(executor, processor);

        MavenProject project = getMockMavenProject(mojo);
        setField(mojo, "mavenProject", project);
        projessConfigArgCaptor = ArgumentCaptor.forClass(ProcessConfiguration.class);
    }

    public void shouldPassAlongBasePathIfProvided() throws Exception {
        setField(mojo, "basePath", "src");
        mojo.execute();
        assertThat(executor, wasCalledWith(projessConfigArgCaptor, "--basePath src"));
    }

    public void shouldPassAlongBrowserIfProvided() throws Exception {
        setField(mojo, "browser", "/path/to/some/browser");
        mojo.execute();
        assertThat(executor, wasCalledWith(projessConfigArgCaptor, "--browser /path/to/some/browser"));
    }

    public void shouldPassAlongBrowserTimeoutIfProvided() throws Exception {
        setField(mojo, "browserTimeout", "10000");
        mojo.execute();
        assertThat(executor, wasCalledWith(projessConfigArgCaptor, "--browserTimeout 10000"));
    }

    public void shouldSetCaptureConsoleIfTrue() throws Exception {
        setField(mojo, "captureConsole", true);
        mojo.execute();
        assertThat(executor, wasCalledWith(projessConfigArgCaptor, "--captureConsole"));
    }

    public void shouldNotSetCaptureConsoleIfFalse() throws Exception {
        setField(mojo, "captureConsole", false);
        mojo.execute();
        assertThat(executor, wasNotCalledWith(projessConfigArgCaptor, "--captureConsole"));
    }

    public void shouldMakeConfigFileAbsolute() throws Exception {
        String path = "path/to/config.txt";
        setField(mojo, "config", path);
        mojo.execute();
        assertThat(
                executor,
                wasCalledWith(projessConfigArgCaptor,
                        String.format("--config %s/%s", basedir.getAbsolutePath(), path)));
    }

    public void shouldPassAlongAbsoluteConfig() throws Exception {
        String path = "/abs/path/to/config.txt";
        setField(mojo, "config", path);
        mojo.execute();
        assertThat(executor, wasCalledWith(projessConfigArgCaptor, String.format("--config %s", path)));
    }

    public void shouldPassAlongDryRunForIfProvided() throws Exception {
        setField(mojo, "dryRunFor", "PersonTest");
        mojo.execute();
        assertThat(executor, wasCalledWith(projessConfigArgCaptor, "--dryRunFor PersonTest"));
    }

    //    public void shouldPassAlongPluginsIfProvided() throws Exception {
    //        setField(mojo, "plugins", "plugin1,plugin2");
    //        mojo.execute();
    //        assertThat(executor, wasCalledWith(projessConfigArgCaptor, "--plugins plugin1,plugin2"));
    //    }

    public void shouldPassAlongPortIfProvided() throws Exception {
        setField(mojo, "port", "9876");
        mojo.execute();
        assertThat(executor, wasCalledWith(projessConfigArgCaptor, "--port 9876"));
    }

    public void shouldSetPreloadFilesIfTrue() throws Exception {
        setField(mojo, "preloadFiles", true);
        mojo.execute();
        assertThat(executor, wasCalledWith(projessConfigArgCaptor, "--preloadFiles"));
    }

    public void shouldNotSetPreloadFilesIfFalse() throws Exception {
        setField(mojo, "preloadFiles", false);
        mojo.execute();
        assertThat(executor, wasNotCalledWith(projessConfigArgCaptor, "--preloadFiles"));
    }

    public void shouldPassAlongRequiredBrowsersIfProvided() throws Exception {
        setField(mojo, "requiredBrowsers", "firefox,chrome");
        mojo.execute();
        assertThat(executor, wasCalledWith(projessConfigArgCaptor, "--requiredBrowsers firefox,chrome"));
    }

    public void shouldSetResetIfTrue() throws Exception {
        setField(mojo, "reset", true);
        mojo.execute();
        assertThat(executor, wasCalledWith(projessConfigArgCaptor, "--reset"));
    }

    public void shouldNotSetResetIfFalse() throws Exception {
        setField(mojo, "reset", false);
        mojo.execute();
        assertThat(executor, wasNotCalledWith(projessConfigArgCaptor, "--reset"));
    }

    public void shouldPassAlongRunnerModeIfProvided() throws Exception {
        setField(mojo, "runnerMode", "DEBUG");
        mojo.execute();
        assertThat(executor, wasCalledWith(projessConfigArgCaptor, "--runnerMode DEBUG"));
    }

    public void shouldPassAlongServerIfProvided() throws Exception {
        setField(mojo, "server", "http://localhost:9999");
        mojo.execute();
        assertThat(executor, wasCalledWith(projessConfigArgCaptor, "--server http://localhost:9999"));
    }

    public void shouldPassAlongServerHandlerPrefixIfProvided() throws Exception {
        setField(mojo, "serverHandlerPrefix", "/foo");
        mojo.execute();
        assertThat(executor, wasCalledWith(projessConfigArgCaptor, "--serverHandlerPrefix /foo"));
    }

    public void shouldPassAlongTestOutputIfProvided() throws Exception {
        setField(mojo, "testOutput", "target/test-out");
        mojo.execute();
        assertThat(executor, wasCalledWith(projessConfigArgCaptor, "--testOutput target/test-out"));
    }

    public void shouldPassAlongTestsIfProvided() throws Exception {
        setField(mojo, "tests", "PersonTest");
        mojo.execute();
        assertThat(executor, wasCalledWith(projessConfigArgCaptor, "--tests PersonTest"));
    }

    public void shouldSetVerboseIfTrue() throws Exception {
        setField(mojo, "verbose", true);
        mojo.execute();
        assertThat(executor, wasCalledWith(projessConfigArgCaptor, "--verbose"));
    }

    public void shouldNotSetVerboseIfFalse() throws Exception {
        setField(mojo, "verbose", false);
        mojo.execute();
        assertThat(executor, wasNotCalledWith(projessConfigArgCaptor, "--verbose"));
    }

}
