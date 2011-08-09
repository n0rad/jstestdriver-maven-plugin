package com.googlecode.jstdmavenplugin;

/**
 * Copyright 2009-2011, Burke Webster (burke.webster@gmail.com)
 */
public class UrlBuilder
{
    public static String build(String... parts)
    {
        StringBuffer buffer = new StringBuffer();
        for (String part : parts)
        {
            buffer.append(part);
        }
        return buffer.toString().replaceAll("[/]{2,}", "/");
    }
}
