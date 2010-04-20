package com.google.jstestdriver;

import java.io.File;

/**
 * Copyright 2009-2010, Burke Webster (burke.webster@gmail.com)
 */
public class FileUtils
{
    public static void makeDirectoryIfNotExists(String testOutput)
    {
        File directory = new File(testOutput);
        if (!directory.exists())
        {
            if (!directory.mkdirs())
            {
                throw new RuntimeException(String.format("Failed to create %s directory", testOutput));
            }
        }
    }
}
