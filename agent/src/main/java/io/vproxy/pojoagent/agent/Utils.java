package io.vproxy.pojoagent.agent;

import io.vproxy.pojoagent.api.PojoAgent;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

public class Utils {
    public static final String fieldIsSetSuffix = "$pojo_agent$isSet";
    public static final String pojoAgentHelperClass = PojoAgent.class.getName().replace('.', '/');
    public static final String fieldIsSetMethodName = "fieldIsSet";
    public static final String unsetFieldMethodName = "unsetField";

    private Utils() {
    }

    public static boolean isStatic(int access) {
        return (access & Opcodes.ACC_STATIC) == Opcodes.ACC_STATIC;
    }

    public static int parametersSize(String desc) {
        var chars = desc.toCharArray();
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

    public static void log(String msg) {
        System.err.println("pojo-agent: " + msg);
    }

    public static void log(Throwable t) {
        t.printStackTrace();
    }
}
