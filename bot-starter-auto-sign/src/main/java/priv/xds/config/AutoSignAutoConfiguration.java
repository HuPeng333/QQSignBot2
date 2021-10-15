package priv.xds.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author DeSen Xu
 * @date 2021-10-13 21:31
 */
@Configuration
@ConditionalOnProperty(prefix = "bot.auto-sign", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(AutoSignProperties.class)
public class AutoSignAutoConfiguration {

    public void sayHello() {

    }
}
