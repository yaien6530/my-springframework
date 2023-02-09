package com.test;

import com.spring.AnnotationApplicationContext;
import com.test.config.AppConfig;
import com.test.service.IUserInterface;
import com.test.service.UserService;

/**
 * <h1> 启动类 </h1>
 *
 * @author aieny
 * @date 2022/2/12
 **/
public class Test {

    public static void main(String[] args) {

        // 扫描 ---> 创建单例Bean
        AnnotationApplicationContext context = new AnnotationApplicationContext(AppConfig.class);


        // 从容器中获取Bean
//        UserService userService = (UserService) context.getBean("userService");

        // 实现动态代理 （由于使用的是JDK动态代理，底层使用接口实现，
        // 若使用CJLB则不需要转接口实现)
        IUserInterface userService = (IUserInterface) context.getBean("userService");


        userService.test();
    }

}
