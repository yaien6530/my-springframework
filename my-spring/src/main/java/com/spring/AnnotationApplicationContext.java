package com.spring;

import com.spring.annotation.Autowired;
import com.spring.annotation.Component;
import com.spring.annotation.ComponentScan;
import com.spring.factory.Aware;
import com.spring.factory.BeanNameAware;
import com.spring.factory.BeanPostProcessor;
import com.spring.factory.InitializingBean;
import com.spring.annotation.Scope;
import com.spring.difinition.BeanDefinition;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <h1> 配置类 </h1>
 *
 * @author aieny
 * @date 2022/2/12
 **/
public class AnnotationApplicationContext {

    private final Class configClass;

    /**
     * 用于存储BeanDefinition
     */
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(64);

    /**
     * 单例池（所谓的bean容器，所有的单例bean都会在这里存储）
     */
    private final Map<String, Object> singletonObjectMap = new ConcurrentHashMap<>(64);

    /**
     * 用于存储bean后置处理器
     */
    private final List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public AnnotationApplicationContext(Class config) {
        if (config == null) {
            throw new RuntimeException("配置类不能为空");
        }
        this.configClass = config;
        scan();
        createBean();
    }

    /**
     * 创建单例Bean
     */
    private void createBean() {

        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();

            Object bean = singletonObjectMap.get(beanName);
            if ("singleton".equals(beanDefinition.getScope()) && null == bean) {
                bean = doCreateBean(beanName, beanDefinition);
                singletonObjectMap.put(beanName, bean);
            }
        }

    }

    /**
     * 执行创建
     *
     * @param beanName       bean的名字
     * @param beanDefinition bean定义
     * @return 创建好的单例bean
     */
    private Object doCreateBean(String beanName, BeanDefinition beanDefinition) {
        Class clazz = beanDefinition.getType();

        Object bean = null;
        try {

            // 实例化前回调

            // 根据无参构造方法创建实例
            bean = clazz.getConstructor().newInstance();

            // 依赖注入(会存在循环依赖问题)
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    field.setAccessible(true);
                    // 通过属性名字去赋值（如果没有找到就加载，加载失败就报错）
                    field.set(bean, getBean(field.getName()));
                }
            }

            // 实例化后回调

            // 调用Aware
            if (bean instanceof BeanNameAware) {
                ((BeanNameAware) bean).setBeanName(beanName);
            }

            // 初始化前回调
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                bean = beanPostProcessor.postProcessBeforeInitialization(bean, beanName);
            }

            // 初始化
            if (bean instanceof InitializingBean) {
                ((InitializingBean) bean).afterPropertiesSet();
            }

            // 初始化后回调
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                bean = beanPostProcessor.postProcessAfterInitialization(bean, beanName);
            }

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            e.printStackTrace();
        }
        return bean;
    }


    /**
     * 获取Bean
     *
     * @param beanName beanName
     * @return bean
     */
    public Object getBean(String beanName) {

        if (!beanDefinitionMap.containsKey(beanName)) {
            // 表示当前spring容器没有该bean
            throw new NullPointerException();
        }

        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if ("singleton".equals(beanDefinition.getScope())) {
            Object bean = singletonObjectMap.get(beanName);
            if (bean == null) {
                bean = doCreateBean(beanName, beanDefinition);
                singletonObjectMap.put(beanName, bean);
            }
            return bean;
        } else {
            // 原型bean （多例）
            return doCreateBean(beanName, beanDefinition);
        }
    }

    /**
     * 获取扫描路径
     */
    private void scan() {

        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
            String path = componentScanAnnotation.value();
            path = path.replaceAll("\\.", "/");

            ClassLoader classLoader = this.getClass().getClassLoader();
            URL resource = classLoader.getResource(path);
            assert resource != null;
            File file = new File(resource.getFile());
            if (file.isDirectory()) {
                for (File f : Objects.requireNonNull(file.listFiles())) {
                    String absolutePath = f.getAbsolutePath();
                    absolutePath = absolutePath.substring(absolutePath.indexOf("com"), absolutePath.indexOf(".class"));
                    absolutePath = absolutePath.replaceAll("/", ".");
                    try {
                        Class<?> clazz = classLoader.loadClass(absolutePath);
                        if (clazz.isAnnotationPresent(Component.class)) {
                            // 判断是否实现了BeanPostProcessor接口
                            if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                                try {
                                    BeanPostProcessor beanPostProcessor = (BeanPostProcessor) clazz.getConstructor().newInstance();
                                    beanPostProcessorList.add(beanPostProcessor);
                                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                                         NoSuchMethodException e) {
                                    e.printStackTrace();
                                }
                            }

                            // 创建一个bean定义
                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setType(clazz);

                            if (clazz.isAnnotationPresent(Scope.class)) {
                                // 判断是否单例加载
                                Scope scopeAnnotation = clazz.getAnnotation(Scope.class);
                                String value = scopeAnnotation.value();
                                beanDefinition.setScope(value);
                            } else {
                                // 默认单例的
                                beanDefinition.setScope("singleton");
                            }

                            // 获取定义在component头部的值作为bean的名字
                            Component componentAnnotation = clazz.getAnnotation(Component.class);
                            String beanName = componentAnnotation.value();
                            if ("".equals(beanName)) {
                                beanName = Introspector.decapitalize(clazz.getSimpleName());
                            }

                            // 保存bean定义
                            beanDefinitionMap.put(beanName, beanDefinition);

                        }

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

    }
}
