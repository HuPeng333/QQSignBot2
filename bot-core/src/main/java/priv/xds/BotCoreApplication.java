package priv.xds;

import love.forte.simbot.spring.autoconfigure.EnableSimbot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author DeSen Xu
 * @date 2021-10-13 21:37
 */
@SpringBootApplication
@EnableSimbot
public class BotCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(BotCoreApplication.class, args);
    }
}
