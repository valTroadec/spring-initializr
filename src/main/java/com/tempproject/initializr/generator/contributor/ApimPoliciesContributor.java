package com.tempproject.initializr.generator.contributor;

import io.spring.initializr.generator.project.contributor.MultipleResourcesProjectContributor;

public class ApimPoliciesContributor extends MultipleResourcesProjectContributor {

    public ApimPoliciesContributor(String classpath) {
        super(classpath, (filename) -> true);

    }

}
