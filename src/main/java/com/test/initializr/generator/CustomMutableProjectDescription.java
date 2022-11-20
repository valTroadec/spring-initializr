package com.test.initializr.generator;

import com.test.initializr.generator.apim.ApimPolicies;
import io.spring.initializr.generator.project.MutableProjectDescription;
import lombok.Data;

@Data
public class CustomMutableProjectDescription extends MutableProjectDescription {

    private ApimPolicies apimPoliciesConfigPublic;
    private ApimPolicies apimPoliciesConfigPrivate;

    public CustomMutableProjectDescription() {
    }
}
