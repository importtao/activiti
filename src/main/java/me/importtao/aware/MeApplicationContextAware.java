package me.importtao.aware;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @program activiti
 * @description: 获取ioc容器
 * @author: changhu
 * @create: 2019/08/29 10:07
 */
@Component
public class MeApplicationContextAware implements ApplicationContextAware {
    private  ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void test(){
        String name = this.applicationContext.getApplicationName();
        System.out.println(name);
    }
}
