package io.vproxy.pojoagent.agent;

import io.vproxy.pojoagent.api.PojoAgent;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

public class Utils {
    public static final String fieldIsSetBitSetPrefix = "bitSet$pojo_agent$isSet$";
    public static final String fieldIsSetMethodSuffix = "$pojo_agent$isSet";
    public static final String unsetFieldMethodSuffix = "$pojo_agent$unset";
    public static final String setFieldMethodSuffix = "$pojo_agent$set";
    public static final String pojoAgentHelperClass = PojoAgent.class.getName().replace('.', '/');
    public static final String fieldIsSetMethodName = "fieldIsSet";
    public static final String unsetFieldMethodName = "unsetField";
    public static final String setFieldMethodName = "setField";

    private Utils() {
    }

    public static boolean isStatic(int access) {
        return (access & Opcodes.ACC_STATIC) == Opcodes.ACC_STATIC;
    }

    public static boolean isPublic(int access) {
        return (access & Opcodes.ACC_PUBLIC) == Opcodes.ACC_PUBLIC;
    }

    public static boolean isPrivate(int access) {
        return (access & Opcodes.ACC_PRIVATE) == Opcodes.ACC_PRIVATE;
    }

    public static int parametersSize(String desc) {
        char[] chars = desc.toCharArray();
        int cnt = 0;
        int state = 0; // 0: init, 1: met 'L', 2: array
        for (int i = 1; i < chars.length; i++) {
            char c = chars[i];
            if (state == 0) {
                if (c == 'L') {
                    state = 1;
                } else if (c == '[') {
                    state = 2;
                } else if (c == ')') {
                    break;
                } else {
                    cnt += 1;
                }
            } else if (state == 1) {
                if (c == ';') {
                    state = 0;
                    cnt += 1;
                }
            } else {
                //noinspection StatementWithEmptyBody
                if (c == '[') {
                    // do nothing
                } else if (c == 'L') {
                    state = 1;
                } else {
                    cnt += 1;
                    state = 0;
                }
            }
        }
        return cnt;
    }

    public static boolean isSetter(MethodNode meth) {
        return !isStatic(meth.access) &&
            meth.name.startsWith("set") && !meth.name.equals("set") &&
            parametersSize(meth.desc) == 1 && meth.desc.endsWith(")V");
    }

    public static boolean isGetterInvocation(MethodInsnNode methInsn) {
        return methInsn.getOpcode() != Opcodes.INVOKESTATIC && (
            (methInsn.name.startsWith("get") && !methInsn.name.equals("get") && methInsn.desc.startsWith("()") && !methInsn.desc.endsWith(")V"))
                || (methInsn.name.startsWith("is") && !methInsn.name.equals("is") && methInsn.desc.equals("()Z"))
        );
    }

    // make sure method name is getXxx/isXxx or setXxx
    public static String fieldName(String methName) {
        String name;
        if (methName.startsWith("get") || methName.startsWith("set")) {
            name = methName.substring(3);
        } else {
            assert methName.startsWith("is");
            name = methName.substring(2);
        }

        if (name.length() == 1) {
            return name.toLowerCase();
        } else {
            return name.substring(0, 1).toLowerCase() + name.substring(1);
        }
    }

    public static boolean isFieldIsSetMethodInvocation(MethodInsnNode methInsn) {
        return methInsn.owner.equals(pojoAgentHelperClass) && methInsn.name.equals(fieldIsSetMethodName);
    }

    public static boolean isUnsetFieldMethodInvocation(MethodInsnNode methInsn) {
        return methInsn.owner.equals(pojoAgentHelperClass) && methInsn.name.equals(unsetFieldMethodName);
    }

    public static boolean isSetFieldMethodInvocation(MethodInsnNode methInsn) {
        return methInsn.owner.equals(pojoAgentHelperClass) && methInsn.name.equals(setFieldMethodName);
    }

    public static void warn(String msg) {
        log("WARN: " + msg);
    }

    public static void log(String msg) {
        System.err.println("pojo-agent: " + msg);
    }

    public static void log(Throwable t) {
        t.printStackTrace();
    }

    public static FieldNode findField(ClassNode node, String fieldName) {
        if (node.fields == null) {
            return null;
        }
        for (FieldNode f : node.fields) {
            if (f.name.equals(fieldName)) return f;
        }
        return null;
    }

    public static MethodNode findGetter(ClassNode node, String fieldName) {
        if (node.methods == null) {
            return null;
        }
        fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
        for (MethodNode m : node.methods) {
            if (m.desc.startsWith("()")) {
                if (m.name.equals("is" + fieldName)) return m;
            } else {
                if (m.name.equals("get" + fieldName)) return m;
            }
        }
        return null;
    }

    public static void invokeSingleSameParamMethod(String className, InsnList insns, MethodNode meth) {
        insns.add(new VarInsnNode(Opcodes.ALOAD, 0)); // this
        int opcode;
        if (meth.desc.startsWith("(I)")) {
            opcode = Opcodes.ILOAD;
        } else {
            opcode = Opcodes.ALOAD;
        }
        insns.add(new VarInsnNode(opcode, 1)); // another
        int invokeOp = Utils.isPrivate(meth.access) ? Opcodes.INVOKESPECIAL : Opcodes.INVOKEVIRTUAL;
        insns.add(new MethodInsnNode(invokeOp, className, meth.name, meth.desc));
    }
}
