package com.test.initializr.config;

/*
 * Copyright 2012-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.test.initializr.api.Controller;
import com.test.initializr.generator.CustomMutableProjectDescription;
import com.test.initializr.generator.CustomProjectGenerator;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerator;
import io.spring.initializr.metadata.InitializrMetadataProvider;
import io.spring.initializr.web.project.DefaultProjectRequestPlatformVersionTransformer;
import io.spring.initializr.web.project.ProjectGenerationInvoker;
import io.spring.initializr.web.project.ProjectRequestPlatformVersionTransformer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration Auto-configuration} to
 * configure Spring initializr. In a web environment, configures the necessary controller to serve
 * the applications from the root context.
 *
 * @author Stephane Nicoll
 */
@Configuration
public class CustomInitializrAutoConfiguration {

  @Bean
  public ProjectGenerator projectGenerator(){
    return CustomProjectGenerator.createProjectGenerator();
  }

  @Bean
  public Controller projectGenerationController(
      InitializrMetadataProvider metadataProvider,
      ObjectProvider<ProjectRequestPlatformVersionTransformer> platformVersionTransformer,
      ApplicationContext applicationContext) {

    ProjectGenerationInvoker projectGenerationInvoker =
        new ProjectGenerationInvoker(
            applicationContext,
            new CustomProjectRequestToDescriptionConverter(
                platformVersionTransformer.getIfAvailable(
                    DefaultProjectRequestPlatformVersionTransformer::new)));
    return new Controller(metadataProvider, projectGenerationInvoker);
  }


}
