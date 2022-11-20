package com.test.initializr.generator;

import com.test.initializr.contributor.ApimPoliciesContributor;
import com.test.initializr.contributor.InitializationProjectContributor;
import io.spring.initializr.generator.io.IndentingWriterFactory;
import io.spring.initializr.generator.io.template.MustacheTemplateRenderer;
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
