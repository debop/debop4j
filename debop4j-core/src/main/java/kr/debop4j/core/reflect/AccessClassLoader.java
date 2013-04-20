package kr.debop4j.core.reflect;

import com.google.common.collect.Lists;
import kr.debop4j.core.Guard;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.List;

/**
 * kr.debop4j.core.reflect.AccessClassLoader
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 1. 21
 */
@Slf4j
class AccessClassLoader extends ClassLoader {

    private static final List<AccessClassLoader> accessClassLoaders = Lists.newArrayList();

    static AccessClassLoader get(Class type) {
        ClassLoader parent = type.getClassLoader();

        // com.google.common.collect.Iterables 를 사용하여 변경해 보세요.
        synchronized (accessClassLoaders) {
            for (AccessClassLoader loader : accessClassLoaders) {
                if (loader.getParent() == parent)
                    return loader;
            }
            AccessClassLoader loader = new AccessClassLoader(parent);
            accessClassLoaders.add(loader);
            return loader;
        }
    }

    private AccessClassLoader(ClassLoader parent) {
        super(parent);
    }

    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Guard.shouldNotBeEmpty(name, "name");
        if (name.equals(FieldAccess.class.getName())) return FieldAccess.class;
        if (name.equals(MethodAccess.class.getName())) return MethodAccess.class;
        if (name.equals(ConstructorAccess.class.getName())) return ConstructorAccess.class;

        //  all other classes come from the ClassLoader
        return super.loadClass(name, resolve);
    }

    Class<?> defineClass(String name, byte[] bytes) throws ClassFormatError {
        Guard.shouldNotBeEmpty(name, "name");
        Guard.shouldNotBeNull(bytes, "bytes");
        try {
            Method method =
                    ClassLoader.class.getDeclaredMethod("defineClass",
                                                        new Class[]{ String.class, byte[].class, int.class, int.class });
            method.setAccessible(true);
            return (Class) method.invoke(getParent(), name, bytes, Integer.valueOf(0), Integer.valueOf(bytes.length));

        } catch (Exception ignored) {}
        return defineClass(name, bytes, 0, bytes.length);
    }
}
