package com.tempproject.initializr.generator;

import com.tempproject.initializr.generator.apim.ApimPolicies;
import io.spring.initializr.generator.project.MutableProjectDescription;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomMutableProjectDescription extends MutableProjectDescription {

  private ApimPolicies apimPoliciesConfigPublic;
  private ApimPolicies apimPoliciesConfigPrivate;
}
