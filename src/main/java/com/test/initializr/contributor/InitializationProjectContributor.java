package com.test.initializr.contributor;

import io.spring.initializr.generator.project.contributor.ProjectContributor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class InitializationProjectContributor implements ProjectContributor {

    @Override
    public void contribute(Path projectRoot) throws IOException {
        Path file = Files.createFile(projectRoot.resolve("hello.txt"));
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(file))) {
            writer.println("Test");
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
