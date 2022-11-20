package com.tempproject.initializr.generator.contributor.api;

import com.tempproject.initializr.generator.CustomMutableProjectDescription;
import com.tempproject.initializr.generator.contributor.api.customizer.APIApplicationTypeCustomizer;
import com.tempproject.initializr.generator.contributor.api.customizer.APICompilationUnitCustomizer;
import com.tempproject.initializr.generator.contributor.api.customizer.APISourceCodeCustomizer;
import io.spring.initializr.generator.language.CompilationUnit;
import io.spring.initializr.generator.language.SourceCode;
import io.spring.initializr.generator.language.SourceCodeWriter;
import io.spring.initializr.generator.language.TypeDeclaration;
import io.spring.initializr.generator.project.contributor.ProjectContributor;
import io.spring.initializr.generator.spring.util.LambdaSafe;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.ObjectProvider;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class APIControllerSourceCodeProjectContributor<
        T extends TypeDeclaration, C extends CompilationUnit<T>, S extends SourceCode<T, C>>
    implements ProjectContributor {

  private final CustomMutableProjectDescription description;
  private final Supplier<S> sourceFactory;
  private final SourceCodeWriter<S> sourceWriter;
  private final ObjectProvider<APIApplicationTypeCustomizer<? extends TypeDeclaration>>
      apiTypeCustomizers;
  private final ObjectProvider<APICompilationUnitCustomizer<?, ?>> apiCompilationUnitCustomizers;
  private final ObjectProvider<APISourceCodeCustomizer<?, ?, ?>> apiSourceCodeCustomizers;

  public APIControllerSourceCodeProjectContributor(
      CustomMutableProjectDescription description,
      Supplier<S> sourceFactory,
      SourceCodeWriter<S> sourceWriter,
      ObjectProvider<APIApplicationTypeCustomizer<?>> apiTypeCustomizers,
      ObjectProvider<APICompilationUnitCustomizer<?, ?>> apiCompilationUnitCustomizers,
      ObjectProvider<APISourceCodeCustomizer<?, ?, ?>> apiSourceCodeCustomizers) {
    this.description = description;
    this.sourceFactory = sourceFactory;
    this.sourceWriter = sourceWriter;
    this.apiTypeCustomizers = apiTypeCustomizers;
    this.apiCompilationUnitCustomizers = apiCompilationUnitCustomizers;
    this.apiSourceCodeCustomizers = apiSourceCodeCustomizers;
  }

  @Override
  public void contribute(Path projectRoot) throws IOException {
    S sourceCode = (S) this.sourceFactory.get();
    String apiController = getApplicationNameFromAPiName(this.description.getApiName()) + "Controller";
    C compilationUnit =
        sourceCode.createCompilationUnit(
            this.description.getPackageName()
                + "."
                + this.description.getApiName().trim().replaceAll("[^a-zA-Z]", "").toLowerCase(), apiController
            );
    T apiApplicationType =
        compilationUnit.createTypeDeclaration(
                apiController);
    this.customizeAPIApplicationType(apiApplicationType);
    this.customizeAPICompilationUnit(compilationUnit);
    this.customizeAPISourceCode(sourceCode);
    this.sourceWriter.writeTo(
        this.description
            .getBuildSystem()
            .getMainSource(projectRoot, this.description.getLanguage()),
        sourceCode);
  }

  private String getApplicationNameFromAPiName(String apiName) {
    return WordUtils.capitalizeFully(apiName.trim(), new char[] {' ', '_', '-'})
        .replaceAll("[^a-zA-Z]", "");
  }

  private void customizeAPIApplicationType(T apiApplicationType) {
    List<APIApplicationTypeCustomizer<?>> customizers =
        (List) this.apiTypeCustomizers.orderedStream().collect(Collectors.toList());
    LambdaSafe.callbacks(
            APIApplicationTypeCustomizer.class, customizers, apiApplicationType, new Object[0])
        .invoke(
            (customizer) -> {
              customizer.customize(apiApplicationType);
            });
  }

  private void customizeAPICompilationUnit(C compilationUnit) {
    List<APICompilationUnitCustomizer<?, ?>> customizers =
        (List) this.apiCompilationUnitCustomizers.orderedStream().collect(Collectors.toList());
    LambdaSafe.callbacks(
            APICompilationUnitCustomizer.class, customizers, compilationUnit, new Object[0])
        .invoke(
            (customizer) -> {
              customizer.customize(compilationUnit);
            });
  }

  private void customizeAPISourceCode(S sourceCode) {
    List<APISourceCodeCustomizer<?, ?, ?>> customizers =
        (List) this.apiSourceCodeCustomizers.orderedStream().collect(Collectors.toList());
    LambdaSafe.callbacks(APISourceCodeCustomizer.class, customizers, sourceCode, new Object[0])
        .invoke(
            (customizer) -> {
              customizer.customize(sourceCode);
            });
  }

  @Override
  public int getOrder() {
    return ProjectContributor.super.getOrder();
  }
}
