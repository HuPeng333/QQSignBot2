package priv.xds;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import love.forte.simbot.spring.autoconfigure.EnableSimbot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import priv.xds.listener.AutoSignListener;

/**
 * @author DeSen Xu
 * @date 2021-10-13 21:37
 */
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        MybatisPlusAutoConfiguration.class
})
@EnableSimbot
public class BotCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(BotCoreApplication.class, args);
    }
}
