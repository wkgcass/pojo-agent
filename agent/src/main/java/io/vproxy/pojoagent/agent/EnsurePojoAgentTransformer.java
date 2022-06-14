package io.vproxy.pojoagent.agent;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

public class EnsurePojoAgentTransformer extends AbstractTransformer {
    @SuppressWarnings("UnnecessaryContinue")
    @Override
    protected boolean transform(ClassNode node) {
        String classname = node.name;
        if (!classname.equals("io/vproxy/pojoagent/api/internal/PojoAgentInternal")) {
            return false;
        }
        for (MethodNode meth : node.methods) {
            if (meth.name.equals("ensurePojoAgent") && meth.desc.equals("()V")) {
                InsnList insns = new InsnList();
                insns.add(new InsnNode(Opcodes.RETURN));
                meth.instructions = insns;
                continue;
            }
            if (meth.name.equals("getAgentVersion") && meth.desc.equals("()J")) {
                InsnList insns = new InsnList();
                insns.add(new LdcInsnNode(1_001_003L));
                insns.add(new InsnNode(Opcodes.LRETURN));
                meth.instructions = insns;
                continue;
            }
        }
        return true;
    }
}
