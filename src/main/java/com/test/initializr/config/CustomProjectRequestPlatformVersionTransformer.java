package com.test.initializr.config;

import io.spring.initializr.generator.version.Version;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.web.project.ProjectRequestPlatformVersionTransformer;

public class CustomProjectRequestPlatformVersionTransformer
    implements ProjectRequestPlatformVersionTransformer {
  public CustomProjectRequestPlatformVersionTransformer() {}

  public Version transform(Version platformVersion, InitializrMetadata metadata) {
    return metadata
        .getConfiguration()
        .getEnv()
        .getPlatform()
        .formatPlatformVersion(platformVersion);
  }
}
