package com.google.jstestdriver;

import java.util.List;

/**
 * Copyright 2009-2010, Burke Webster (burke.webster@gmail.com)
 */
public interface ProcessConfiguration
{
    String getExecutable();

    List<String> getArguments();
}
