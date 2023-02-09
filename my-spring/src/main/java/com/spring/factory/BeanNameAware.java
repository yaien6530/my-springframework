package com.spring.factory;

/**
 * <h1>  </h1>
 *
 * @author aieny
 * @date 2023-02-09
 **/
public interface BeanNameAware extends Aware {

    /**
     * 设置BeanName
     * @param beanName beanName
     */
    void setBeanName(String beanName);

}
