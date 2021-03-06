package io.vproxy.pojoagent.agent;

import java.lang.instrument.Instrumentation;

public class Premain {
    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new EnsurePojoAgentTransformer(), false);
        inst.addTransformer(new CallerTransformer(), false);
        inst.addTransformer(new PojoTransformer(), false);
    }
}
