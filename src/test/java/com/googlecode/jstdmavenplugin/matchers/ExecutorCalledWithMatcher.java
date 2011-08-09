package com.googlecode.jstdmavenplugin.matchers;

import com.googlecode.jstdmavenplugin.ProcessConfiguration;
import com.googlecode.jstdmavenplugin.ProcessExecutor;
import org.apache.commons.lang.StringUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertTrue;

/**
 * Copyright 2009-2011, Burke Webster (burke.webster@gmail.com)
 */
public class ExecutorCalledWithMatcher extends TypeSafeMatcher<ProcessExecutor> {

    protected String commandLineArg;
    protected ArgumentCaptor<ProcessConfiguration> processConfigurationArgumentCaptor;

    public ExecutorCalledWithMatcher(ArgumentCaptor<ProcessConfiguration> processConfigurationArgumentCaptor, String commandLineArg) {
        this.processConfigurationArgumentCaptor = processConfigurationArgumentCaptor;
        this.commandLineArg = commandLineArg;
    }

    public static ExecutorCalledWithMatcher wasCalledWith(ArgumentCaptor<ProcessConfiguration> config, String value) {
        return new ExecutorCalledWithMatcher(config, value);
    }

    @Override
    public boolean matchesSafely(ProcessExecutor executor) {
        verify(executor).execute(processConfigurationArgumentCaptor.capture());
        String commandLine = StringUtils.join(processConfigurationArgumentCaptor.getValue().getArguments(), " ");
        return commandLine.contains(commandLineArg);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(String.format(" command line args should have contained %s", commandLineArg));
    }
}
