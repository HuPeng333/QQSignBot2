package priv.xds.config;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.bot.BotManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import priv.xds.listener.AdminListener;
import priv.xds.listener.GroupListener;
import priv.xds.listener.SignListener;
import priv.xds.task.TipSignTask;

import java.util.ArrayList;

/**
 * @author DeSen Xu
 * @date 2021-10-17 16:25
 */
@Configuration
@ConditionalOnProperty(prefix = "bot.sign-count", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(SignCountProperties.class)
@Slf4j
@EnableScheduling
public class SignCountAutoConfiguration {

    private SignCountProperties signCountProperties;

    @Autowired
    public void setSignCountProperties(SignCountProperties signCountProperties) {
        this.signCountProperties = signCountProperties;
    }

    @Bean
    public void init() {
        log.info("已开启打卡统计功能");
        // 防止该值为空
        if (signCountProperties.getAdmins() == null) {
            signCountProperties.setAdmins(new ArrayList<>(0));
        }
        if (signCountProperties.getAdmins().size() == 0) {
            log.info("没有设置超级用户");
        } else {
            log.info("打卡统计超级用户列表:" + signCountProperties.getAdmins());
        }
    }

    @Bean
    public SignListener getSignListener() {
        return new SignListener();
    }

    @Bean
    public CheckRole getCheckRole(BotManager botManager) {
        return new CheckRole(botManager, signCountProperties.getAdmins());
    }

    @Bean
    public AdminListener getAdminListener() {
        return new AdminListener();
    }

    @Bean
    public GroupListener getGroupListener() {
        return new GroupListener();
    }

    @Bean
    public TipSignTask getTipSignTask(BotManager botManager) {
        return new TipSignTask(botManager);
    }
}
