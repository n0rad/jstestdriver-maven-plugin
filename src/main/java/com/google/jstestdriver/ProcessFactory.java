package com.google.jstestdriver;

import java.io.IOException;
import java.util.List;

/**
 * Copyright © 2010, Burke Webster (burke.webster@gmail.com)
 **/
public class ProcessFactory
{
    public static Process create(JarApplication runner) throws IOException
    {
        List<String> processArgs = runner.getArguments();

        ProcessBuilder pb = new ProcessBuilder(processArgs);
        pb.redirectErrorStream(true);
        return pb.start();
    }

}
