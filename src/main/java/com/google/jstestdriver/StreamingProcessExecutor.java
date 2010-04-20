package com.google.jstestdriver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2009-2010, Burke Webster (burke.webster@gmail.com)
 */
public class StreamingProcessExecutor implements ProcessExecutor
{
    public String execute(ProcessConfiguration jarConfig)
    {
        StringBuffer buffer = new StringBuffer();
        try
        {
            Process process = create(jarConfig);
            BufferedReader inputReader = getInputStream(process);
            for (String line = inputReader.readLine(); line != null; line = inputReader.readLine())
            {
                System.out.println(line);
                buffer.append(line).append("\n");
            }

            process.waitFor();

            return buffer.toString();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private Process create(ProcessConfiguration configuration) throws IOException
    {
        List<String> commandLine = new ArrayList<String>();
        commandLine.add(configuration.getExecutable());
        commandLine.addAll(configuration.getArguments());

        ProcessBuilder pb = new ProcessBuilder(commandLine);
        pb.redirectErrorStream(true);
        return pb.start();
    }

    private BufferedReader getInputStream(Process p)
    {
        InputStream inputStream = p.getInputStream();
        return new BufferedReader(new InputStreamReader(inputStream));
    }
}
