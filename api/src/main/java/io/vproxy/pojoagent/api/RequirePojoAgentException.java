package io.vproxy.pojoagent.api;

public class RequirePojoAgentException extends UnsupportedOperationException {
    public RequirePojoAgentException() {
        super("Pojo-agent is required to auto-implement this function");
    }

    protected RequirePojoAgentException(String msg) {
        super(msg);
    }
}
