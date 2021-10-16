package priv.xds.config;

import love.forte.simbot.bot.BotManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import priv.xds.listener.AutoSignListener;
import priv.xds.task.AutoSignExpireTip;
import priv.xds.task.AutoSignTask;

/**
 * @author DeSen Xu
 * @date 2021-10-13 21:31
 */
@Configuration
@ConditionalOnProperty(prefix = "bot.auto-sign", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(AutoSignProperties.class)
@EnableScheduling
public class AutoSignAutoConfiguration {

    @Bean
    public AutoSignListener getAutoSignListener() {
        return new AutoSignListener();
    }

    @Bean
    public AutoSignExpireTip getAutoSignExpireTip(BotManager botManager) {
        return new AutoSignExpireTip(botManager);
    }

    @Bean
    public AutoSignTask getAutoSignTask(BotManager botManager) {
        return new AutoSignTask(botManager);
    }
}
