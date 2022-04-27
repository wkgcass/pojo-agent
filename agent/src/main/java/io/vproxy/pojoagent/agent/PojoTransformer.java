package io.vproxy.pojoagent.agent;

import io.vproxy.pojoagent.api.*;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.lang.instrument.IllegalClassFormatException;
import java.util.ArrayList;

@SuppressWarnings("RedundantThrows")
public class PojoTransformer extends AbstractTransformer {
    private static final String pojoAnnotationDesc = "L" + Pojo.class.getName().replace('.', '/') + ";";
    private static final String pojoAutoImplAnnotationDesc = "L" + PojoAutoImpl.class.getName().replace('.', '/') + ";";
    private static final String mustExistAnnotationDesc = "L" + MustExist.class.getName().replace('.', '/') + ";";
    private static final String mustNotExistAnnotationDesc = "L" + MustNotExist.class.getName().replace('.', '/') + ";";
    private static final String mustNotNullAnnotationDesc = "L" + MustNotNull.class.getName().replace('.', '/') + ";";
    private static final String validationResultInternalName = ValidationResult.class.getName().replace('.', '/');
    private static final String validationResultDesc = "L" + validationResultInternalName + ";";

    @Override
    protected boolean transform(ClassNode node) throws IllegalClassFormatException {
        if (node.visibleAnnotations == null) {
            return false;
        }

        boolean shouldHandle = false;
        for (AnnotationNode anno : node.visibleAnnotations) {
            if (anno.desc.equals(pojoAnnotationDesc)) {
                shouldHandle = true;
                break;
            }
        }
        if (!shouldHandle) {
            return false;
        }

        ArrayList<ArrayList<String>> bitsetFields = new ArrayList<>();
        return enhanceSetters(node, bitsetFields) | enhanceAutoImpl(node, bitsetFields);
    }

    private boolean enhanceSetters(ClassNode node, ArrayList<ArrayList<String>> bitsetFields) throws IllegalClassFormatException {
        String className = node.name;
        if (node.methods == null) {
            return false;
        }
        for (MethodNode meth : node.methods) {
            if (!Utils.isSetter(meth)) {
                continue;
            }
            ArrayList<String> fields;
            if (bitsetFields.isEmpty() || bitsetFields.get(bitsetFields.size() - 1).size() == 32) {
                fields = new ArrayList<>();
                bitsetFields.add(fields);
            } else {
                fields = bitsetFields.get(bitsetFields.size() - 1);
            }
            fields.add(Utils.fieldName(meth.name));

            String bitSetFieldName = Utils.fieldIsSetBitSetPrefix + bitsetFields.size();
            int shift = fields.size() - 1;

            InsnList insns = new InsnList();
            // this.bitsetN = this.bitsetN | (1 << x);

            insns.add(new VarInsnNode(Opcodes.ALOAD, 0)); // this
            // {
            insns.add(new VarInsnNode(Opcodes.ALOAD, 0)); // this
            insns.add(new FieldInsnNode(Opcodes.GETFIELD, className, bitSetFieldName, "I")); // this.bitsetN
            insns.add(new InsnNode(Opcodes.ICONST_1)); // 1
            insns.add(new IntInsnNode(Opcodes.BIPUSH, shift)); // x
            insns.add(new InsnNode(Opcodes.ISHL)); // 1 << x
            insns.add(new InsnNode(Opcodes.IOR));
            // }
            insns.add(new FieldInsnNode(Opcodes.PUTFIELD, className, bitSetFieldName, "I")); // this.bitsetN = ...;

            meth.instructions.insert(insns);

            Utils.log("setter enhanced: " + className + "." + meth.name + meth.desc);
        }

        for (int i = 0; i < bitsetFields.size(); ++i) {
            String bitSetFieldName = Utils.fieldIsSetBitSetPrefix + (i + 1);
            node.visitField(Opcodes.ACC_PRIVATE | Opcodes.ACC_SYNTHETIC,
                bitSetFieldName, "I", "I", 0);
        }
        for (int i = 0; i < bitsetFields.size(); ++i) {
            String bitSetFieldName = Utils.fieldIsSetBitSetPrefix + (i + 1);
            ArrayList<String> fields = bitsetFields.get(i);
            for (int shift = 0; shift < fields.size(); ++shift) {
                String field = fields.get(shift);
                // fieldIsSet
                {
                    MethodNode meth = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_SYNTHETIC,
                        field + Utils.fieldIsSetMethodSuffix, "()Z", "()Z", null);
                    // return (this.bitsetN | (1 << x)) == (1 << x)
                    InsnList insns = new InsnList();

                    insns.add(new InsnNode(Opcodes.ICONST_1)); // 1
                    insns.add(new IntInsnNode(Opcodes.BIPUSH, shift)); // x
                    insns.add(new InsnNode(Opcodes.ISHL)); // 1 << x
                    insns.add(new VarInsnNode(Opcodes.ISTORE, 1)); // var a1 = 1 << x;

                    insns.add(new VarInsnNode(Opcodes.ALOAD, 0)); // this
                    insns.add(new FieldInsnNode(Opcodes.GETFIELD, className, bitSetFieldName, "I")); // this.bitsetN
                    insns.add(new VarInsnNode(Opcodes.ILOAD, 1)); // a1
                    insns.add(new InsnNode(Opcodes.IAND)); // this.bitsetN & a1

                    insns.add(new VarInsnNode(Opcodes.ILOAD, 1)); // a1

                    LabelNode label = new LabelNode();
                    insns.add(new JumpInsnNode(Opcodes.IF_ICMPEQ, label)); // if (this.bitsetN & a1) == a1
                    insns.add(new InsnNode(Opcodes.ICONST_0)); // false
                    insns.add(new InsnNode(Opcodes.IRETURN)); // return false;

                    insns.add(label);
                    insns.add(new InsnNode(Opcodes.ICONST_1)); // true
                    insns.add(new InsnNode(Opcodes.IRETURN)); // return true;

                    meth.instructions = insns;
                    node.methods.add(meth);
                }
                // unsetField
                {
                    MethodNode meth = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_SYNTHETIC,
                        field + Utils.unsetFieldMethodSuffix, "()V", "()V", null);
                    // this.bitsetN = this.bitsetN & ~(1 << x)
                    InsnList insns = new InsnList();

                    insns.add(new VarInsnNode(Opcodes.ALOAD, 0)); // this
                    // {
                    insns.add(new InsnNode(Opcodes.ICONST_1)); // 1
                    insns.add(new IntInsnNode(Opcodes.BIPUSH, shift)); // x
                    insns.add(new InsnNode(Opcodes.ISHL)); // 1 << x
                    insns.add(new InsnNode(Opcodes.ICONST_M1));
                    insns.add(new InsnNode(Opcodes.IXOR)); // ~(1 << x)

                    insns.add(new VarInsnNode(Opcodes.ALOAD, 0)); // this
                    insns.add(new FieldInsnNode(Opcodes.GETFIELD, className, bitSetFieldName, "I")); // this.bitsetN
                    insns.add(new InsnNode(Opcodes.IAND)); // this.bitsetN & ~(1 << x)
                    // }
                    insns.add(new FieldInsnNode(Opcodes.PUTFIELD, className, bitSetFieldName, "I")); // this.bitsetN = ...
                    insns.add(new InsnNode(Opcodes.RETURN)); // return;

                    meth.instructions = insns;
                    node.methods.add(meth);
                }
            }
        }

        return !bitsetFields.isEmpty();
    }

    private boolean enhanceAutoImpl(ClassNode node, ArrayList<ArrayList<String>> bitsetFields) throws IllegalClassFormatException {
        String className = node.name;
        if (node.methods == null) {
            return false;
        }
        boolean ret = false;
        for (MethodNode meth : node.methods) {
            if (meth.visibleAnnotations == null) {
                continue;
            }
            boolean exists = false;
            for (AnnotationNode anno : meth.visibleAnnotations) {
                if (anno.desc == null) {
                    continue;
                }
                if (anno.desc.equals(pojoAutoImplAnnotationDesc)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                continue;
            }
            if (!Utils.isStatic(meth.access) && Utils.isPublic(meth.access)
                && meth.name.equals("updateFrom")
                && meth.desc.equals("(L" + className + ";)V")) {
                enhanceUpdateFrom(node, meth, bitsetFields);
                ret = true;
            } else if (!Utils.isStatic(meth.access) && Utils.isPublic(meth.access)
                && meth.name.equals("validate")
                && meth.desc.equals("(I)" + validationResultDesc)) {
                enhanceValidate(node, meth);
                ret = true;
            }
        }
        return ret;
    }

    private void enhanceUpdateFrom(ClassNode node, MethodNode meth, ArrayList<ArrayList<String>> bitsetFields) {
        String className = node.name;
        InsnList insns = new InsnList();

        // find and invoke pre method
        for (MethodNode pre : node.methods) {
            if (pre.name.equals("preUpdateFrom") && pre.desc.equals(meth.desc)) {
                Utils.log("preUpdateFrom found for " + className);
                Utils.invokeSingleSameParamMethod(className, insns, pre);
            }
        }

        // set fields
        for (ArrayList<String> fields : bitsetFields) {
            for (String fieldName : fields) {
                FieldNode field = Utils.findField(node, fieldName);
                MethodNode getter = Utils.findGetter(node, fieldName);
                if (field == null && getter == null) {
                    continue;
                }
                insns.add(new VarInsnNode(Opcodes.ALOAD, 1)); // another
                insns.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, className, fieldName + Utils.fieldIsSetMethodSuffix, "()Z")); // another.xxIsSet
                insns.add(new InsnNode(Opcodes.ICONST_0)); // false
                LabelNode label = new LabelNode();
                insns.add(new JumpInsnNode(Opcodes.IF_ICMPEQ, label));
                // : if (another.xxIsSet != false) {
                insns.add(new VarInsnNode(Opcodes.ALOAD, 0)); // this
                // {
                String setterDesc;
                if (field != null) {
                    insns.add(new VarInsnNode(Opcodes.ALOAD, 1)); // another
                    insns.add(new FieldInsnNode(Opcodes.GETFIELD, className, field.name, field.desc)); // another.xx
                    setterDesc = "(" + field.desc + ")V";
                } else {
                    // assert getter != null;
                    insns.add(new VarInsnNode(Opcodes.ALOAD, 1)); // another
                    insns.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, className, getter.name, getter.desc)); // another.getXx()
                    setterDesc = "(" + getter.desc.substring(2) + ")V";
                }
                // }
                insns.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, className,
                    "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1),
                    setterDesc)); // this.setXx(...);
                // }
                insns.add(label);
            }
        }

        // find and invoke post method
        for (MethodNode post : node.methods) {
            if (post.name.equals("postUpdateFrom") && post.desc.equals(meth.desc)) {
                Utils.log("postUpdateFrom found for " + className);
                Utils.invokeSingleSameParamMethod(className, insns, post);
                break;
            }
        }

        insns.add(new InsnNode(Opcodes.RETURN));
        Utils.log("method enhanced: " + className + "." + meth.name + meth.desc);
        meth.instructions = insns;
    }

    private void enhanceValidate(ClassNode node, MethodNode meth) {
        String className = node.name;
        InsnList insns = new InsnList();

        // find and invoke pre method
        for (MethodNode pre : node.methods) {
            if (pre.name.equals("preValidate") && pre.desc.equals("(I)V")) {
                Utils.log("preValidate found for " + className);
                Utils.invokeSingleSameParamMethod(className, insns, pre);
            }
        }

        // var result = new ValidationResult();
        insns.add(new TypeInsnNode(Opcodes.NEW, validationResultInternalName));
        insns.add(new InsnNode(Opcodes.DUP));
        insns.add(new VarInsnNode(Opcodes.ILOAD, 1)); // action
        insns.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, validationResultInternalName, "<init>", "(I)V"));
        insns.add(new VarInsnNode(Opcodes.ASTORE, 2));

        // validate fields
        for (FieldNode field : node.fields) {
            if (Utils.isStatic(field.access)) {
                // will not handle static fields
                continue;
            }
            if (field.visibleAnnotations == null) {
                continue;
            }
            for (AnnotationNode anno : field.visibleAnnotations) {
                if (anno.desc.equals(mustExistAnnotationDesc)) {
                    enhanceValidateMustExist(className, field, anno, insns);
                } else if (anno.desc.equals(mustNotExistAnnotationDesc)) {
                    enhanceValidateMustNotExist(className, field, anno, insns);
                } else if (anno.desc.equals(mustNotNullAnnotationDesc)) {
                    enhanceValidateMustNotNull(className, field, anno, insns);
                }
            }
        }

        // find and invoke post method
        boolean postFound = false;
        for (MethodNode post : node.methods) {
            if (post.name.equals("postValidate") && post.desc.equals("(" + validationResultDesc + ")" + validationResultDesc)) {
                Utils.log("postValidate found for " + className);
                insns.add(new VarInsnNode(Opcodes.ALOAD, 0)); // this
                insns.add(new VarInsnNode(Opcodes.ALOAD, 2)); // result
                int invokeOp = Utils.isPrivate(meth.access) ? Opcodes.INVOKESPECIAL : Opcodes.INVOKEVIRTUAL;
                insns.add(new MethodInsnNode(invokeOp, className, "postValidate", "(" + validationResultDesc + ")" + validationResultDesc));
                insns.add(new InsnNode(Opcodes.ARETURN));
                postFound = true;
                break;
            }
        }

        Utils.log("method enhanced: " + className + "." + meth.name + meth.desc);
        if (!postFound) {
            insns.add(new VarInsnNode(Opcodes.ALOAD, 2));
            insns.add(new InsnNode(Opcodes.ARETURN));
        }
        meth.instructions = insns;
    }

    private static int getAnnoValue(AnnotationNode anno) {
        if (anno.values == null) {
            return 0;
        }
        for (int i = 0; i < anno.values.size(); i += 2) {
            String name = (String) anno.values.get(i);
            Object value = anno.values.get(i + 1);
            if (name.equals("value")) {
                return (Integer) value;
            }
        }
        return 0;
    }

    private void enhanceValidateMustExist(String className, FieldNode field, AnnotationNode anno, InsnList insns) {
        insns.add(new VarInsnNode(Opcodes.ALOAD, 0)); // this
        insns.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, className, field.name + Utils.fieldIsSetMethodSuffix, "()Z")); // this.xxIsSet()
        insns.add(new InsnNode(Opcodes.ICONST_1)); // true
        LabelNode label = new LabelNode();
        insns.add(new JumpInsnNode(Opcodes.IF_ICMPEQ, label));
        // if (this.xxIsSet != true) {
        insns.add(new VarInsnNode(Opcodes.ALOAD, 2)); // result
        insns.add(new LdcInsnNode(field.name)); // name
        insns.add(new VarInsnNode(Opcodes.ILOAD, 1)); // action
        insns.add(new LdcInsnNode(getAnnoValue(anno))); // mask
        // result.addMissingIf(name, action, mask);
        insns.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, validationResultInternalName, "addMissingIf", "(Ljava/lang/String;II)V"));
        // }
        insns.add(label);
    }

    private void enhanceValidateMustNotExist(String className, FieldNode field, AnnotationNode anno, InsnList insns) {
        insns.add(new VarInsnNode(Opcodes.ALOAD, 0)); // this
        insns.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, className, field.name + Utils.fieldIsSetMethodSuffix, "()Z")); // this.xxIsSet()
        insns.add(new InsnNode(Opcodes.ICONST_0)); // false
        LabelNode label = new LabelNode();
        insns.add(new JumpInsnNode(Opcodes.IF_ICMPEQ, label));
        // if (this.xxIsSet != false) {
        insns.add(new VarInsnNode(Opcodes.ALOAD, 2)); // result
        insns.add(new LdcInsnNode(field.name)); // name
        insns.add(new VarInsnNode(Opcodes.ILOAD, 1)); // action
        insns.add(new LdcInsnNode(getAnnoValue(anno))); // mask
        // result.addRedundantIf(name, action, mask);
        insns.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, validationResultInternalName, "addRedundantIf", "(Ljava/lang/String;II)V"));
        // }
        insns.add(label);
    }

    private void enhanceValidateMustNotNull(String className, FieldNode field, AnnotationNode anno, InsnList insns) {
        if (field.desc.length() == 1) {
            Utils.warn("field " + className + "." + field + " cannot be null, ignoring @MustNotNull annotation on this field");
            return;
        }
        insns.add(new VarInsnNode(Opcodes.ALOAD, 0)); // this
        insns.add(new FieldInsnNode(Opcodes.GETFIELD, className, field.name, field.desc)); // this.xx
        LabelNode label0 = new LabelNode();
        insns.add(new JumpInsnNode(Opcodes.IFNULL, label0));
        LabelNode label1 = new LabelNode();
        insns.add(new JumpInsnNode(Opcodes.GOTO, label1));
        insns.add(label0);
        // if (this.xx == null) {
        insns.add(new VarInsnNode(Opcodes.ALOAD, 2)); // result
        insns.add(new LdcInsnNode(field.name)); // name
        insns.add(new VarInsnNode(Opcodes.ILOAD, 1)); // action
        insns.add(new LdcInsnNode(getAnnoValue(anno))); // mask
        // result.addNullIf(name, action, mask);
        insns.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, validationResultInternalName, "addNullIf", "(Ljava/lang/String;II)V"));
        // }
        insns.add(label1);
    }
}
