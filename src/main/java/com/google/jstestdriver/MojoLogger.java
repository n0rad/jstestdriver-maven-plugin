package com.google.jstestdriver;

import org.apache.maven.plugin.logging.Log;

/**
 * Copyright 2009-2010, Burke Webster (burke.webster@gmail.com)
 */
public class MojoLogger {

    private Log log;
    private static MojoLogger instance;

    private MojoLogger(Log log)
    {
        this.log = log;
    }

    public static void bindLog(Log log)
    {
        instance = new MojoLogger(log);
    }

    public static MojoLogger getInstance()
    {
        if (instance == null) {
            throw new RuntimeException("Failed to find log - you probably need to first call bindLog(Log)");
        }
        return instance;
    }

    public Log getLog()
    {
        return log;
    }
}
