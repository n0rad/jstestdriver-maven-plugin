package com.google.jstestdriver;

import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;

/**
 * Copyright 2009-2010, Burke Webster (burke.webster@gmail.com)
 **/
public class FileUtils
{
    public static void makeDirectoryIfNotExists(String testOutput) throws MojoExecutionException
    {
        File directory = new File(testOutput);
        if (!directory.exists())
        {
            if (!directory.mkdirs())
            {
                throw new MojoExecutionException("Failed to create testOutput directory");
            }
        }
    }
}
