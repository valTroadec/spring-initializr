package com.tempproject.initializr.generator.contributor.api.customizer;

import io.spring.initializr.generator.language.CompilationUnit;
import io.spring.initializr.generator.language.SourceCode;
import io.spring.initializr.generator.language.TypeDeclaration;
import org.springframework.core.Ordered;

@FunctionalInterface
public interface APISourceCodeCustomizer<T extends TypeDeclaration, C extends CompilationUnit<T>, S extends SourceCode<T, C>> extends Ordered {
    void customize(S var1);

    default int getOrder() {
        return 0;
    }
}
