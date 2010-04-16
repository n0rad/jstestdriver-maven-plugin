package com.google.jstestdriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2009-2010, Burke Webster (burke.webster@gmail.com)
 **/
public class ProcessFactory
{
    public static Process create(JarProcessConfiguration configuration) throws IOException
    {
        List<String> commandLine = new ArrayList<String>();
        commandLine.add(configuration.getExecutable());
        commandLine.addAll(configuration.getArguments());

        ProcessBuilder pb = new ProcessBuilder(commandLine);
        pb.redirectErrorStream(true);
        return pb.start();
    }

}
