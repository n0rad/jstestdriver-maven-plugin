package com.google.jstestdriver;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class AbstractMojoTest {

    public static final String GROUP_ID = "com.google.jstestdriver";
    public static final String ARTIFACT_ID = "jstestdriver";

    protected void setField(JsTestDriverMojo mojo, String fieldName, Object object) throws
            NoSuchFieldException,
            IllegalAccessException
    {
        Field field = mojo.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(mojo, object);
    }

    protected MavenProject getMockMavenProject(JsTestDriverMojo mojo) throws
            NoSuchFieldException,
            IllegalAccessException
    {
        File file = mock(File.class);
        when(file.getAbsolutePath()).thenReturn("/foo/bar");

        Artifact artifact = mock(Artifact.class);
        when(artifact.getGroupId()).thenReturn(GROUP_ID);
        when(artifact.getArtifactId()).thenReturn(ARTIFACT_ID);
        when(artifact.getFile()).thenReturn(file);

        Set<Artifact> artifacts = new HashSet<Artifact>();
        artifacts.add(artifact);

        MavenProject project = mock(MavenProject.class);
        when(project.getArtifacts()).thenReturn(artifacts);

        setField(mojo, "groupId", GROUP_ID);
        setField(mojo, "artifactId", ARTIFACT_ID);
        setField(mojo, "mavenProject", project);
        setField(mojo, "dependencies", Collections.<Artifact>emptyList());

        return project;
    }

}
