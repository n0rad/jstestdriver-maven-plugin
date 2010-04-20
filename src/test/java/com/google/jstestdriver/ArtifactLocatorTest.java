package com.google.jstestdriver;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;
import static org.testng.AssertJUnit.assertSame;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;

/**
 * Copyright 2009-2010, Burke Webster (burke.webster@gmail.com)
 */
@Test
public class ArtifactLocatorTest
{
    private Artifact artifact;
    private MavenProject project;
    private ArtifactLocator locator;

    @BeforeMethod
    public void setUp()
    {
        artifact = mock(Artifact.class);
        when(artifact.getGroupId()).thenReturn("com.google.jstestdriver");
        when(artifact.getArtifactId()).thenReturn("artifact-test");

        project = mock(MavenProject.class);
        when(project.getArtifacts()).thenReturn(Collections.singleton(artifact));

        locator = new ArtifactLocator(project);
    }

    public void shouldFindArtifact()
    {
        assertSame(locator.findArtifact("com.google.jstestdriver", "artifact-test"), artifact);
    }

    public void shouldThrowExceptionForInvalidGroupId()
    {
        try
        {
            new ArtifactLocator(project).findArtifact("com.google", "artifact-test");
            fail("Should have thrown an exception for invalid groupId");
        }
        catch (Exception ignored)
        {

        }
    }

    public void shouldThrowExceptionForInvalidArtifactId()
    {
        try
        {
            new ArtifactLocator(project).findArtifact("com.google.jstestdriver", "foo");
            fail("Should have thrown an exception for invalid artifactId");
        }
        catch (Exception ignored)
        {

        }
    }
}
