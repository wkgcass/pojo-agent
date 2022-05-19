package io.vproxy.pojoagent.agent;

import io.vproxy.pojoagent.api.PojoCaller;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.lang.instrument.IllegalClassFormatException;

public class CallerTransformer extends AbstractTransformer {
    private static final String callerAnnotationDesc = "L" + PojoCaller.class.getName().replace('.', '/') + ";";

    @Override
    protected boolean transform(ClassNode node) throws IllegalClassFormatException {
        if (node.methods == null) {
            return false;
        }

        boolean transformed = false;

        for (MethodNode meth : node.methods) {
            if (meth.visibleAnnotations == null)
                continue;

            for (AnnotationNode anno : meth.visibleAnnotations) {
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
        AbstractInsnNode[] insns = meth.instructions.toArray();

        boolean fieldIsSetTransformed = false;
        boolean unsetFieldTransformed = false;

        for (int i = 0; i < insns.length; ++i) {
            AbstractInsnNode insn = insns[i];
            if (!(insn instanceof MethodInsnNode))
                continue;

            MethodInsnNode methInsn = (MethodInsnNode) insn;
            if (!Utils.isFieldIsSetMethodInvocation(methInsn) && !Utils.isUnsetFieldMethodInvocation(methInsn))
                continue;

            if (i == 0)
                continue;

            AbstractInsnNode prev = insns[i - 1];
            if (!(prev instanceof MethodInsnNode) && !(prev instanceof FieldInsnNode))
                continue;

            String fieldName;
            String owner;
            if (prev instanceof MethodInsnNode) {
                MethodInsnNode prevMethInsn = (MethodInsnNode) prev;
                if (!Utils.isGetterInvocation(prevMethInsn))
                    continue;

                fieldName = Utils.fieldName(prevMethInsn.name);
                owner = prevMethInsn.owner;
            } else {
                FieldInsnNode prevFieldInsn = (FieldInsnNode) prev;
                if (prevFieldInsn.getOpcode() != Opcodes.GETFIELD)
                    continue;
                fieldName = prevFieldInsn.name;
                owner = prevFieldInsn.owner;
            }

            if (Utils.isFieldIsSetMethodInvocation(methInsn)) {
                String methodName = fieldName + Utils.fieldIsSetMethodSuffix;
                meth.instructions.set(insns[i - 1], new MethodInsnNode(Opcodes.INVOKEVIRTUAL, owner, methodName, "()Z"));
                meth.instructions.set(insns[i], new InsnNode(Opcodes.NOP));
                fieldIsSetTransformed = true;
            } else /* isUnsetFieldMethodInvocation */ {
                String methodName = fieldName + Utils.unsetFieldMethodSuffix;
                meth.instructions.set(insns[i - 1], new MethodInsnNode(Opcodes.INVOKEVIRTUAL, owner, methodName, "()V"));
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
