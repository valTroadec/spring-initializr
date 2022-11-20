package com.tempproject.initializr.generator.apim;


public interface ApimPoliciesFactory {


    ApimPolicies createApimPolicies(String id);

    default ApimPolicies createApimPolicies(String id, boolean value) {
        return createApimPolicies(id, value);
    }

}