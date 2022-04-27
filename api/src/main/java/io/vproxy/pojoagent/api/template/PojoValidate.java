package io.vproxy.pojoagent.api.template;

import io.vproxy.pojoagent.api.ValidationResult;

public interface PojoValidate {
    default ValidationResult validate() {
        return validate(0);
    }

    ValidationResult validate(int action);

    default void preValidate(int action) {
    }

    default ValidationResult postValidate(ValidationResult result) {
        return result;
    }
}
