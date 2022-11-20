package com.tempproject.initializr.config.projectgenerationconfiguration;

import com.tempproject.initializr.generator.contributor.DockerContributor;
import io.spring.initializr.generator.io.template.TemplateRenderer;
import io.spring.initializr.generator.project.ProjectDescription;
import org.springframework.context.annotation.Bean;

public class CustomDockerProjectGenerationConfiguration {

    @Bean
    public DockerContributor dockerContributor(TemplateRenderer dockerTemplateRenderer, ProjectDescription projectDescription){
        return new DockerContributor(dockerTemplateRenderer, projectDescription);
    }
}
