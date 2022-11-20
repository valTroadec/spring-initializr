package com.tempproject.initializr.config.projectgenerationconfiguration;

import com.tempproject.initializr.generator.contributor.ApimPoliciesContributor;
import com.tempproject.initializr.generator.apim.ApimPoliciesPrivate;
import com.tempproject.initializr.generator.apim.ApimPoliciesPublic;
import com.tempproject.initializr.generator.condition.ConditionalOnApimPolicies;
import org.springframework.context.annotation.Bean;

public class CustomApimPoliciesConfiguration {

    @Bean
    @ConditionalOnApimPolicies(ApimPoliciesPrivate.ID)
    public ApimPoliciesContributor apimPoliciesPrivate(){

        return new ApimPoliciesContributor("classpath:templates/apim-policies-private");
    }

    @Bean
    @ConditionalOnApimPolicies(ApimPoliciesPublic.ID)
    public ApimPoliciesContributor apimPoliciesPublic(){

        return new ApimPoliciesContributor("classpath:templates/apim-policies-public");
    }
}
