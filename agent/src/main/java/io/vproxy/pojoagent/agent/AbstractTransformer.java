package io.vproxy.pojoagent.agent;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.ClassNode;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Arrays;

@SuppressWarnings("PointlessBitwiseExpression")
public abstract class AbstractTransformer implements ClassFileTransformer {
    @Override
    public final byte[] transform(Module module, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        return transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
    }

    private static final int ASM4 = 4 << 16 | 0 << 8;
    private static final int ASM5 = 5 << 16 | 0 << 8;
    private static final int ASM6 = 6 << 16 | 0 << 8;
    private static final int ASM7 = 7 << 16 | 0 << 8;
    private static final int ASM8 = 8 << 16 | 0 << 8;

    private static final int[] ASMs = new int[]{
        ASM8, ASM7, ASM6, ASM5, ASM4,
    };
    private static int ASM_VER = 0;

    private static ClassNode newClassNode() throws IllegalClassFormatException {
        if (ASM_VER != 0) {
            return new ClassNode(ASM_VER);
        }
        for (var asm : ASMs) {
            try {
                var node = new ClassNode(asm);
                ASM_VER = asm;
                return node;
            } catch (Throwable ignore) {
            }
        }
        throw new IllegalClassFormatException("failed to create ClassNode with ASM8|7|6|5|4: "
            + Arrays.toString(ASMs));
    }

    @Override
    public final byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        ClassNode node;
        try {
            var reader = new ClassReader(classfileBuffer);
            node = newClassNode();
            reader.accept(node, 0);
        } catch (Throwable t) {
            Utils.log("failed to load class " + className + ", " +
                "please check whether " +
                "java.base/jdk.internal.org.objectweb.asm and " +
                "java.base/jdk.internal.org.objectweb.asm.tree " +
                "are correctly opened");
            Utils.log("sample: " +
                "--add-opens java.base/jdk.internal.org.objectweb.asm=ALL-UNNAMED " +
                "--add-opens java.base/jdk.internal.org.objectweb.asm.tree=ALL-UNNAMED");
            Utils.log(t);
            return classfileBuffer;
        }

        boolean transformed;
        try {
            transformed = transform(node);
        } catch (Throwable t) {
            Utils.log("failed to transform class " + className);
            Utils.log(t);
            return classfileBuffer;
        }
        if (transformed) {
            var writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
            node.accept(writer);
            return writer.toByteArray();
        } else {
            return classfileBuffer;
        }
    }

    abstract protected boolean transform(ClassNode node) throws IllegalClassFormatException;
}
