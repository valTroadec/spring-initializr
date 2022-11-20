package com.tempproject.initializr.generator.apim;

public final class ApimPoliciesPublic implements ApimPolicies {

  public static final String ID = "public";

  private boolean value;

  public ApimPoliciesPublic(boolean value) {
    this.setValue(value);
  }

  @Override
  public String id() {
    return ID;
  }

  @Override
  public boolean value() {
    return value;
  }

  @Override
  public void setValue(boolean valueInit) {
    this.value = valueInit;
  }

  @Override
  public String toString() {
    return id();
  }
}
