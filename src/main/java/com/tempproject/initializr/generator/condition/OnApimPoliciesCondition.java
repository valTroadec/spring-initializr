package com.tempproject.initializr.generator.condition;

import com.tempproject.initializr.generator.CustomMutableProjectDescription;
import com.tempproject.initializr.generator.apim.ApimPoliciesPrivate;
import com.tempproject.initializr.generator.apim.ApimPoliciesPublic;
import io.spring.initializr.generator.condition.ProjectGenerationCondition;
import io.spring.initializr.generator.project.ProjectDescription;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.MultiValueMap;

import java.util.Objects;

class OnApimPoliciesCondition extends ProjectGenerationCondition {
    OnApimPoliciesCondition() {
    }

    protected boolean matches(ProjectDescription projectDescription, ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        MultiValueMap<String, Object> attributes = annotatedTypeMetadata
                .getAllAnnotationAttributes(ConditionalOnApimPolicies.class.getName());

        assert attributes != null;
        String apimPoliciesId = (String) attributes.getFirst("value");

        switch (Objects.requireNonNull(apimPoliciesId)){
            case ApimPoliciesPublic.ID: return ((CustomMutableProjectDescription) projectDescription).getApimPoliciesConfigPublic().value();
            case ApimPoliciesPrivate.ID: return ((CustomMutableProjectDescription) projectDescription).getApimPoliciesConfigPrivate().value();
        }

        return false;
    }
}
