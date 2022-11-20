package com.test.initializr.generator;

import com.test.initializr.request.ApimPoliciesConfig;
import io.spring.initializr.generator.project.ProjectDescription;


public interface CustomProjectDescription extends ProjectDescription {

    ApimPoliciesConfig getApimPoliciesConfig();
}
