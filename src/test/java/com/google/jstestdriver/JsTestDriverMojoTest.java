package com.google.jstestdriver;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.project.MavenProject;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertTrue;

@Test
public class JsTestDriverMojoTest extends AbstractMojoTest {

    public void shouldAdjustConfigPathIfBasePathIsGiven() throws Exception {
        ArgumentCaptor<ProcessConfiguration> config = ArgumentCaptor.forClass(ProcessConfiguration.class);
        StreamingProcessExecutor executor = mock(StreamingProcessExecutor.class);

        JsTestDriverMojo mojo = new JsTestDriverMojo(executor, mock(ResultsProcessor.class));

        MavenProject project = getMockMavenProject(mojo);
        setField(mojo, "mavenProject", project);
        setField(mojo, "basePath", "src");
        setField(mojo, "config", "src/test/resources/jsTestDriver.conf");

        mojo.execute();

        verify(executor).execute(config.capture());
        String commandLine = StringUtils.join(config.getValue().getArguments(), " ");
        assertTrue(commandLine.contains("--config test/resources/jsTestDriver.conf"));

    }

    public void shouldSkipTests() throws Exception {
        StreamingProcessExecutor executor = mock(StreamingProcessExecutor.class);
        ResultsProcessor processor = mock(ResultsProcessor.class);
        JsTestDriverMojo mojo = new JsTestDriverMojo(executor, processor);

        MavenProject project = getMockMavenProject(mojo);
        setField(mojo, "mavenProject", project);
        setField(mojo, "skipTests", true);

        mojo.execute();

        verify(executor, never()).execute(any(ProcessConfiguration.class));
        verify(processor, never()).processResults(anyString());
    }


}
