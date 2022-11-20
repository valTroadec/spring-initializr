package com.tempproject.initializr.generator.contributor;

import io.spring.initializr.generator.io.template.MustacheTemplateRenderer;
import io.spring.initializr.generator.io.template.TemplateRenderer;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.contributor.ProjectContributor;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

@RequiredArgsConstructor
public class DockerContributor implements ProjectContributor {

  private final TemplateRenderer dockerTemplateRenderer;

  private final ProjectDescription projectDescription;

  @Override
  public void contribute(Path projectRoot) throws IOException {
    Path file = Files.createFile(projectRoot.resolve("Dockerfile"));

    String dockerfile = ((MustacheTemplateRenderer) dockerTemplateRenderer)
            .render("Dockerfile", Collections.singletonMap("key", "value"));
    String javaVersion = projectDescription.getLanguage().jvmVersion();
    String name = projectDescription.getName();

    try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(file))) {
      writer.println(
              dockerfile.replace("tmpversion", javaVersion).replace("tmp.jar", name + ".jar")
          );
    }
  }

  @Override
  public int getOrder() {
    return ProjectContributor.super.getOrder();
  }
}
