package com.tempproject.initializr.config.projectgenerationconfiguration;


import com.tempproject.initializr.api.CustomProjectGenerationController;
import com.tempproject.initializr.generator.CustomProjectGenerator;
import com.tempproject.initializr.generator.CustomProjectRequestToDescriptionConverter;
import io.spring.initializr.generator.io.template.MustacheTemplateRenderer;
import io.spring.initializr.generator.io.template.TemplateRenderer;
import io.spring.initializr.generator.project.ProjectGenerator;
import io.spring.initializr.metadata.InitializrMetadataProvider;
import io.spring.initializr.web.project.DefaultProjectRequestPlatformVersionTransformer;
import io.spring.initializr.web.project.ProjectGenerationInvoker;
import io.spring.initializr.web.project.ProjectRequestPlatformVersionTransformer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomInitializrAutoConfiguration {

  @Bean
  public ProjectGenerator projectGenerator(){
    return CustomProjectGenerator.createProjectGenerator();
  }

  @Bean
  public CustomProjectGenerationController projectGenerationController(
      InitializrMetadataProvider metadataProvider,
      ObjectProvider<ProjectRequestPlatformVersionTransformer> platformVersionTransformer,
      ApplicationContext applicationContext) {

    ProjectGenerationInvoker projectGenerationInvoker =
        new ProjectGenerationInvoker(
            applicationContext,
            new CustomProjectRequestToDescriptionConverter(
                platformVersionTransformer.getIfAvailable(
                    DefaultProjectRequestPlatformVersionTransformer::new)));
    return new CustomProjectGenerationController(metadataProvider, projectGenerationInvoker);
  }

  @Bean
  @ConditionalOnMissingBean
  public TemplateRenderer dockerTemplateRenderer(){
    return new MustacheTemplateRenderer("classpath:templates/docker");
  }


}
