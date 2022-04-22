package io.vproxy.pojoagent.api.internal;

import io.vproxy.pojoagent.api.RequirePojoAgentException;

public class RequirePojoCallerException extends RequirePojoAgentException {
    public RequirePojoCallerException() {
        super("you must add @PojoCaller annotation on the caller, and enable pojo-agent");
    }
}
