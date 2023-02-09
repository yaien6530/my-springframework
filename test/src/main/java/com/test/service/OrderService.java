package com.test.service;

import com.spring.annotation.Component;
import com.spring.factory.BeanNameAware;

/**
 * <h1> 订单服务 </h1>
 *
 * @author aieny
 * @date 2022/2/12
 **/
@Component
public class OrderService implements BeanNameAware {

    private String beanName;

    public void test() {
        System.err.println(beanName + " Test()!!");
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
