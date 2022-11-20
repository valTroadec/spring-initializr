package com.test.initializr.generator.apim;

class ApimPoliciesPublicFactory implements ApimPoliciesFactory {

    @Override
    public ApimPolicies createApimPolicies(String id) {
        if (ApimPoliciesPublic.ID.equals(id)) {
            return new ApimPoliciesPublic(false);
        }
        return null;
    }

    @Override
    public ApimPoliciesPublic createApimPolicies(String id, boolean value) {
        if (ApimPoliciesPublic.ID.equals(id)) {
            return new ApimPoliciesPublic(value);
        }
        return null;
    }

}
