package com.test.service;

import com.spring.annotation.Autowired;
import com.spring.annotation.Component;
import com.spring.factory.InitializingBean;

/**
 * <h1> 用户服务 </h1>
 *
 * @author aieny
 * @date 2022/2/12
 **/
@Component()
public class UserService implements InitializingBean, IUserInterface {

    @Autowired
    private OrderService orderService;

    public void test() {
        System.err.println("User Service Test()!!");
        System.err.println("orderService------->  " + orderService);
    }

    @Override
    public void afterPropertiesSet() {
        System.err.println("正在初始化");
    }
}
