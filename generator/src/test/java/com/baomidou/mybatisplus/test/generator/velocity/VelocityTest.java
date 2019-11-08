package com.baomidou.mybatisplus.test.generator.velocity;

import lombok.Data;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @program activiti
 * @description:
 * @author: changhu
 * @create: 2019/08/28 11:54
 */
@Data
public class VelocityTest {

    private int id;
    private String name;


    public static void main(String[] args) {
        VelocityTest velocityTest = new VelocityTest();
        velocityTest.setId(1);
        velocityTest.setName("testName");
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER,"classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();
        Template template = velocityEngine.getTemplate("templates/test.vm");
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("test",velocityTest);

        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            stringList.add(String.valueOf(i));
        }

        velocityContext.put("list",stringList);
        velocityContext.put("velocityTest",velocityTest);
        StringWriter stringWriter = new StringWriter();

        template.merge(velocityContext,stringWriter);
        System.out.println(stringWriter.toString());


    }
}
