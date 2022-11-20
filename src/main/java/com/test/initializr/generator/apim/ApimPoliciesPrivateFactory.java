package com.test.initializr.generator.apim;

class ApimPoliciesPrivateFactory implements ApimPoliciesFactory {

    @Override
    public ApimPolicies createApimPolicies(String id) {
        if (ApimPoliciesPrivate.ID.equals(id)) {
            return new ApimPoliciesPrivate(false);
        }
        return null;
    }

    @Override
    public ApimPoliciesPrivate createApimPolicies(String id, boolean value) {
        if (ApimPoliciesPrivate.ID.equals(id)) {
            return new ApimPoliciesPrivate(value);
        }
        return null;
    }

}
