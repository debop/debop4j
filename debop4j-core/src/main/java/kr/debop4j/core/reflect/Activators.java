package kr.debop4j.core.reflect;

import com.google.common.collect.Lists;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.ArrayTool;
import kr.debop4j.core.tools.StringTool;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * 리플렉션을 이용하여, 객체를 생성시키는 Utility Class 입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 12
 */
public final class Activators {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Activators.class);
    private static final boolean isDebugEnabled = log.isDebugEnabled();

    private Activators() { }

    public static Object createInstance(String className) {
        try {
            return createInstance(Class.forName(className));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 지정된 수형의 새로운 인스턴스를 생성합니다.
     *
     * @param clazz 생성할 수형
     * @param <T>   수형
     * @return 지정한 수형의 새로운 인스턴스, 생성 실패시에는 null을 반환합니다.
     */
    public static <T> T createInstance(Class<T> clazz) {
        Guard.shouldNotBeNull(clazz, "clazz");
        if (log.isDebugEnabled())
            log.debug("수형 [{}] 의 새로운 인스턴스를 생성합니다...", clazz.getName());

        try {
            return (T) clazz.newInstance();
        } catch (Exception e) {
            if (log.isWarnEnabled())
                log.warn(clazz.getName() + " 수형을 생성하는데 실패했습니다.", e);
            return null;
        }
    }


    @SuppressWarnings("unchecked")
    public static <T> T createInstance(Class<T> clazz, Object... initArgs) {
        Guard.shouldNotBeNull(clazz, "clazz");
        if (log.isDebugEnabled())
            log.debug("[{}] 수형의 객체를 생성합니다. initArgs=[{}]",
                      clazz.getName(), StringTool.listToString(initArgs));
        if (initArgs == null || initArgs.length == 0)
            return createInstance(clazz);

        try {
            List<Class<?>> parameterTypes = Lists.newArrayList();
            for (Object arg : initArgs) {
                Class<?> argClass = arg.getClass();
                if (argClass == Integer.class)
                    argClass = Integer.TYPE;
                if (argClass == Long.class)
                    argClass = Long.TYPE;
                parameterTypes.add(argClass);
            }
            try {
                Constructor<T> constructor = clazz.getDeclaredConstructor(ArrayTool.asArray(parameterTypes, Class.class));
                return (constructor != null)
                        ? constructor.newInstance(initArgs)
                        : null;
            } catch (Throwable ignored) {
                Constructor<?>[] constructors = clazz.getDeclaredConstructors();
                for (Constructor<?> ctor : constructors) {
                    if (ctor.getParameterTypes().length == parameterTypes.size())
                        return (T) ctor.newInstance(initArgs);
                }
            }
        } catch (Exception e) {
            if (log.isErrorEnabled())
                log.error(clazz.getName() + " 수형을 생성하는데 실패했습니다.", e);
            throw new RuntimeException(e);
        }
        return null;
    }

    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes) {
        if (log.isDebugEnabled())
            log.debug("[{}] 수형의 생성자를 구합니다. parameterTypes=[{}]",
                      clazz.getName(), StringTool.listToString(parameterTypes));
        try {
            return clazz.getDeclaredConstructor(parameterTypes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
