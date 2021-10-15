package priv.xds.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author DeSen Xu
 * @date 2021-10-13 21:50
 */
@ConfigurationProperties(prefix = "bot.auto-sign")
public class AutoSignProperties {

    /**
     * 是否开启自动打卡功能
     */
    private boolean enabled = false;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
