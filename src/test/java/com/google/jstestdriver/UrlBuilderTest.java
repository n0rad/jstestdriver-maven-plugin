package com.google.jstestdriver;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 * Copyright 2009-2010, Burke Webster (burke.webster@gmail.com)
 */
@Test
public class UrlBuilderTest
{
    public void shouldNotAddBeginingSlash()
    {
        assertEquals(UrlBuilder.build("foo/", "bar"), "foo/bar");
    }

    public void shouldBuildUrlWhenEachChunkStartsWithSlash()
    {
        assertEquals(UrlBuilder.build("/foo", "/bar"), "/foo/bar");
    }

    public void shouldBuildUrlWhenEachChunkEndsWithSlash()
    {
        assertEquals(UrlBuilder.build("/foo/", "bar/"), "/foo/bar/");
    }

    public void shouldCollapseMultipleSlashes()
    {
        assertEquals(UrlBuilder.build("/foo/", "/bar"), "/foo/bar");
    }

    public void shouldNotLooseEndingSlash()
    {
        assertEquals(UrlBuilder.build("/foo", "/bar/"), "/foo/bar/");
    }
}
