package priv.xds.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author DeSen Xu
 * @date 2021-10-17 16:27
 */
@Data
@ConfigurationProperties(prefix = "bot.sign-count")
public class SignCountProperties {

    /**
     * 是否开启该功能
     */
    private boolean enabled;

    /**
     * 超级用户qq
     */
    private List<String> admins;

}
