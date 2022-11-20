package com.tempproject.initializr.generator;

import com.tempproject.initializr.generator.contributor.InitializationProjectContributor;
import io.spring.initializr.generator.io.IndentingWriterFactory;
import io.spring.initializr.generator.project.ProjectGenerator;

public class CustomProjectGenerator {

    public CustomProjectGenerator(){
    }

    public static ProjectGenerator createProjectGenerator() {
        return new ProjectGenerator((context) -> {
            context.registerBean(IndentingWriterFactory.class, IndentingWriterFactory::withDefaultSettings);
            context.registerBean(InitializationProjectContributor.class, InitializationProjectContributor::new);
        });
    }
}
