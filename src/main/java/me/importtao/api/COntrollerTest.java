package me.importtao.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program activiti
 * @description:
 * @author: changhu
 * @create: 2019/09/21 17:46
 */
@Slf4j(topic = "test")
@RestController
@RequestMapping("test")
public class COntrollerTest {
    @GetMapping("/test")
    @ResponseBody
    public  String test(){
        log.info("hello word");
        log.error("hello word");
        log.warn("hello word");
        log.debug("hello word");
        return "helloword";
    }
    @GetMapping("/test1")
    @ResponseBody
    public  String test1(){
        log.info("hello word");
        return "helloword";
    }
}
