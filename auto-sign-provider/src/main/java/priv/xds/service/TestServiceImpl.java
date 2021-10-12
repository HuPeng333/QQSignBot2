package priv.xds.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * @author DeSen Xu
 * @date 2021-10-12 22:02
 */
@Service
@DubboService
public class TestServiceImpl implements TestService {

    @Override
    @SentinelResource(value = "sayHello", fallback = "fallback")
    public String sayHello() {
        int num = (int) Math.round(Math.random());
        System.out.println("===================\n" + num);
        if (num == 0) {
            System.out.println(0/0);
        }
        return "Hello World! num is: " + num;
    }

    public String fallback() {
        return "server is busy!";
    }

}
