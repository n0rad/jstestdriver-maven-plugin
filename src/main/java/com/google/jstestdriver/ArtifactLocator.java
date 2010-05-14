package com.google.jstestdriver;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.util.Iterator;
import java.util.List;

/**
 * Copyright 2009-2010, Burke Webster (burke.webster@gmail.com)
 */
public class ArtifactLocator
{
    private MavenProject project;

    public ArtifactLocator(MavenProject project)
    {
        this.project = project;
    }

    public Artifact findArtifact(String groupId, String artifactId)
    {
        for (Object object : project.getArtifacts())
        {
            Artifact artifact = (Artifact) object;
            if (artifact.getGroupId().equals(groupId) && artifact.getArtifactId().equals(artifactId))
            {
                return artifact;
            }
        }

        throw new RuntimeException(String.format("Failed to locate %s:%s", groupId, artifactId));
    }
}
