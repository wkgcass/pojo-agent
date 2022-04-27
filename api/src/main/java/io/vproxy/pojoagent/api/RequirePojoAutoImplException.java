package io.vproxy.pojoagent.api;

public class RequirePojoAutoImplException extends UnsupportedOperationException {
    public RequirePojoAutoImplException() {
        super("You must add @PojoAutoImpl annotation on the method, and enable pojo-agent");
    }
}
