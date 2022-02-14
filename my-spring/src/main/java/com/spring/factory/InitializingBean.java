package com.spring.factory;

/**
 * <h1>  </h1>
 *
 * @author aieny
 * @date 2022/2/12
 **/
public interface InitializingBean {
    /**
     * 自定义初始化
     */
    void afterPropertiesSet();
}
