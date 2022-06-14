package io.vproxy.pojoagent.agent;

import jdk.internal.org.objectweb.asm.ClassWriter;

public class SimpleClassWriter extends ClassWriter {
    public SimpleClassWriter() {
        super(COMPUTE_FRAMES | COMPUTE_MAXS);
    }

    @Override
    protected String getCommonSuperClass(String type1, String type2) {
        if (type1.equals(type2)) return type1;
        if (type1.equals("java/lang/Object") || type2.equals("java/lang/Object")) return "java/lang/Object";
        Utils.log("failed to get common super class for " + type1 + " and " + type2 + ", using java/lang/Object instead");
        return "java/lang/Object";
    }
}
