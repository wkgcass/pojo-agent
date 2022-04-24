package io.vproxy.pojoagent.agent;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.InsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

public class EnsurePojoAgentTransformer extends AbstractTransformer {
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
                break;
            }
        }
        return true;
    }
}
