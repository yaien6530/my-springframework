package com.spring.factory;

import com.sun.istack.internal.Nullable;

/**
 * <h1> bean的后置处理器 </h1>
 * 如果实现了此接口，则每一个bean在创建时都会调用接口的方法
 *
 * @author aieny
 * @date 2022/2/12
 **/
public interface BeanPostProcessor {


    /**
     * 初始化前
     *
     * @param bean     bean对象
     * @param beanName bean名称
     * @return 处理后的bean
     */
    @Nullable
    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    /**
     * 初始化后
     *
     * @param bean     bean对象
     * @param beanName bean名称
     * @return 处理后的bean
     */
    @Nullable
    default Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
