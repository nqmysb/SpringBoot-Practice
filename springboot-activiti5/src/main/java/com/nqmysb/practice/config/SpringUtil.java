package com.nqmysb.practice.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


@Component
public class SpringUtil implements ApplicationContextAware {



    /**
     * 当前IOC
     *
     */
    private static ApplicationContext applicationContext;

    /**
     * * 设置当前上下文环境，此方法由spring自动装配 前提必须用注解@Component 通知spring
     *
     */
    @Override
    public void setApplicationContext(ApplicationContext arg0)
            throws BeansException {
        applicationContext = arg0;
    }

    /**
     * 从当前IOC获取bean
     *
     * @param id
     * bean的id
     * @return
     *
     */
    public static Object getObject(String id) {
        Object object = null;
        object = applicationContext.getBean(id);
        return object;
    }
    /**
     * 从IOC中获取bean 通过class对象
     * @param zlass
     * @return
     */
    public static Object getObject(Class<?> zlass) {
        Object object = null;
        object = applicationContext.getBean(zlass) ;
        return object;
    }
    
    /**
     * 获取当前IOC容器
     * @return
     */
    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }


}