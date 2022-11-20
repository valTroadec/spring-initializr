package com.tempproject.initializr.generator.condition;

import org.springframework.context.annotation.Conditional;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Conditional({OnApimPoliciesCondition.class})
public @interface ConditionalOnApimPolicies {
    @AliasFor("id")
    String value() default "";

    @AliasFor("value")
    String id() default "";
}
