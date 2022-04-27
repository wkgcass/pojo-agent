package io.vproxy.pojoagent.api.internal;

import io.vproxy.pojoagent.api.RequirePojoAgentException;

public class RequirePojoCallerException extends RequirePojoAgentException {
    public RequirePojoCallerException() {
        super("You must add @PojoCaller annotation on the caller, and enable pojo-agent");
    }
}
