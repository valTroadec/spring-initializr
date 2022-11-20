package com.test.initializr.request;

import com.test.initializr.generator.apim.ApimPolicies;
import io.spring.initializr.web.project.WebProjectRequest;
import lombok.Data;

@Data
public class CustomProjectRequest extends WebProjectRequest {

    private boolean apimPoliciesPublic;

    private boolean apimPoliciesPrivate;
}
