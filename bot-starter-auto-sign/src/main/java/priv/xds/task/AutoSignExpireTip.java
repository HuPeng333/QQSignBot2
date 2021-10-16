package priv.xds.task;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.bot.BotManager;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.scheduling.annotation.Scheduled;
import priv.xds.pojo.AutoSign;
import priv.xds.service.AutoSignService;

import java.util.List;

/**
 * token即将过期时的提醒
 * @author DeSen Xu
 * @date 2021-10-16 16:58
 */
@Slf4j
public class AutoSignExpireTip {

    @DubboReference
    private AutoSignService autoSignService;

    private final BotManager botManager;

    public AutoSignExpireTip(BotManager botManager) {
        this.botManager = botManager;
    }

    /**
     * 每天8点,提醒即将过期的用户
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public void tipExpire() {
        log.info("正在提醒token即将过期的用户");
        List<AutoSign> willExpiredUser = autoSignService.getWillExpiredUser();
        if (willExpiredUser == null) {
            log.info("没有即将过期的用户");
            return;
        }
        Sender sender = botManager.getDefaultBot().getSender().SENDER;
        willExpiredUser.forEach(autoSign -> {
            log.info("用户: " + autoSign.getQq() + "的token即将过期");
            sender.sendPrivateMsg(autoSign.getQq(), "您提交的token将会在明天过期,请注意及时更新!");
        });
        log.info("提醒完毕");
    }
}
