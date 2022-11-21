package com.tempproject.initializr.generator.contributor.api.customizer;

import io.spring.initializr.generator.language.TypeDeclaration;
import org.springframework.core.Ordered;

@FunctionalInterface
public interface APIApplicationTypeCustomizer<T extends TypeDeclaration> extends Ordered {
    void customize(T var1);

    default int getOrder() {
        return 0;
    }
}
