package io.vproxy.pojoagent.agent;

import io.vproxy.pojoagent.api.Pojo;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.lang.instrument.IllegalClassFormatException;
import java.util.ArrayList;

public class PojoTransformer extends AbstractTransformer {
    private static final String pojoAnnotationDesc = "L" + Pojo.class.getName().replace('.', '/') + ";";

    @Override
    protected boolean transform(ClassNode node) throws IllegalClassFormatException {
        if (node.visibleAnnotations == null) {
            return false;
        }

        boolean shouldHandle = false;
        for (var anno : node.visibleAnnotations) {
            if (anno.desc.equals(pojoAnnotationDesc)) {
                shouldHandle = true;
                break;
            }
        }
        if (!shouldHandle) {
            return false;
        }

        enhance(node);
        return true;
    }

    @SuppressWarnings("RedundantThrows")
    private void enhance(ClassNode node) throws IllegalClassFormatException {
        var className = node.name;
        var fields = new ArrayList<String>();
        for (var meth : node.methods) {
            if (!Utils.isSetter(meth)) {
                continue;
            }
            var fieldName = Utils.fieldName(meth.name) + Utils.fieldIsSetSuffix;
            fields.add(fieldName);

            InsnList insns = new InsnList();
            insns.add(new VarInsnNode(Opcodes.ALOAD, 0)); // this
            insns.add(new InsnNode(Opcodes.ICONST_1)); // true
            insns.add(new FieldInsnNode(Opcodes.PUTFIELD, className, fieldName, "Z")); // this.field = true;

            meth.instructions.insert(insns);

            Utils.log("setter enhanced: " + className + "." + meth.name + meth.desc);
        }

        for (var field : fields) {
            node.visitField(Opcodes.ACC_PUBLIC | Opcodes.ACC_SYNTHETIC,
                field, "Z", "Z", false);
            Utils.log("field added: " + className + "." + field);
        }
    }
}
