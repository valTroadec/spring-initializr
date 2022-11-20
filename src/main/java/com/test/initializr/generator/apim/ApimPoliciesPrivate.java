package com.test.initializr.generator.apim;

public final class ApimPoliciesPrivate implements ApimPolicies {

  public static final String ID = "private";

  private boolean value;

  public ApimPoliciesPrivate(boolean value) {
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
