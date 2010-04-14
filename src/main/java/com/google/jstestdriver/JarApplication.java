package com.google.jstestdriver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright © 2010, Burke Webster (burke.webster@gmail.com)
 **/
public class JarApplication
{
    private String jarPath;
    private List<String> arguments;

    public JarApplication(String jarPath)
    {
        this.jarPath = jarPath;

        arguments = new ArrayList<String>();
        arguments.add("java");
        this.addArgument("-jar", this.jarPath);
    }

    public void addArgument(String... values)
    {
        arguments.addAll(Arrays.asList(values));
    }

    public List<String> getArguments()
    {
        return arguments;
    }
}
