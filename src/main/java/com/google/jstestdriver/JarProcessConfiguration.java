package com.google.jstestdriver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright 2009-2010, Burke Webster (burke.webster@gmail.com)
 **/
public class JarProcessConfiguration
{
    private List<String> arguments;

    public JarProcessConfiguration(String jarPath)
    {
        arguments = new ArrayList<String>();
        arguments.add("-jar");
        arguments.add(jarPath);
    }

    public void addArgument(String... values)
    {
        arguments.addAll(Arrays.asList(values));
    }

    public List<String> getArguments()
    {
        return arguments;
    }

    public String getExecutable()
    {
        return "java";
    }
}
