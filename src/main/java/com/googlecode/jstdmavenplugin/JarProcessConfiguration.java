package com.googlecode.jstdmavenplugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright 2009-2011, Burke Webster (burke.webster@gmail.com)
 */
public class JarProcessConfiguration implements ProcessConfiguration
{
    private List<String> arguments;
    private List<String> classpath;
    private List<String> options;
    private String jarPath;
    private static final String JAVA_ARG = "java";
    private static final String JAR_ARG = "-jar";

    public JarProcessConfiguration(String jarPath)
    {
        this.jarPath = jarPath;
        arguments = new ArrayList<String>();
        classpath = new ArrayList<String>();
        options = new ArrayList<String>();
    }

    public void addArgument(String... values)
    {
        arguments.addAll(Arrays.asList(values));
    }

    public void addClasspath(String... values)
    {
        classpath.addAll(Arrays.asList(values));
    }

    public void addExecutableOptions(String... jvmOptions) {
        options.addAll(Arrays.asList(jvmOptions));
    }

    public List<String> getExecutableOptions() {
        return options;
    }

    public List<String> getFullCommand() {
        List<String> commandLine = new ArrayList<String>();
        commandLine.add(getExecutable());
        commandLine.addAll(getExecutableOptions());
        commandLine.addAll(getArguments());
        return commandLine;
    }

    public List<String> getArguments()
    {
        List<String> allArguments = new ArrayList<String>();

        if (!classpath.isEmpty())
        {
            allArguments.add("-classpath");
            allArguments.addAll(classpath);
        }

        allArguments.add(JAR_ARG);
        allArguments.add(jarPath);

        allArguments.addAll(arguments);
        return allArguments;
    }

    public String getExecutable()
    {
        return JAVA_ARG;
    }
}
