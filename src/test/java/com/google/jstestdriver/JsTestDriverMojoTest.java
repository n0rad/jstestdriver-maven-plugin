package com.google.jstestdriver;

import org.apache.maven.project.MavenProject;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;

import static com.google.jstestdriver.matchers.ExecutorCalledWithMatcher.wasCalledWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * Copyright 2009-2011, Burke Webster (burke.webster@gmail.com)
 */
@Test
public class JsTestDriverMojoTest extends AbstractMojoTest {

    private StreamingProcessExecutor executor;
    private ResultsProcessor processor;
    private JsTestDriverMojo mojo;
    private ArgumentCaptor<ProcessConfiguration> projessConfigArgCaptor;

    @BeforeMethod
    public void setUp() throws Exception {
        executor = mock(StreamingProcessExecutor.class);
        processor = mock(ResultsProcessor.class);
        mojo = new JsTestDriverMojo(executor, processor);

        MavenProject project = getMockMavenProject(mojo);
        setField(mojo, "mavenProject", project);
        projessConfigArgCaptor = ArgumentCaptor.forClass(ProcessConfiguration.class);
    }

    public void shouldSkipTests() throws Exception {
        setField(mojo, "skipTests", true);

        mojo.execute();

        verify(executor, never()).execute(any(ProcessConfiguration.class));
        verify(processor, never()).processResults(anyString());
    }

    public void shouldAdjustConfigPathIfBasePathIsGiven() throws Exception {
        setField(mojo, "basePath", "src");
        setField(mojo, "config", "test/resources/jsTestDriver.conf");

        mojo.execute();

        assertThat(executor, wasCalledWith(projessConfigArgCaptor, "--config src/test/resources/jsTestDriver.conf"));
    }

    public void shouldCreateTestOutputDirectoryIfNotExists() throws Exception {
        File testOutput = new File("target/test-out");
        removeDir(testOutput);

        setField(mojo, "testOutput", "target/test-out");
        mojo.execute();

        assertTrue(testOutput.isDirectory());
    }

    public void shouldNotFailIfTestOutputDirectoryAlreadyExists() throws Exception {
        File testOutput = new File("target/test-out");
        makeDir(testOutput);

        setField(mojo, "testOutput", "target/test-out");
        mojo.execute();

        assertTrue(testOutput.isDirectory());
    }

    public void shouldUseJarIfProvided() throws Exception {
        String jarFile = "test/resources/jstestdriver-1.X.jar";
        setField(mojo, "jar", jarFile);
        mojo.execute();

        assertThat(executor, wasCalledWith(projessConfigArgCaptor, "-jar " + jarFile));
    }

    private void removeDir(File testOutput) {
        if (testOutput.isDirectory()) {
            if (!testOutput.delete()) {
                fail("Failed to clean up directory in test setup");
            }
        }
    }

    private void makeDir(File testOutput) {
        if (!testOutput.isDirectory()) {
            if (!testOutput.mkdir()) {
                fail("Failed to create directory in test setup");
            }
        }
    }

}
