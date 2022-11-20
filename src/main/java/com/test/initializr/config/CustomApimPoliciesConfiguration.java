package com.test.initializr.config;

import com.test.initializr.contributor.ApimPoliciesContributor;
import com.test.initializr.generator.apim.ApimPoliciesPrivate;
import com.test.initializr.generator.apim.ApimPoliciesPublic;
import com.test.initializr.generator.condition.ConditionalOnApimPolicies;
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
