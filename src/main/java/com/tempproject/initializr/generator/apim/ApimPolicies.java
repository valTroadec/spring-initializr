package com.tempproject.initializr.generator.apim;



/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.Objects;

/**
 * A build system that can be used by a generated project.
 *
 * @author Andy Wilkinson
 */
public interface ApimPolicies {

    /**
     * The id of the build system.
     * @return the id
     */
    String id();

    boolean value();

    void setValue(boolean valueInit);

    default String dialect() {
        return null;
    }

    static ApimPolicies forId(String id) {
        return forIdAndValue(id, false);
    }

    static ApimPolicies forIdAndValue(String id, boolean value) {
        return SpringFactoriesLoader.loadFactories(ApimPoliciesFactory.class, ApimPolicies.class.getClassLoader())
                .stream().map((factory) -> factory.createApimPolicies(id, value)).filter(Objects::nonNull).findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Unrecognized apim policies id '" + id + "' and value '" + value + "'"));
    }

}