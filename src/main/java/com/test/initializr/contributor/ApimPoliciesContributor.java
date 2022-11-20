package com.test.initializr.contributor;

import com.test.initializr.generator.condition.ConditionalOnApimPolicies;
import io.spring.initializr.generator.project.contributor.MultipleResourcesProjectContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

public class ApimPoliciesContributor extends MultipleResourcesProjectContributor {

    public ApimPoliciesContributor(String classpath) {
        super(classpath, (filename) -> true);

    }

}
