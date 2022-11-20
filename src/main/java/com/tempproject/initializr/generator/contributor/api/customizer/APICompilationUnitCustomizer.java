package com.tempproject.initializr.generator.contributor.api.customizer;

import io.spring.initializr.generator.language.CompilationUnit;
import io.spring.initializr.generator.language.TypeDeclaration;
import org.springframework.core.Ordered;

@FunctionalInterface
public interface APICompilationUnitCustomizer<T extends TypeDeclaration, C extends CompilationUnit<T>> extends Ordered {
    void customize(C var1);

    default int getOrder() {
        return 0;
    }
}
