package priv.xds.task;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.api.message.results.SimpleGroupInfo;
import love.forte.simbot.api.sender.BotSender;
import love.forte.simbot.bot.BotManager;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.scheduling.annotation.Scheduled;
import priv.xds.pojo.Sign;
import priv.xds.service.SignService;
import priv.xds.util.GroupMessageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author DeSen Xu
 * @date 2021-10-18 12:53
 */
@Slf4j
public class TipSignTask {


    private final BotManager botManager;

    private final List<String> finishedList = new ArrayList<>(10);

    @DubboReference
    private SignService signService;

    public TipSignTask(BotManager botManager) {
        this.botManager = botManager;
    }

    @Scheduled(cron = "0 0 10,11,12 * * ?")
    public void tipUnsignedUser() {
        log.info("开始提醒未打卡的人");
        BotSender bot = botManager.getDefaultBot().getSender();
        List<Sign> allUnsignedUser = signService.getUnsignedUser();
        if (allUnsignedUser == null) {
            log.info("所有人都已经打卡了!");
            for (SimpleGroupInfo simpleGroupInfo : bot.GETTER.getGroupList().stream().collect(Collectors.toList())) {
                if (finishedList.contains(simpleGroupInfo.getGroupCode())) {
                    // 已经提醒过一遍了,不再继续提醒
                    continue;
                }

                try {
                    bot.SENDER.sendGroupMsg(simpleGroupInfo.getGroupCode(), "好耶,今天所有人都打卡了!");
                    finishedList.add(simpleGroupInfo.getGroupCode());
                } catch (IllegalStateException e){
                    log.error(e.toString());
                    // 因为机器人好友或者QQ群列表是以缓存的形式保存的,所以有可能得到的群不存在了
                }
            }
            return;
        }
        for (SimpleGroupInfo simpleGroupInfo : bot.GETTER.getGroupList().stream().collect(Collectors.toList())) {
            if (finishedList.contains(simpleGroupInfo.getGroupCode())) {
                // 已经提醒过一遍了,不再继续提醒
                continue;
            }
            try {
                String groupCode = simpleGroupInfo.getGroupCode();
                List<Sign> unsigned = allUnsignedUser.stream().filter(sign -> sign.getGroupCode().equals(groupCode)).collect(Collectors.toList());
                int size = unsigned.size();
                if (size == 0) {
                    log.info("群: " + groupCode + "全部打卡了");
                    bot.SENDER.sendGroupMsg(simpleGroupInfo.getGroupCode(), "好耶,今天所有人都打卡了!");
                    finishedList.add(simpleGroupInfo.getGroupCode());
                    continue;
                }
                log.info("群: " + groupCode + "有" + size + "个人没有打卡");

                StringBuilder builder = new StringBuilder(size * 20);
                for (Sign sign : unsigned) {
                    builder
                            .append(GroupMessageUtil.atSomeone(sign.getQq()))
                            .append(" ");
                }
                builder.append("记得打卡!");
                bot.SENDER.sendGroupMsg(simpleGroupInfo.getGroupCode(), builder.toString());
            } catch (IllegalStateException e){
                log.error(e.toString());
                // 因为机器人好友或者QQ群列表是以缓存的形式保存的,所以有可能得到的群不存在了
            }
        }
    }

    @Scheduled(cron = "10 0 0 * * ?")
    public void clearRank() {
        log.info("重置所有群组的打卡");
        finishedList.clear();
        BotSender bot = botManager.getDefaultBot().getSender();
        for (SimpleGroupInfo simpleGroupInfo : bot.GETTER.getGroupList().stream().collect(Collectors.toList())) {
            try {
                bot.SENDER.sendGroupMsg(simpleGroupInfo.getGroupCode(), "[CAT:at,all=true]打卡开始了!");
            } catch (IllegalStateException ignored) {}
        }
    }


}
