package kr.debop4j.core.reflect;

import com.google.common.collect.Lists;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.StringTool;
import lombok.Getter;
import org.objectweb.asm.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;

/**
 * 객체의 특정 메소드를 동적으로 호출할 수 있도록 한 메소드 접근자입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 1. 21
 */
abstract public class MethodAccess {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MethodAccess.class);
    private static final boolean isDebugEnabled = log.isDebugEnabled();

    @Getter
    private String[] methodNames;
    @Getter
    private Class[][] parameterTypes;

    abstract public Object invoke(Object instance, int methodIndex, Object... args);

    public Object invoke(Object instance, String methodName, Object... args) {
        if (log.isDebugEnabled())
            log.debug("객체[{}]의 메소드[{}]를 실행합니다. args=[{}]", instance, methodName, StringTool.listToString(args));
        return invoke(instance, getIndex(methodName), args);
    }

    public int getIndex(String methodName) {
        for (int i = 0, size = methodNames.length; i < size; i++) {
            if (methodNames[i].equals(methodName))
                return i;
        }
        throw new IllegalArgumentException("Unable to find public method: " + methodName);
    }

    public int getIndex(String methodName, Class... paramTypes) {
        for (int i = 0, size = methodNames.length; i < size; i++) {
            if (methodNames[i].equals(methodName) && Arrays.equals(paramTypes, parameterTypes[i]))
                return i;
        }
        throw new IllegalArgumentException("Unable to find public method: " + methodName + " " + Arrays.toString(parameterTypes));
    }

    /**
     * 지정한 수형의 메소드에 동적으로 접근하기 위한 MethodAccess를 빌드합니다.
     */
    static public MethodAccess get(Class type) {
        Guard.shouldNotBeNull(type, "type");

        List<Method> methods = Lists.newArrayList();
        Class nextClass = type;
        while (nextClass != Object.class) {
            Method[] declaredMethods = nextClass.getDeclaredMethods();
            for (Method method : declaredMethods) {
                int modifiers = method.getModifiers();
                if (Modifier.isStatic(modifiers)) continue;
                if (Modifier.isPrivate(modifiers)) continue;
                methods.add(method);
            }
            nextClass = nextClass.getSuperclass();
        }

        Class[][] parameterTypes = new Class[methods.size()][];
        String[] methodNames = new String[methods.size()];
        for (int i = 0, size = methodNames.length; i < size; i++) {
            Method method = methods.get(i);
            methodNames[i] = method.getName();
            parameterTypes[i] = method.getParameterTypes();
        }

        String className = type.getName();
        String accessClassName = className + "MethodAccess";
        if (accessClassName.startsWith("java."))
            accessClassName = ReflectConsts.BASE_PACKAGE + "." + accessClassName;
        Class accessClass = null;

        AccessClassLoader loader = AccessClassLoader.get(type);
        synchronized (loader) {
            try {
                accessClass = loader.loadClass(accessClassName);
            } catch (ClassNotFoundException ignored) {
                String accessClassNameInternal = accessClassName.replace('.', '/');
                String classNameInternal = className.replace('.', '/');

                ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                MethodVisitor mv;
                cw.visit(V1_1, ACC_PUBLIC + ACC_SUPER, accessClassNameInternal, null, ReflectConsts.METHOD_ACCESS_PATH,
                         null);
                {
                    mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
                    mv.visitCode();
                    mv.visitVarInsn(ALOAD, 0);
                    mv.visitMethodInsn(INVOKESPECIAL, ReflectConsts.METHOD_ACCESS_PATH, "<init>", "()V");
                    mv.visitInsn(RETURN);
                    mv.visitMaxs(0, 0);
                    mv.visitEnd();
                }
                {
                    mv = cw.visitMethod(ACC_PUBLIC + ACC_VARARGS, "invoke",
                                        "(Ljava/lang/Object;I[Ljava/lang/Object;)Ljava/lang/Object;", null, null);
                    mv.visitCode();

                    if (!methods.isEmpty()) {
                        mv.visitVarInsn(ALOAD, 1);
                        mv.visitTypeInsn(CHECKCAST, classNameInternal);
                        mv.visitVarInsn(ASTORE, 4);

                        mv.visitVarInsn(ILOAD, 2);
                        Label[] labels = new Label[methods.size()];
                        for (int i = 0, n = labels.length; i < n; i++)
                            labels[i] = new Label();
                        Label defaultLabel = new Label();
                        mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);

                        StringBuilder buffer = new StringBuilder(128);
                        for (int i = 0, n = labels.length; i < n; i++) {
                            mv.visitLabel(labels[i]);
                            if (i == 0)
                                mv.visitFrame(Opcodes.F_APPEND, 1, new Object[]{classNameInternal}, 0, null);
                            else
                                mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                            mv.visitVarInsn(ALOAD, 4);

                            buffer.setLength(0);
                            buffer.append('(');

                            Method method = methods.get(i);
                            Class[] paramTypes = method.getParameterTypes();
                            for (int paramIndex = 0; paramIndex < paramTypes.length; paramIndex++) {
                                mv.visitVarInsn(ALOAD, 3);
                                mv.visitIntInsn(BIPUSH, paramIndex);
                                mv.visitInsn(AALOAD);
                                Type paramType = Type.getType(paramTypes[paramIndex]);
                                switch (paramType.getSort()) {
                                    case Type.BOOLEAN:
                                        mv.visitTypeInsn(CHECKCAST, "java/lang/Boolean");
                                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z");
                                        break;
                                    case Type.BYTE:
                                        mv.visitTypeInsn(CHECKCAST, "java/lang/Byte");
                                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Byte", "byteValue", "()B");
                                        break;
                                    case Type.CHAR:
                                        mv.visitTypeInsn(CHECKCAST, "java/lang/Character");
                                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Character", "charValue", "()C");
                                        break;
                                    case Type.SHORT:
                                        mv.visitTypeInsn(CHECKCAST, "java/lang/Short");
                                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Short", "shortValue", "()S");
                                        break;
                                    case Type.INT:
                                        mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
                                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I");
                                        break;
                                    case Type.FLOAT:
                                        mv.visitTypeInsn(CHECKCAST, "java/lang/Float");
                                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F");
                                        break;
                                    case Type.LONG:
                                        mv.visitTypeInsn(CHECKCAST, "java/lang/Long");
                                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Long", "longValue", "()J");
                                        break;
                                    case Type.DOUBLE:
                                        mv.visitTypeInsn(CHECKCAST, "java/lang/Double");
                                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D");
                                        break;
                                    case Type.ARRAY:
                                        mv.visitTypeInsn(CHECKCAST, paramType.getDescriptor());
                                        break;
                                    case Type.OBJECT:
                                        mv.visitTypeInsn(CHECKCAST, paramType.getInternalName());
                                        break;
                                }
                                buffer.append(paramType.getDescriptor());
                            }

                            buffer.append(')');
                            buffer.append(Type.getDescriptor(method.getReturnType()));
                            mv.visitMethodInsn(INVOKEVIRTUAL, classNameInternal, method.getName(), buffer.toString());

                            switch (Type.getType(method.getReturnType()).getSort()) {
                                case Type.VOID:
                                    mv.visitInsn(ACONST_NULL);
                                    break;
                                case Type.BOOLEAN:
                                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
                                    break;
                                case Type.BYTE:
                                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;");
                                    break;
                                case Type.CHAR:
                                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;");
                                    break;
                                case Type.SHORT:
                                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;");
                                    break;
                                case Type.INT:
                                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
                                    break;
                                case Type.FLOAT:
                                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;");
                                    break;
                                case Type.LONG:
                                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;");
                                    break;
                                case Type.DOUBLE:
                                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
                                    break;
                            }

                            mv.visitInsn(ARETURN);
                        }

                        mv.visitLabel(defaultLabel);
                        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                    }
                    mv.visitTypeInsn(NEW, "java/lang/IllegalArgumentException");
                    mv.visitInsn(DUP);
                    mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
                    mv.visitInsn(DUP);
                    mv.visitLdcInsn("Method not found: ");
                    mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V");
                    mv.visitVarInsn(ILOAD, 2);
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;");
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
                    mv.visitMethodInsn(INVOKESPECIAL, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V");
                    mv.visitInsn(ATHROW);
                    mv.visitMaxs(0, 0);
                    mv.visitEnd();
                }
                cw.visitEnd();
                byte[] data = cw.toByteArray();
                accessClass = loader.defineClass(accessClassName, data);
            }
        }

        try {
            MethodAccess access = (MethodAccess) accessClass.newInstance();
            access.methodNames = methodNames;
            access.parameterTypes = parameterTypes;

            return access;
        } catch (Exception ex) {
            throw new RuntimeException("Error constructing method access class=[" + accessClassName + "]", ex);
        }
    }
}
