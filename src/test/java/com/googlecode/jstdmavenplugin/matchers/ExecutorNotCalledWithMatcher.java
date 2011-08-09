package com.googlecode.jstdmavenplugin.matchers;

import com.googlecode.jstdmavenplugin.ProcessConfiguration;
import com.googlecode.jstdmavenplugin.ProcessExecutor;
import org.hamcrest.Description;
import org.mockito.ArgumentCaptor;

/**
 * Copyright 2009-2011, Burke Webster (burke.webster@gmail.com)
 */
public class ExecutorNotCalledWithMatcher extends ExecutorCalledWithMatcher {

    public ExecutorNotCalledWithMatcher(ArgumentCaptor<ProcessConfiguration> processConfigurationArgumentCaptor, String commandLineArg)
    {
        super(processConfigurationArgumentCaptor, commandLineArg);
    }

    public static ExecutorNotCalledWithMatcher wasNotCalledWith(ArgumentCaptor<ProcessConfiguration> config, String value) {
        return new ExecutorNotCalledWithMatcher(config, value);
    }

    @Override
    public boolean matchesSafely(ProcessExecutor executor) {
        return !super.matchesSafely(executor);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(String.format(" command line args should not have contained %s", commandLineArg));
    }
}
