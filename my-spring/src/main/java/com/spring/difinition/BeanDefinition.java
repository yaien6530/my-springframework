package com.spring.difinition;

/**
 * <h1> Bean定义 </h1>
 * <p>
 * 个人理解是为了防止当需要进行懒加载等等操作的Bean进行getBean（），
 * 还要重新去解析class的注解等属性，因为解析这一步已经在扫描的时候做了，
 * 所以可以将信息保存到此元信息类中
 * </p>
 *
 * @author aieny
 * @date 2022/2/12
 **/
public class BeanDefinition {
    /**
     * 类型
     */
    private Class type;

    /**
     * 创建形式（单例或原型）
     */
    private String scope;

    /**
     * 是否懒加载
     */
    private boolean isLazy;

    // 等等...


    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isLazy() {
        return isLazy;
    }

    public void setLazy(boolean lazy) {
        isLazy = lazy;
    }
}
