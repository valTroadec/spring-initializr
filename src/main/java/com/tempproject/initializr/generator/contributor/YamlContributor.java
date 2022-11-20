package com.tempproject.initializr.generator.contributor;

import io.spring.initializr.generator.project.contributor.ProjectContributor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class YamlContributor implements ProjectContributor {
    @Override
    public void contribute(Path path) throws IOException {

        new File(path.toFile(), "src/main/resources/application.properties").delete();
        Path file = Files.createFile(path.resolve("src/main/resources/application.yml"));
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(file))) {
            writer.println("Test");
        }
    }

    @Override
    public int getOrder() {
        return ProjectContributor.super.getOrder();
    }
}
