package kr.debop4j.core.reflect;

import com.google.common.collect.Lists;
import kr.debop4j.core.Guard;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;

/**
 * 객체의 속성에 접근하기 위한 Accessor 입니다. (속성에 대해 get, set을 수행할 수 있습니다)
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 1. 21
 */
abstract public class FieldAccess {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FieldAccess.class);
    private static final boolean isDebugEnabled = log.isDebugEnabled();

    private String[] fieldNames;

    public int getIndex(String fieldName) {
        for (int i = 0, n = fieldNames.length; i < n; i++)
            if (fieldNames[i].equals(fieldName)) return i;
        throw new IllegalArgumentException("Unable to find public field: " + fieldName);
    }

    public void set(Object instance, String fieldName, Object value) {
        if (log.isDebugEnabled())
            log.debug("객체[{}]의 속성[{}]에 값[{}] 을 설정합니다.", instance, fieldName, value);
        set(instance, getIndex(fieldName), value);
    }

    public Object get(Object instance, String fieldName) {
        if (log.isDebugEnabled())
            log.debug("객체[{}]의 속성[{}] 값을 조회합니다.", instance, fieldName);
        return get(instance, getIndex(fieldName));
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    abstract public void set(Object instance, int fieldIndex, Object value);

    abstract public void setBoolean(Object instance, int fieldIndex, boolean value);

    abstract public void setByte(Object instance, int fieldIndex, byte value);

    abstract public void setShort(Object instance, int fieldIndex, short value);

    abstract public void setInt(Object instance, int fieldIndex, int value);

    abstract public void setLong(Object instance, int fieldIndex, long value);

    abstract public void setDouble(Object instance, int fieldIndex, double value);

    abstract public void setFloat(Object instance, int fieldIndex, float value);

    abstract public void setChar(Object instance, int fieldIndex, char value);

    abstract public Object get(Object instance, int fieldIndex);

    abstract public String getString(Object instance, int fieldIndex);

    abstract public char getChar(Object instance, int fieldIndex);

    abstract public boolean getBoolean(Object instance, int fieldIndex);

    abstract public byte getByte(Object instance, int fieldIndex);

    abstract public short getShort(Object instance, int fieldIndex);

    abstract public int getInt(Object instance, int fieldIndex);

    abstract public long getLong(Object instance, int fieldIndex);

    abstract public double getDouble(Object instance, int fieldIndex);

    abstract public float getFloat(Object instance, int fieldIndex);

    static public FieldAccess get(Class type) {
        Guard.shouldNotBeNull(type, "type");

        List<Field> fields = Lists.newArrayList();
        Class nextClass = type;
        while (nextClass != Object.class) {
            Field[] declaredFields = nextClass.getDeclaredFields();
            for (Field field : declaredFields) {
                int modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers)) continue;
                if (Modifier.isPrivate(modifiers)) continue;
                fields.add(field);
            }
            nextClass = nextClass.getSuperclass();
        }

        String[] fieldNames = new String[fields.size()];
        for (int i = 0, n = fieldNames.length; i < n; i++)
            fieldNames[i] = fields.get(i).getName();

        String className = type.getName();
        String accessClassName = className + "FieldAccess";
        if (accessClassName.startsWith("java.")) accessClassName = ReflectConsts.BASE_PACKAGE + "." + accessClassName;
        Class accessClass = null;

        AccessClassLoader loader = AccessClassLoader.get(type);
        synchronized (loader) {
            try {
                accessClass = loader.loadClass(accessClassName);
            } catch (ClassNotFoundException ignored) {
                String accessClassNameInternal = accessClassName.replace('.', '/');
                String classNameInternal = className.replace('.', '/');

                ClassWriter cw = new ClassWriter(0);
                cw.visit(V1_1, ACC_PUBLIC + ACC_SUPER, accessClassNameInternal, null, ReflectConsts.FIELD_ACCESS_PATH,
                         null);
                insertConstructor(cw);
                insertGetObject(cw, classNameInternal, fields);
                insertSetObject(cw, classNameInternal, fields);
                insertGetPrimitive(cw, classNameInternal, fields, Type.BOOLEAN_TYPE);
                insertSetPrimitive(cw, classNameInternal, fields, Type.BOOLEAN_TYPE);
                insertGetPrimitive(cw, classNameInternal, fields, Type.BYTE_TYPE);
                insertSetPrimitive(cw, classNameInternal, fields, Type.BYTE_TYPE);
                insertGetPrimitive(cw, classNameInternal, fields, Type.SHORT_TYPE);
                insertSetPrimitive(cw, classNameInternal, fields, Type.SHORT_TYPE);
                insertGetPrimitive(cw, classNameInternal, fields, Type.INT_TYPE);
                insertSetPrimitive(cw, classNameInternal, fields, Type.INT_TYPE);
                insertGetPrimitive(cw, classNameInternal, fields, Type.LONG_TYPE);
                insertSetPrimitive(cw, classNameInternal, fields, Type.LONG_TYPE);
                insertGetPrimitive(cw, classNameInternal, fields, Type.DOUBLE_TYPE);
                insertSetPrimitive(cw, classNameInternal, fields, Type.DOUBLE_TYPE);
                insertGetPrimitive(cw, classNameInternal, fields, Type.FLOAT_TYPE);
                insertSetPrimitive(cw, classNameInternal, fields, Type.FLOAT_TYPE);
                insertGetPrimitive(cw, classNameInternal, fields, Type.CHAR_TYPE);
                insertSetPrimitive(cw, classNameInternal, fields, Type.CHAR_TYPE);
                insertGetString(cw, classNameInternal, fields);
                cw.visitEnd();
                accessClass = loader.defineClass(accessClassName, cw.toByteArray());
            }
        }
        try {
            FieldAccess access = (FieldAccess) accessClass.newInstance();
            access.fieldNames = fieldNames;
            return access;
        } catch (Exception ex) {
            throw new RuntimeException("Error constructing field access class: " + accessClassName, ex);
        }
    }

    static private void insertConstructor(ClassWriter cw) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, ReflectConsts.FIELD_ACCESS_PATH, "<init>", "()V");
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    static private void insertSetObject(ClassWriter cw, String classNameInternal, List<Field> fields) {
        int maxStack = 6;
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "set", "(Ljava/lang/Object;ILjava/lang/Object;)V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ILOAD, 2);

        if (!fields.isEmpty()) {
            maxStack--;
            Label[] labels = new Label[fields.size()];
            for (int i = 0, n = labels.length; i < n; i++)
                labels[i] = new Label();
            Label defaultLabel = new Label();
            mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);

            for (int i = 0, n = labels.length; i < n; i++) {
                Field field = fields.get(i);
                Type fieldType = Type.getType(field.getType());

                mv.visitLabel(labels[i]);
                mv.visitFrame(F_SAME, 0, null, 0, null);
                mv.visitVarInsn(ALOAD, 1);
                mv.visitTypeInsn(CHECKCAST, classNameInternal);
                mv.visitVarInsn(ALOAD, 3);

                switch (fieldType.getSort()) {
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
                        mv.visitTypeInsn(CHECKCAST, fieldType.getDescriptor());
                        break;
                    case Type.OBJECT:
                        mv.visitTypeInsn(CHECKCAST, fieldType.getInternalName());
                        break;
                }

                mv.visitFieldInsn(PUTFIELD, classNameInternal, field.getName(), fieldType.getDescriptor());
                mv.visitInsn(RETURN);
            }

            mv.visitLabel(defaultLabel);
            mv.visitFrame(F_SAME, 0, null, 0, null);
        }
        mv = insertThrowExceptionForFieldNotFound(mv);
        mv.visitMaxs(maxStack, 4);
        mv.visitEnd();
    }

    static private void insertGetObject(ClassWriter cw, String classNameInternal, List<Field> fields) {
        int maxStack = 6;
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "get", "(Ljava/lang/Object;I)Ljava/lang/Object;", null, null);
        mv.visitCode();
        mv.visitVarInsn(ILOAD, 2);

        if (!fields.isEmpty()) {
            maxStack--;
            Label[] labels = new Label[fields.size()];
            for (int i = 0, n = labels.length; i < n; i++)
                labels[i] = new Label();
            Label defaultLabel = new Label();
            mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);

            for (int i = 0, n = labels.length; i < n; i++) {
                Field field = fields.get(i);

                mv.visitLabel(labels[i]);
                mv.visitFrame(F_SAME, 0, null, 0, null);
                mv.visitVarInsn(ALOAD, 1);
                mv.visitTypeInsn(CHECKCAST, classNameInternal);
                mv.visitFieldInsn(GETFIELD, classNameInternal, field.getName(), Type.getDescriptor(field.getType()));

                Type fieldType = Type.getType(field.getType());
                switch (fieldType.getSort()) {
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
            mv.visitFrame(F_SAME, 0, null, 0, null);
        }
        insertThrowExceptionForFieldNotFound(mv);
        mv.visitMaxs(maxStack, 3);
        mv.visitEnd();
    }

    static private void insertGetString(ClassWriter cw, String classNameInternal, List<Field> fields) {
        int maxStack = 6;
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "getString", "(Ljava/lang/Object;I)Ljava/lang/String;", null, null);
        mv.visitCode();
        mv.visitVarInsn(ILOAD, 2);

        if (!fields.isEmpty()) {
            maxStack--;
            Label[] labels = new Label[fields.size()];
            Label labelForInvalidTypes = new Label();
            boolean hasAnyBadTypeLabel = false;
            for (int i = 0, n = labels.length; i < n; i++) {
                if (fields.get(i).getType().equals(String.class))
                    labels[i] = new Label();
                else {
                    labels[i] = labelForInvalidTypes;
                    hasAnyBadTypeLabel = true;
                }
            }
            Label defaultLabel = new Label();
            mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);

            for (int i = 0, n = labels.length; i < n; i++) {
                if (!labels[i].equals(labelForInvalidTypes)) {
                    mv.visitLabel(labels[i]);
                    mv.visitFrame(F_SAME, 0, null, 0, null);
                    mv.visitVarInsn(ALOAD, 1);
                    mv.visitTypeInsn(CHECKCAST, classNameInternal);
                    mv.visitFieldInsn(GETFIELD, classNameInternal, fields.get(i).getName(), "Ljava/lang/String;");
                    mv.visitInsn(ARETURN);
                }
            }
            // Rest of fields: different type
            if (hasAnyBadTypeLabel) {
                mv.visitLabel(labelForInvalidTypes);
                mv.visitFrame(F_SAME, 0, null, 0, null);
                insertThrowExceptionForFieldType(mv, "String");
            }
            // Default: field not found
            mv.visitLabel(defaultLabel);
            mv.visitFrame(F_SAME, 0, null, 0, null);
        }
        insertThrowExceptionForFieldNotFound(mv);
        mv.visitMaxs(maxStack, 3);
        mv.visitEnd();
    }

    static private void insertSetPrimitive(ClassWriter cw, String classNameInternal, List<Field> fields, Type primitiveType) {
        int maxStack = 6;
        int maxLocals = 4; // See correction below for LLOAD and DLOAD
        final String setterMethodName;
        final String typeNameInternal = primitiveType.getDescriptor();
        final int loadValueInstruction;
        switch (primitiveType.getSort()) {
            case Type.BOOLEAN:
                setterMethodName = "setBoolean";
                loadValueInstruction = ILOAD;
                break;
            case Type.BYTE:
                setterMethodName = "setByte";
                loadValueInstruction = ILOAD;
                break;
            case Type.CHAR:
                setterMethodName = "setChar";
                loadValueInstruction = ILOAD;
                break;
            case Type.SHORT:
                setterMethodName = "setShort";
                loadValueInstruction = ILOAD;
                break;
            case Type.INT:
                setterMethodName = "setInt";
                loadValueInstruction = ILOAD;
                break;
            case Type.FLOAT:
                setterMethodName = "setFloat";
                loadValueInstruction = FLOAD;
                break;
            case Type.LONG:
                setterMethodName = "setLong";
                loadValueInstruction = LLOAD;
                maxLocals++; // (LLOAD and DLOAD actually load two slots)
                break;
            case Type.DOUBLE:
                setterMethodName = "setDouble";
                loadValueInstruction = DLOAD; // (LLOAD and DLOAD actually load two slots)
                maxLocals++;
                break;
            default:
                setterMethodName = "set";
                loadValueInstruction = ALOAD;
                break;
        }
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, setterMethodName, "(Ljava/lang/Object;I" + typeNameInternal + ")V", null,
                                          null);
        mv.visitCode();
        mv.visitVarInsn(ILOAD, 2);

        if (!fields.isEmpty()) {
            maxStack--;
            Label[] labels = new Label[fields.size()];
            Label labelForInvalidTypes = new Label();
            boolean hasAnyBadTypeLabel = false;
            for (int i = 0, n = labels.length; i < n; i++) {
                if (Type.getType(fields.get(i).getType()).equals(primitiveType))
                    labels[i] = new Label();
                else {
                    labels[i] = labelForInvalidTypes;
                    hasAnyBadTypeLabel = true;
                }
            }
            Label defaultLabel = new Label();
            mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);

            for (int i = 0, n = labels.length; i < n; i++) {
                if (!labels[i].equals(labelForInvalidTypes)) {
                    mv.visitLabel(labels[i]);
                    mv.visitFrame(F_SAME, 0, null, 0, null);
                    mv.visitVarInsn(ALOAD, 1);
                    mv.visitTypeInsn(CHECKCAST, classNameInternal);
                    mv.visitVarInsn(loadValueInstruction, 3);
                    mv.visitFieldInsn(PUTFIELD, classNameInternal, fields.get(i).getName(), typeNameInternal);
                    mv.visitInsn(RETURN);
                }
            }
            // Rest of fields: different type
            if (hasAnyBadTypeLabel) {
                mv.visitLabel(labelForInvalidTypes);
                mv.visitFrame(F_SAME, 0, null, 0, null);
                insertThrowExceptionForFieldType(mv, primitiveType.getClassName());
            }
            // Default: field not found
            mv.visitLabel(defaultLabel);
            mv.visitFrame(F_SAME, 0, null, 0, null);
        }
        mv = insertThrowExceptionForFieldNotFound(mv);
        mv.visitMaxs(maxStack, maxLocals);
        mv.visitEnd();
    }

    static private void insertGetPrimitive(ClassWriter cw, String classNameInternal, List<Field> fields, Type primitiveType) {
        int maxStack = 6;
        final String getterMethodName;
        final String typeNameInternal = primitiveType.getDescriptor();
        final int returnValueInstruction;
        switch (primitiveType.getSort()) {
            case Type.BOOLEAN:
                getterMethodName = "getBoolean";
                returnValueInstruction = IRETURN;
                break;
            case Type.BYTE:
                getterMethodName = "getByte";
                returnValueInstruction = IRETURN;
                break;
            case Type.CHAR:
                getterMethodName = "getChar";
                returnValueInstruction = IRETURN;
                break;
            case Type.SHORT:
                getterMethodName = "getShort";
                returnValueInstruction = IRETURN;
                break;
            case Type.INT:
                getterMethodName = "getInt";
                returnValueInstruction = IRETURN;
                break;
            case Type.FLOAT:
                getterMethodName = "getFloat";
                returnValueInstruction = FRETURN;
                break;
            case Type.LONG:
                getterMethodName = "getLong";
                returnValueInstruction = LRETURN;
                break;
            case Type.DOUBLE:
                getterMethodName = "getDouble";
                returnValueInstruction = DRETURN;
                break;
            default:
                getterMethodName = "get";
                returnValueInstruction = ARETURN;
                break;
        }
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, getterMethodName, "(Ljava/lang/Object;I)" + typeNameInternal, null, null);
        mv.visitCode();
        mv.visitVarInsn(ILOAD, 2);

        if (!fields.isEmpty()) {
            maxStack--;
            Label[] labels = new Label[fields.size()];
            Label labelForInvalidTypes = new Label();
            boolean hasAnyBadTypeLabel = false;
            for (int i = 0, n = labels.length; i < n; i++) {
                if (Type.getType(fields.get(i).getType()).equals(primitiveType))
                    labels[i] = new Label();
                else {
                    labels[i] = labelForInvalidTypes;
                    hasAnyBadTypeLabel = true;
                }
            }
            Label defaultLabel = new Label();
            mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);

            for (int i = 0, n = labels.length; i < n; i++) {
                Field field = fields.get(i);
                if (!labels[i].equals(labelForInvalidTypes)) {
                    mv.visitLabel(labels[i]);
                    mv.visitFrame(F_SAME, 0, null, 0, null);
                    mv.visitVarInsn(ALOAD, 1);
                    mv.visitTypeInsn(CHECKCAST, classNameInternal);
                    mv.visitFieldInsn(GETFIELD, classNameInternal, field.getName(), typeNameInternal);
                    mv.visitInsn(returnValueInstruction);
                }
            }
            // Rest of fields: different type
            if (hasAnyBadTypeLabel) {
                mv.visitLabel(labelForInvalidTypes);
                mv.visitFrame(F_SAME, 0, null, 0, null);
                insertThrowExceptionForFieldType(mv, primitiveType.getClassName());
            }
            // Default: field not found
            mv.visitLabel(defaultLabel);
            mv.visitFrame(F_SAME, 0, null, 0, null);
        }
        mv = insertThrowExceptionForFieldNotFound(mv);
        mv.visitMaxs(maxStack, 3);
        mv.visitEnd();
    }

    static private MethodVisitor insertThrowExceptionForFieldNotFound(MethodVisitor mv) {
        mv.visitTypeInsn(NEW, "java/lang/IllegalArgumentException");
        mv.visitInsn(DUP);
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
        mv.visitInsn(DUP);
        mv.visitLdcInsn("Field not found: ");
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V");
        mv.visitVarInsn(ILOAD, 2);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V");
        mv.visitInsn(ATHROW);
        return mv;
    }

    static private MethodVisitor insertThrowExceptionForFieldType(MethodVisitor mv, String fieldType) {
        mv.visitTypeInsn(NEW, "java/lang/IllegalArgumentException");
        mv.visitInsn(DUP);
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
        mv.visitInsn(DUP);
        mv.visitLdcInsn("Field not declared as " + fieldType + ": ");
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V");
        mv.visitVarInsn(ILOAD, 2);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V");
        mv.visitInsn(ATHROW);
        return mv;
    }
}
