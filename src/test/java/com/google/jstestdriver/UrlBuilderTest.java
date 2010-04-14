package com.google.jstestdriver;

import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

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
