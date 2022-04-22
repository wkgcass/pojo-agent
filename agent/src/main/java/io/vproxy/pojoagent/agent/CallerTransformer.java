package io.vproxy.pojoagent.agent;

import io.vproxy.pojoagent.api.PojoCaller;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.InsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.lang.instrument.IllegalClassFormatException;

public class CallerTransformer extends AbstractTransformer {
    private static final String callerAnnotationDesc = "L" + PojoCaller.class.getName().replace('.', '/') + ";";

    @Override
    protected boolean transform(ClassNode node) throws IllegalClassFormatException {
        if (node.methods == null) {
            return false;
        }

        boolean transformed = false;

        for (var meth : node.methods) {
            if (meth.visibleAnnotations == null)
                continue;

            for (var anno : meth.visibleAnnotations) {
                if (anno.desc.equals(callerAnnotationDesc)) {
                    if (enhance(node.name, meth)) {
                        transformed = true;
                    }
                    break;
                }
            }
        }

        return transformed;
    }

    @SuppressWarnings("RedundantThrows")
    private boolean enhance(String classname, MethodNode meth) throws IllegalClassFormatException {
        if (meth.instructions == null) {
            return false;
        }
        var insns = meth.instructions.toArray();

        boolean fieldIsSetTransformed = false;
        boolean unsetFieldTransformed = false;

        for (int i = 0; i < insns.length; ++i) {
            var insn = insns[i];
            if (!(insn instanceof MethodInsnNode))
                continue;

            var methInsn = (MethodInsnNode) insn;
            if (!Utils.isFieldIsSetMethodInvocation(methInsn) && !Utils.isUnsetFieldMethodInvocation(methInsn))
                continue;

            if (i == 0)
                continue;

            var prev = insns[i - 1];
            if (!(prev instanceof MethodInsnNode))
                continue;

            var prevMethInsn = (MethodInsnNode) prev;
            if (!Utils.isGetterInvocation(prevMethInsn))
                continue;

            if (Utils.isFieldIsSetMethodInvocation(methInsn)) {
                var methodName = Utils.fieldName(prevMethInsn.name) + Utils.fieldIsSetMethodSuffix;
                meth.instructions.set(insns[i - 1], new MethodInsnNode(Opcodes.INVOKEVIRTUAL, prevMethInsn.owner, methodName, "()Z"));
                meth.instructions.set(insns[i], new InsnNode(Opcodes.NOP));
                fieldIsSetTransformed = true;
            } else /* isUnsetFieldMethodInvocation */ {
                var methodName = Utils.fieldName(prevMethInsn.name) + Utils.unsetFieldMethodSuffix;
                meth.instructions.set(insns[i - 1], new MethodInsnNode(Opcodes.INVOKEVIRTUAL, prevMethInsn.owner, methodName, "()V"));
                meth.instructions.set(insns[i], new InsnNode(Opcodes.NOP));
                unsetFieldTransformed = true;
            }
        }

        if (fieldIsSetTransformed && unsetFieldTransformed) {
            Utils.log("transformed fieldIsSet and unsetField invocation for " + classname + "." + meth.name + meth.desc);
        } else if (fieldIsSetTransformed) {
            Utils.log("transformed fieldIsSet invocation for " + classname + "." + meth.name + meth.desc);
        } else if (unsetFieldTransformed) {
            Utils.log("transformed unsetField invocation for " + classname + "." + meth.name + meth.desc);
        }

        return fieldIsSetTransformed || unsetFieldTransformed;
    }
}
