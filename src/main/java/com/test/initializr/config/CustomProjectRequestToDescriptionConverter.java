package com.test.initializr.config;

import com.test.initializr.generator.CustomMutableProjectDescription;
import com.test.initializr.generator.apim.ApimPolicies;
import com.test.initializr.generator.apim.ApimPoliciesPrivate;
import com.test.initializr.generator.apim.ApimPoliciesPublic;
import com.test.initializr.request.CustomProjectRequest;
import io.spring.initializr.generator.buildsystem.BuildSystem;
import io.spring.initializr.generator.language.Language;
import io.spring.initializr.generator.packaging.Packaging;
import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.metadata.*;
import io.spring.initializr.metadata.support.MetadataBuildItemMapper;
import io.spring.initializr.web.project.*;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

public class CustomProjectRequestToDescriptionConverter< R extends CustomProjectRequest> extends DefaultProjectRequestToDescriptionConverter
    implements ProjectRequestToDescriptionConverter<ProjectRequest> {

  private final ProjectRequestPlatformVersionTransformer platformVersionTransformer;

  public CustomProjectRequestToDescriptionConverter() {
    this(
        (version, metadata) -> {
          return version;
        });
  }

  public CustomProjectRequestToDescriptionConverter(
      ProjectRequestPlatformVersionTransformer platformVersionTransformer) {
    Assert.notNull(platformVersionTransformer, "PlatformVersionTransformer must not be null");
    this.platformVersionTransformer = platformVersionTransformer;
  }

  public ProjectDescription convert(ProjectRequest request, InitializrMetadata metadata) {
    CustomMutableProjectDescription description = new CustomMutableProjectDescription();
    this.convert((CustomProjectRequest) request, description, metadata);
    return description;
  }

  public void convert(
      CustomProjectRequest request, CustomMutableProjectDescription description, InitializrMetadata metadata) {
    this.validate(request, metadata);
    Version platformVersion = this.getPlatformVersion(request, metadata);
    List<Dependency> resolvedDependencies =
        this.getResolvedDependencies(request, platformVersion, metadata);
    this.validateDependencyRange(platformVersion, resolvedDependencies);
    description.setApplicationName(request.getApplicationName());
    description.setArtifactId(request.getArtifactId());
    description.setBaseDirectory(request.getBaseDir());
    description.setBuildSystem(this.getBuildSystem(request, metadata));
    description.setDescription(request.getDescription());
    description.setGroupId(request.getGroupId());
    description.setLanguage(
        Language.forId(
            getValueOrDefault(request.getLanguage(), metadata, "language"),
            request.getJavaVersion()));
    description.setName(request.getName());
    description.setPackageName(request.getPackageName());
    description.setPackaging(
        Packaging.forId(getValueOrDefault(request.getPackaging(), metadata, "packaging")));
    description.setPlatformVersion(platformVersion);
    description.setVersion(request.getVersion());
    resolvedDependencies.forEach(
        (dependency) -> {
          description.addDependency(
              dependency.getId(), MetadataBuildItemMapper.toDependency(dependency));
        });
    description.setApimPoliciesConfigPrivate(ApimPolicies.forIdAndValue(ApimPoliciesPrivate.ID, request.isApimPoliciesPrivate()));
    description.setApimPoliciesConfigPublic(ApimPolicies.forIdAndValue(ApimPoliciesPublic.ID, request.isApimPoliciesPublic()));
  }

  private void validate(ProjectRequest request, InitializrMetadata metadata) {
    this.validatePlatformVersion(request, metadata);
    this.validateType(request.getType(), metadata);
    this.validateLanguage(request.getLanguage(), metadata);
    this.validatePackaging(request.getPackaging(), metadata);
    this.validateDependencies(request, metadata);
  }

  private void validatePlatformVersion(ProjectRequest request, InitializrMetadata metadata) {
    Version platformVersion =
        getVersionOrDefault(request.getBootVersion(), metadata, "bootVersions");
    InitializrConfiguration.Platform platform = metadata.getConfiguration().getEnv().getPlatform();
    if (platformVersion != null && !platform.isCompatibleVersion(platformVersion)) {
      throw new InvalidProjectRequestException(
          "Invalid Spring Boot version '"
              + platformVersion
              + "', Spring Boot compatibility range is "
              + platform.determineCompatibilityRangeRequirement());
    }
  }

  private Version getVersionOrDefault(
      String version, InitializrMetadata metadata, String versionsToSearchIn) {
    if (version != null) {
      return Version.safeParse(version);
    }
    switch (versionsToSearchIn) {
      case "bootVersions":
        return Version.safeParse(metadata.getBootVersions().getDefault().getId());
    }
    throw new InvalidProjectRequestException("No version initialized for " + versionsToSearchIn);
  }

  private void validateType(String type, InitializrMetadata metadata) {
    if (type != null) {
      Type typeFromMetadata = metadata.getTypes().get(type);
      if (typeFromMetadata == null) {
        throw new InvalidProjectRequestException(
            "Unknown type '" + type + "' check project metadata");
      }

      if (!typeFromMetadata.getTags().containsKey("build")) {
        throw new InvalidProjectRequestException(
            "Invalid type '" + type + "' (missing build tag) check project metadata");
      }
    }
  }

  private void validateLanguage(String language, InitializrMetadata metadata) {
    if (language != null) {
      DefaultMetadataElement languageFromMetadata = metadata.getLanguages().get(language);
      if (languageFromMetadata == null) {
        throw new InvalidProjectRequestException(
            "Unknown language '" + language + "' check project metadata");
      }
    }
  }

  private void validatePackaging(String packaging, InitializrMetadata metadata) {
    if (packaging != null) {
      DefaultMetadataElement packagingFromMetadata = metadata.getPackagings().get(packaging);
      if (packagingFromMetadata == null) {
        throw new InvalidProjectRequestException(
            "Unknown packaging '" + packaging + "' check project metadata");
      }
    }
  }

  private void validateDependencies(ProjectRequest request, InitializrMetadata metadata) {
    List<String> dependencies =
        getValuesOrDefaults(request.getDependencies(), metadata, "dependencies");
    dependencies.forEach(
        (dep) -> {
          Dependency dependency = metadata.getDependencies().get(dep);
          if (dependency == null) {
            throw new InvalidProjectRequestException(
                "Unknown dependency '" + dep + "' check project metadata");
          }
        });
  }

  private List<String> getValuesOrDefaults(
      List<String> values, InitializrMetadata metadata, String valuesToSearchIn) {
    if (values != null) {
      return values;
    }
    switch (valuesToSearchIn) {
      case "dependencies":
        return metadata.getDependencies().getContent().stream()
            .flatMap(
                content ->
                    content.getContent().stream()
                        .map(MetadataElement::getId))
            .collect(Collectors.toList());
    }
    throw new InvalidProjectRequestException("No values initialized for " + valuesToSearchIn);
  }

  private void validateDependencyRange(
      Version platformVersion, List<Dependency> resolvedDependencies) {
    resolvedDependencies.forEach(
        (dep) -> {
          if (!dep.match(platformVersion)) {
            throw new InvalidProjectRequestException(
                "Dependency '"
                    + dep.getId()
                    + "' is not compatible with Spring Boot "
                    + platformVersion);
          }
        });
  }

  private BuildSystem getBuildSystem(ProjectRequest request, InitializrMetadata metadata) {
    String type = getValueOrDefault(request.getType(), metadata, "types");
    Type typeFromMetadata = metadata.getTypes().get(type);
    return BuildSystem.forId((String) typeFromMetadata.getTags().get("build"));
  }

  private String getValueOrDefault(
      String value, InitializrMetadata metadata, String valuesToSearchIn) {
    if (value != null) {
      return value;
    }
    switch (valuesToSearchIn) {
      case "types":
        return metadata.getTypes().getDefault().getId();
      case "language":
        return metadata.getLanguages().getDefault().getId();
      case "packaging":
        return metadata.getPackagings().getDefault().getId();
    }
    throw new InvalidProjectRequestException("No value initialized for " + valuesToSearchIn);
  }

  private Version getPlatformVersion(ProjectRequest request, InitializrMetadata metadata) {
    String versionText =
        request.getBootVersion() != null
            ? request.getBootVersion()
            : metadata.getBootVersions().getDefault().getId();
    Version version = Version.parse(versionText);
    return this.platformVersionTransformer.transform(version, metadata);
  }

  private List<Dependency> getResolvedDependencies(
      ProjectRequest request, Version platformVersion, InitializrMetadata metadata) {
    List<String> depIds = getValuesOrDefaults(request.getDependencies(), metadata, "dependencies");
    return (List)
        depIds.stream()
            .map(
                (it) -> {
                  Dependency dependency = metadata.getDependencies().get(it);
                  return dependency.resolve(platformVersion);
                })
            .collect(Collectors.toList());
  }
}
