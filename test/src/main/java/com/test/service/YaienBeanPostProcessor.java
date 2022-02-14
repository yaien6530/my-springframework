package com.test.service;

import com.spring.annotation.Component;
import com.spring.factory.BeanPostProcessor;

import java.lang.reflect.Proxy;

/**
 * <h1> 自定义的bean后置处理器 </h1>
 *
 * @author aieny
 * @date 2022/2/12
 **/
@Component
public class YaienBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        System.err.println(beanName + "初始化前回调");
        return bean;
    }

//    @Override
//    public Object postProcessAfterInitialization(Object bean, String beanName) {
//
//        // 通过后置处理器我们就可以做AOP，此处用的是JDK动态代理
//        if ("userService".equals(beanName)) {
//            Object proxyInstance = Proxy.newProxyInstance(
//                    YaienBeanPostProcessor.class.getClassLoader(),
//                    bean.getClass().getInterfaces(),
//                    (proxy, method, args) -> {
//                        // 切面
//                        System.err.println("AOP 切面逻辑");
//
//                        // 我们自己的逻辑
//                        return method.invoke(bean, args);
//                    });
//
//            return proxyInstance;
//        }
//
//        return bean;
//    }
}
