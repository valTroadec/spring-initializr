package com.tempproject.initializr.config.projectgenerationconfiguration;

import com.tempproject.initializr.generator.CustomMutableProjectDescription;
import com.tempproject.initializr.generator.contributor.api.APIControllerSourceCodeProjectContributor;
import com.tempproject.initializr.generator.contributor.api.config.APIRestControllerProjectGenerationDefaultContributorConfiguration;
import com.tempproject.initializr.generator.contributor.api.customizer.APIApplicationTypeCustomizer;
import com.tempproject.initializr.generator.contributor.api.customizer.APICompilationUnitCustomizer;
import com.tempproject.initializr.generator.contributor.api.customizer.APISourceCodeCustomizer;
import io.spring.initializr.generator.condition.ConditionalOnLanguage;
import io.spring.initializr.generator.io.IndentingWriterFactory;
import io.spring.initializr.generator.language.java.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@ConditionalOnLanguage(JavaLanguage.ID)
@RequiredArgsConstructor
@Import(APIRestControllerProjectGenerationDefaultContributorConfiguration.class)
public class CustomAPIConfiguration {

    private final CustomMutableProjectDescription projectDescription;

    private final IndentingWriterFactory indentingWriterFactory;

    @Bean
    public APIControllerSourceCodeProjectContributor<JavaTypeDeclaration, JavaCompilationUnit, JavaSourceCode> apiJavaSourceCodeProjectContributor(
            ObjectProvider<APIApplicationTypeCustomizer<?>> apiApplicationTypeCustomizers,
            ObjectProvider<APICompilationUnitCustomizer<?, ?>> apiCompilationUnitCustomizers,
            ObjectProvider<APISourceCodeCustomizer<?, ?, ?>> apiSourceCodeCustomizers) {
        return new APIControllerSourceCodeProjectContributor(this.projectDescription, JavaSourceCode::new,
                new JavaSourceCodeWriter(this.indentingWriterFactory), apiApplicationTypeCustomizers,
                apiCompilationUnitCustomizers, apiSourceCodeCustomizers);
    }


}
