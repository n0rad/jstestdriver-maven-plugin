package com.google.jstestdriver;

import org.apache.maven.plugin.MojoExecutionException;

import java.io.IOException;
import java.io.Closeable;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.util.regex.Matcher;

/**
 * Copyright 2009-2010, Burke Webster (burke.webster@gmail.com)
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

    public static String run(JarProcessConfiguration jarConfig, boolean streamResults) throws MojoExecutionException
    {
        StringBuffer buffer = new StringBuffer();
        try
        {
            Process process = ProcessFactory.create(jarConfig);
            BufferedReader processInputStreamReader = ProcessUtils.getInputStream(process);
            for (String line = processInputStreamReader.readLine(); line != null; line = processInputStreamReader.readLine())
            {
                if (streamResults)
                {
                    System.out.println(line);
                }
                buffer.append(line).append("\n");
            }

            process.waitFor();

            return buffer.toString();
        }
        catch (IOException e)
        {
            throw new MojoExecutionException(e.getMessage());
        }
        catch (InterruptedException e)
        {
            throw new MojoExecutionException(e.getMessage());
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
