package io.vproxy.pojoagent.agent;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.InsnNode;

public class EnsurePojoAgentTransformer extends AbstractTransformer {
    @Override
    protected boolean transform(ClassNode node) {
        String classname = node.name;
        if (!classname.equals("io/vproxy/pojoagent/api/internal/PojoAgentInternal")) {
            return false;
        }
        for (var meth : node.methods) {
            if (meth.name.equals("ensurePojoAgent") && meth.desc.equals("()V")) {
                var insns = new InsnList();
                insns.add(new InsnNode(Opcodes.RETURN));
                meth.instructions = insns;
                break;
            }
        }
        return true;
    }
}
