package com.hengzhang.springboot.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * spring 获取上下文工具类以及获取bean的方法
 * @author zhangh
 * @date 2018年8月3日下午5:56:21
 */
@Component
public class SpringUtil implements ApplicationContextAware{

    private static ApplicationContext applicationContext;

    @Override
    public  void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(SpringUtil.applicationContext == null) {
            SpringUtil.applicationContext = applicationContext;
        }
    }

    //获取applicationContext
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过name获取 Bean.
     * @author zhangh
     * @date 2018年8月3日下午5:57:06
     * @param name
     * @return
     */
    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean.
     * @author zhangh
     * @date 2018年8月3日下午5:57:15
     * @param clazz
     * @return
     */
    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     * @author zhangh
     * @date 2018年8月3日下午5:57:24
     * @param name
     * @param clazz
     * @return
     */
    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }

}