package com.tempproject.initializr.generator.contributor.api.config;

import com.tempproject.initializr.generator.CustomMutableProjectDescription;
import com.tempproject.initializr.generator.contributor.api.customizer.APIApplicationTypeCustomizer;
import io.spring.initializr.generator.language.Annotation;
import io.spring.initializr.generator.language.java.JavaTypeDeclaration;
import io.spring.initializr.generator.project.ProjectDescription;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Modifier;

@RequiredArgsConstructor
public class APIRestControllerProjectGenerationDefaultContributorConfiguration {

  private final ProjectDescription description;

  @Bean
  APIApplicationTypeCustomizer<JavaTypeDeclaration> apiMethodContributor() {
    return (typeDeclaration) -> {
      typeDeclaration.annotate(
          Annotation.name(
              "org.springframework.web.bind.annotation.RestController",
              (builder) ->
                  builder.attribute(
                      "value",
                      String.class,
                      ((CustomMutableProjectDescription) description).getApiName())));
      typeDeclaration.modifiers(Modifier.PUBLIC);
      /*typeDeclaration.addMethodDeclaration(
          JavaMethodDeclaration.method("test")
              .modifiers(Modifier.PUBLIC)
              .returning("void")
              .parameters(new Parameter("java.lang.String", "testParameter"))
              .body(new JavaExpressionStatement(new JavaExpression())));*/
    };
  }
}
