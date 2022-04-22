package io.vproxy.pojoagent.api.internal;

import io.vproxy.pojoagent.api.PojoAutoImpl;
import io.vproxy.pojoagent.api.RequirePojoAgentException;

public class PojoAgentInternal {
    private PojoAgentInternal() {
    }

    @PojoAutoImpl
    public static void ensurePojoAgent() {
        throw new RequirePojoAgentException();
    }
}
