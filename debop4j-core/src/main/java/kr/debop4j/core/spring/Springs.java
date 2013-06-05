/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.debop4j.core.spring;

import kr.debop4j.core.AutoCloseableAction;
import kr.debop4j.core.Guard;
import kr.debop4j.core.Local;
import kr.debop4j.core.tools.ArrayTool;
import kr.debop4j.core.tools.StringTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static kr.debop4j.core.Guard.shouldBe;
import static kr.debop4j.core.Guard.shouldNotBeNull;

/**
 * Springs Framework 의 Dependency Injection을 담당하는 클래스입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 11. 23.
 */
@Slf4j
@ThreadSafe
@Component
public final class Springs {

    /**
     * Instantiates a new Springs.
     *
     * @param context the context
     */
    @Autowired
    protected Springs(ApplicationContext context) {
        globalContext = context;
    }

    public static final String DEFAULT_APPLICATION_CONTEXT_XML = "applicationContext.xml";
    private static final String LOCAL_SPRING_CONTEXT = Springs.class.getName() + ".globalContext";
    private static final String NOT_INITIALIZED_MSG =
            "Springs의 ApplicationContext가 초기화되지 않았습니다. Springs를 ComponentScan 해주셔야합니다. ";

    private static volatile ApplicationContext globalContext;
    private static ThreadLocal<Stack<GenericApplicationContext>> localContextStack = new ThreadLocal<>();

    /**
     * Is initialized.
     *
     * @return the boolean
     */
    public static synchronized boolean isInitialized() {
        return (globalContext != null);
    }

    /**
     * Is not initialized.
     *
     * @return the boolean
     */
    public static synchronized boolean isNotInitialized() {
        return (globalContext == null);
    }

    private static synchronized void assertInitialized() {
        shouldBe(isInitialized(), NOT_INITIALIZED_MSG);
    }

    /**
     * Gets context.
     *
     * @return the context
     */
    public static synchronized GenericApplicationContext getContext() {
        ApplicationContext context = getLocalContext();
        if (context == null)
            context = globalContext;
        shouldBe(context != null, NOT_INITIALIZED_MSG);

        return (GenericApplicationContext) context;
    }

    private static synchronized GenericApplicationContext getLocalContext() {
        if (getLocalContextStack().size() == 0)
            return null;
        return getLocalContextStack().peek();
    }

    private static synchronized Stack<GenericApplicationContext> getLocalContextStack() {
        if (localContextStack.get() == null) {
            localContextStack.set(new Stack<GenericApplicationContext>());
        }
        return localContextStack.get();
    }

    /**
     * Init void.
     */
    public static synchronized void init() {
        init(DEFAULT_APPLICATION_CONTEXT_XML);
    }

    /**
     * Init void.
     *
     * @param resourceLocations the resource locations
     */
    public static synchronized void init(String... resourceLocations) {
        log.info("Springs Context 를 초기화합니다. resourceLocations=[{}]", StringTool.listToString(resourceLocations));
        init(new GenericXmlApplicationContext(resourceLocations));
    }

    /**
     * Init void.
     *
     * @param applicationContext the application context
     */
    public static synchronized void init(ApplicationContext applicationContext) {
        shouldNotBeNull(applicationContext, "applicationContext");
        log.info("Springs ApplicationContext 를 초기화 작업을 시작합니다...");
        if (globalContext != null) {
            log.warn("Springs ApplicationContext가 이미 초기화 되었으므로, 무시합니다. reset 후 init 을 호출하세요.");
        }
        globalContext = applicationContext;
        log.info("Springs ApplicationContext를 초기화 작업을 완료했습니다.");
    }

    /**
     * Init by annotated classes.
     *
     * @param annotatedClasses the annotated classes
     */
    public static synchronized void initByAnnotatedClasses(Class<?>... annotatedClasses) {
        init(new AnnotationConfigApplicationContext(annotatedClasses));
    }

    /**
     * Init by packages.
     *
     * @param basePackages the base packages
     */
    public static synchronized void initByPackages(String... basePackages) {
        init(new AnnotationConfigApplicationContext(basePackages));
    }


    /**
     * Use local context.
     *
     * @param localContext the local context
     * @return the auto closeable action
     */
    public static synchronized AutoCloseableAction useLocalContext(final GenericApplicationContext localContext) {
        shouldNotBeNull(localContext, "localContext");

        if (log.isDebugEnabled())
            log.debug("로컬 컨텍스트를 사용하려고 합니다... localContext=[{}]", localContext);

        getLocalContextStack().push(localContext);
        return new AutoCloseableAction(new Runnable() {
            @Override
            public void run() {
                reset(localContext);
            }
        });
    }

    /**
     * 지정된 ApplicationContext 를 초기화합니다.
     *
     * @param contextToReset 초기화 시킬 ApplicationContext
     */
    public static synchronized void reset(@Nullable final ApplicationContext contextToReset) {

        if (contextToReset == null) {
            globalContext = null;
            log.info("Global Springs Context 를 Reset 했습니다!!!");
            return;
        }

        if (log.isDebugEnabled())
            log.debug("ApplicationContext=[{}] 을 Reset 합니다...", contextToReset);

        if (getLocalContext() == contextToReset) {
            getLocalContextStack().pop();

            if (getLocalContextStack().size() == 0)
                Local.put(LOCAL_SPRING_CONTEXT, null);

            log.info("Local Application Context 를 Reset 했습니다.");
            return;
        }

        if (globalContext == contextToReset) {
            globalContext = null;
            log.info("Global Application Context 를 Reset 했습니다!!!");
        }
    }

    /** Springs ApplicationContext를 초기화합니다. */
    public static synchronized void reset() {
        if (getLocalContext() != null)
            reset(getLocalContext());
        else
            reset(globalContext);
    }


    public static synchronized Object getBean(String name) {
        assertInitialized();
        if (log.isDebugEnabled())
            log.debug("ApplicationContext로부터 Bean을 가져옵니다. name=[{}]", name);

        return getContext().getBean(name);
    }

    public static synchronized Object getBean(String name, Object... args) {
        assertInitialized();
        if (log.isDebugEnabled())
            log.debug("ApplicationContext로부터 Bean을 가져옵니다. name=[{}], args=[{}]", name, StringTool.listToString(args));

        return getContext().getBean(name, args);
    }

    public static synchronized <T> T getBean(Class<T> beanClass) {
        assertInitialized();
        if (log.isDebugEnabled())
            log.debug("ApplicationContext로부터 Bean을 가져옵니다. beanClass=[{}]", beanClass.getName());
        return getContext().getBean(beanClass);
    }

    public static synchronized <T> T getBean(String name, Class<T> beanClass) {
        assertInitialized();
        if (log.isDebugEnabled())
            log.debug("ApplicationContext로부터 Bean을 가져옵니다. beanName=[{}], beanClass=[{}]", name, beanClass);

        return getContext().getBean(name, beanClass);
    }

    public static synchronized <T> String[] getBeanNamesForType(Class<T> beanClass) {
        return getBeanNamesForType(beanClass, true, true);
    }

    public static synchronized <T> String[] getBeanNamesForType(Class<T> beanClass,
                                                                boolean includeNonSingletons,
                                                                boolean allowEagerInit) {
        shouldNotBeNull(beanClass, "beanClass");
        if (log.isDebugEnabled())
            log.debug("해당 수형의 모든 Bean의 이름을 조회합니다. beanClass=[{}], includeNonSingletons=[{}], allowEagerInit=[{}]",
                    beanClass.getName(), includeNonSingletons, allowEagerInit);

        return getContext().getBeanNamesForType(beanClass, includeNonSingletons, allowEagerInit);
    }

    /** 지정한 타입의 Bean 들의 인스턴스를 가져옵니다. (Prototype Bean 도 포함됩니다.) */
    public static <T> List<T> getBeansByType(Class<T> beanClass) {
        return getBeansByType(beanClass, true, true);
    }

    public static <T> List<T> getBeansByType(Class<T> beanClass, boolean includeNonSingletons, boolean allowEagerInit) {
        Map<String, T> beanMap = getBeansOfType(beanClass, includeNonSingletons, allowEagerInit);
        return ArrayTool.toList(beanMap.values());
    }

    public static <T> T getFirstBeanByType(Class<T> beanClass) {
        return getFirstBeanByType(beanClass, true, true);
    }

    public static <T> T getFirstBeanByType(Class<T> beanClass, boolean includeNonSingletons, boolean allowEagerInit) {
        List<T> beans = getBeansByType(beanClass, includeNonSingletons, allowEagerInit);
        if (beans != null && beans.size() > 0)
            return beans.get(0);
        else
            return null;
    }

    /** 지정된 수형 또는 상속한 수형으로 등록된 bean 들을 조회합니다. */
    public static synchronized <T> Map<String, T> getBeansOfType(Class<T> beanClass) {
        return getBeansOfType(beanClass, true, true);
    }

    /** 지정된 수형 또는 상속한 수형으로 등록된 bean 들을 조회합니다. */
    public static synchronized <T> Map<String, T> getBeansOfType(Class<T> beanClass,
                                                                 boolean includeNonSingletons,
                                                                 boolean allowEagerInit) {
        assert beanClass != null;
        if (log.isDebugEnabled())
            log.debug("해당 수형의 모든 Bean을 조회합니다. beanClass=[{}], includeNonSingletons=[{}], allowEagerInit=[{}]",
                    beanClass.getName(), includeNonSingletons, allowEagerInit);

        return getContext().getBeansOfType(beanClass,
                includeNonSingletons,
                allowEagerInit);
    }

    public static synchronized <T> T getOrRegisterBean(Class<T> beanClass) {
        return getOrRegisterBean(beanClass, ConfigurableBeanFactory.SCOPE_SINGLETON);
    }

    public static synchronized <T> T getOrRegisterBean(Class<T> beanClass, String scope) {
        return getOrRegisterBean(beanClass, beanClass, scope);
    }

    public static synchronized <T> T getOrRegisterBean(Class<T> beanClass, Class<? extends T> registBeanClass) {
        return getOrRegisterBean(beanClass, registBeanClass, ConfigurableBeanFactory.SCOPE_SINGLETON);
    }

    /**
     * 등록된 beanClass 를 조회 (보통 Interface) 하고, 없다면, registerBeanClass (Concrete Class) 를 등록합니다.
     *
     * @param beanClass       조회할 Bean의 수형 (보통 인터페이스)
     * @param registBeanClass 등록되지 않은 beanClass 일때, 실제 등록할 Bean의 수형 (Concrete Class)
     * @param scope           "Singleton", "Scope"
     * @param <T>             Bean의 수형
     * @return 등록된 Bean의 인스턴스
     */
    public static synchronized <T> T getOrRegisterBean(Class<T> beanClass,
                                                       Class<? extends T> registBeanClass,
                                                       String scope) {
        T bean = getFirstBeanByType(beanClass, true, true);
        if (bean != null)
            return bean;

        registerBean(registBeanClass.getName(), registBeanClass, scope);
        return getContext().getBean(registBeanClass);
    }

    public static synchronized <T> T getOrRegisterSingletonBean(Class<T> beanClass) {
        return getOrRegisterBean(beanClass, ConfigurableBeanFactory.SCOPE_SINGLETON);
    }

    public static synchronized <T> T getOrRegisterPrototypeBean(Class<T> beanClass) {
        return getOrRegisterBean(beanClass, ConfigurableBeanFactory.SCOPE_PROTOTYPE);
    }

    public static synchronized boolean isBeanNameInUse(String beanName) {
        return getContext().isBeanNameInUse(beanName);
    }

    public static synchronized boolean isRegisteredBean(String beanName) {
        return getContext().isBeanNameInUse(beanName);
    }

    public static synchronized <T> boolean isRegisteredBean(Class<T> beanClass) {
        assert beanClass != null;
        try {
            return (getContext().getBean(beanClass) != null);
        } catch (Exception e) {
            return false;
        }
    }

    public static synchronized <T> boolean registerBean(String beanName, Class<T> beanClass) {
        return registerBean(beanName, beanClass, ConfigurableBeanFactory.SCOPE_SINGLETON);
    }

    public static synchronized <T> boolean registerBean(String beanName,
                                                        Class<T> beanClass,
                                                        String scope,
                                                        PropertyValue... propertyValues) {
        assert beanClass != null;
        BeanDefinition definition = new RootBeanDefinition(beanClass);
        definition.setScope(scope);

        for (PropertyValue pv : propertyValues) {
            definition.getPropertyValues().addPropertyValue(pv);
        }
        return registerBean(beanName, definition);
    }

    public static synchronized boolean registerBean(String beanName, BeanDefinition beanDefinition) {
        Guard.shouldNotBeEmpty(beanName, "beanName");
        shouldNotBeNull(beanDefinition, "beanDefinition");

        if (isBeanNameInUse(beanName))
            throw new BeanDefinitionValidationException("이미 등록된 Bean입니다. beanName=" + beanName);

        if (log.isInfoEnabled())
            log.info("새로운 Bean을 등록합니다. beanName=[{}], beanDefinition=[{}]", beanName, beanDefinition);

        try {
            getContext().registerBeanDefinition(beanName, beanDefinition);
            return true;
        } catch (Exception e) {
            log.error("새로운 Bean 등록에 실패했습니다. beanName=" + beanName, e);
        }
        return false;
    }

    public static synchronized boolean registerBean(String beanName, Object instance) {
        Guard.shouldNotBeEmpty(beanName, "beanName");

        try {
            getContext().getBeanFactory().registerSingleton(beanName, instance);
            return true;
        } catch (Exception e) {
            log.error("인스턴스를 빈으로 등록하는데 실패했습니다. beanName=" + beanName, e);
            return false;
        }
    }

    public static synchronized boolean registerSingletonBean(String beanName, Object instance) {
        return registerBean(beanName, instance);
    }

    public static synchronized <T> boolean registerSingletonBean(Class<T> beanClass, PropertyValue... pvs) {
        assert beanClass != null;
        return registerSingletonBean(beanClass.getName(), beanClass, pvs);
    }

    public static synchronized <T> boolean registerSingletonBean(String beanName, Class<T> beanClass, PropertyValue... pvs) {
        return registerBean(beanName, beanClass, ConfigurableBeanFactory.SCOPE_SINGLETON, pvs);
    }

    public static synchronized <T> boolean registerPrototypeBean(Class<T> beanClass, PropertyValue... pvs) {
        assert beanClass != null;
        return registerPrototypeBean(beanClass.getName(), beanClass, pvs);
    }

    public static synchronized <T> boolean registerPrototypeBean(String beanName, Class<T> beanClass, PropertyValue... pvs) {
        return registerBean(beanName, beanClass, ConfigurableBeanFactory.SCOPE_PROTOTYPE, pvs);
    }

    public static synchronized void removeBean(String beanName) {
        Guard.shouldNotBeEmpty(beanName, "beanName");

        if (isBeanNameInUse(beanName)) {
            if (log.isDebugEnabled())
                log.debug("ApplicationContext에서 name=[{}]인 Bean을 제거합니다.", beanName);
            getContext().removeBeanDefinition(beanName);
        }
    }

    public static synchronized <T> void removeBean(Class<T> beanClass) {
        assert beanClass != null;
        if (log.isDebugEnabled())
            log.debug("Bean 형식 [{}]의 모든 Bean을 ApplicationContext에서 제거합니다.", beanClass.getName());

        String[] beanNames = getContext().getBeanNamesForType(beanClass, true, true);
        for (String beanName : beanNames)
            removeBean(beanName);
    }

    public static synchronized Object tryGetBean(String beanName) {
        Guard.shouldNotBeEmpty(beanName, "beanName");
        try {
            return getBean(beanName);
        } catch (Exception e) {
            log.warn("bean을 찾는데 실패했습니다. null을 반환합니다. beanName=" + beanName, e);
            return null;
        }
    }

    public static synchronized Object tryGetBean(String beanName, Object... args) {
        Guard.shouldNotBeEmpty(beanName, "beanName");
        try {
            return getBean(beanName, args);
        } catch (Exception e) {
            log.warn("bean을 찾는데 실패했습니다. null을 반환합니다. beanName=" + beanName, e);
            return null;
        }
    }

    public static synchronized <T> T tryGetBean(Class<T> beanClass) {
        shouldNotBeNull(beanClass, "beanClass");
        try {
            return getBean(beanClass);
        } catch (Exception e) {
            log.warn("bean을 찾는데 실패했습니다. null을 반환합니다. beanClass=" + beanClass.getName(), e);
            return null;
        }
    }

    public static synchronized <T> T tryGetBean(String beanName, Class<T> beanClass) {
        shouldNotBeNull(beanClass, "beanClass");
        try {
            return getBean(beanName, beanClass);
        } catch (Exception e) {
            log.warn("bean을 찾는데 실패했습니다. null을 반환합니다. beanName=" + beanName, e);
            return null;
        }
    }
}
