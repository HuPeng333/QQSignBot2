package priv.xds.task;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.bot.BotManager;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.scheduling.annotation.Scheduled;
import priv.xds.exception.NoSuchUserException;
import priv.xds.exception.UnNecessaryInvokeException;
import priv.xds.function.SignHelper;
import priv.xds.function.SignResponse;
import priv.xds.pojo.AutoSign;
import priv.xds.service.AutoSignService;

import java.util.Date;
import java.util.List;

/**
 * @author DeSen Xu
 * @date 2021-10-16 17:13
 */
@Slf4j
public class AutoSignTask {

    private final BotManager botManager;

    public AutoSignTask(BotManager botManager) {
        this.botManager = botManager;
    }

    @DubboReference
    private AutoSignService autoSignService;


    /**
     * 是否已经全部打卡完毕
     */
    private boolean isAllDone = false;

    @Scheduled(cron = "0 0,20,40 1,2,3,4,5 * * ? ")
    public void helpToSign() {
        if (isAllDone) {
            return;
        }
        log.info("正在帮助自动打卡");
        List<AutoSign> autoSignUsers = autoSignService.getActiveUser();
        if (autoSignUsers == null) {
            log.info("所有用户都已经自动打卡了");
            isAllDone = true;
            return;
        }
        Sender sender = botManager.getDefaultBot().getSender().SENDER;
        autoSignUsers.forEach(autoSign -> {
            SignResponse resp = new SignHelper(autoSign).send();
            log.info("帮助用户: " + autoSign.getQq() + "打卡," + "结果: " + resp.getMessage());
            if (resp.isSuccess()) {
                sender.sendPrivateMsg(resp.getUser(), "已经帮你自动打卡了");
                // 更新上次自动打卡时间,防止连续打卡
                autoSignService.updateLastSignTime(autoSign.getQq());
            } else {
                sender.sendPrivateMsg(resp.getUser(), "自动打卡失败: " + resp.getMessage() + "\n已经帮你关闭自动打卡了!");
                try {
                    // 停止自动打卡
                    autoSignService.stopAutoSign(autoSign.getQq());
                } catch (UnNecessaryInvokeException | NoSuchUserException ignored) {
                    // ignored
                }
            }
        });
        log.info("自动打卡完毕!");
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void reset() {
        log.info("重置了自动打卡服务");
        isAllDone = true;
    }

}
