package priv.xds.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import priv.xds.service.TestService;

/**
 * @author DeSen Xu
 * @date 2021-10-12 22:20
 */
@RestController
public class TestController {

    @DubboReference
    TestService testService;

    @GetMapping("/hello")
    public String hello() {
        System.out.println("调用了消费者的hello方法");
        return testService.sayHello();
    }
}
