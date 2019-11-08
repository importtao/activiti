package me.importtao.aware;

import me.importtao.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @program activiti
 * @description:
 * @author: changhu
 * @create: 2019/08/29 10:18
 */
public class AwareTest extends BaseTest {
    @Autowired
    MeApplicationContextAware meApplicationContextAware;

    @Test
    public void test(){
        meApplicationContextAware.test();
    }
}
