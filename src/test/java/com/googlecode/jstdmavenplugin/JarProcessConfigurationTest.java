package com.googlecode.jstdmavenplugin;

import com.googlecode.jstdmavenplugin.JarProcessConfiguration;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.assertEquals;

/**
 * Copyright 2009-2011, Burke Webster (burke.webster@gmail.com)
 */
@Test
public class JarProcessConfigurationTest
{
    public void shouldBuildUpArguments()
    {
        String path = "/some/path/to/a.jar";
        JarProcessConfiguration config = new JarProcessConfiguration(path);
        config.addArgument("-test");
        config.addArgument("works");

        assertEquals(config.getArguments(), Arrays.asList("-jar", path, "-test", "works"));
    }

    public void shouldAddClasspathArgs()
    {
        String path = "/some/path/to/a.jar";
        JarProcessConfiguration config = new JarProcessConfiguration(path);
        config.addArgument("-test");
        config.addArgument("works");
        config.addClasspath("/some/jar/path/library.jar");

        assertEquals(config.getArguments(), Arrays.asList("-classpath", "/some/jar/path/library.jar", "-jar", path, "-test", "works"));
    }

    public void executableShouldBeJava()
    {
        assertEquals(new JarProcessConfiguration(null).getExecutable(), "java");
    }
}
