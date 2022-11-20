package com.test.initializr.customizer;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import org.springframework.context.annotation.Bean;
/*
@ProjectGenerationConfiguration
public class BuildCustomizer {

    @Bean
    @ConditionalOnBuildSystem(MavenBuildSystem.ID)
    public io.spring.initializr.generator.spring.build.BuildCustomizer<Build> customBuildCustomizer(){
        return (build) -> {
            build.properties().version("example.version", "1.0.0.RELEASE");
            build.settings().group("test")
                    .artifact("test")
                    .version("1");
        };
    }
}*/
