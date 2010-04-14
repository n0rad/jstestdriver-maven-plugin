package com.google.jstestdriver;

import java.io.IOException;
import java.io.Closeable;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.InputStream;

/**
 * Copyright © 2010, Burke Webster (burke.webster@gmail.com)
 **/
public class ProcessUtils
{
    public static void close(Process... processes)
    {
        for (Process process : processes)
        {
            if (process != null)
            {
                try
                {
                    process.getErrorStream().close();
                    process.getOutputStream().close();
                    process.getInputStream().close();
                }
                catch (IOException e)
                {
                    System.out.println("Exception caught when closing process input/output stream(s)");
                    e.printStackTrace();
                }
                process.destroy();
            }
        }
    }

    public static BufferedReader getInputStream(Process p)
    {
        InputStream inputStream = p.getInputStream();
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    public static void closeProcessAndStreams(Process p, Closeable... streams)
    {
        for (Closeable stream : streams)
        {
            if (stream != null)
            {
                try
                {
                    stream.close();
                }
                catch (IOException e)
                {
                    System.out.println("Error closing stream:");
                    e.printStackTrace();
                }
            }
        }
        if (p != null)
        {
            p.destroy();
        }
    }
}
