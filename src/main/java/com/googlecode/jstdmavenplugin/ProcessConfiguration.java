package com.googlecode.jstdmavenplugin;

import java.util.List;

/**
 * Copyright 2009-2011, Burke Webster (burke.webster@gmail.com)
 */
public interface ProcessConfiguration
{
    String getExecutable();

    List<String> getArguments();

    List<String> getExecutableOptions();

    List<String> getFullCommand();
}
