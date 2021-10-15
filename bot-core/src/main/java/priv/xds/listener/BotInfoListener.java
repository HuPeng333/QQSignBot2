package priv.xds.listener;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MatchType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author DeSen Xu
 * @date 2021-10-15 21:48
 */
@Component
@Slf4j
public class BotInfoListener {

    /**
     * 当前机器人的版本
     */
    @Value("${app.version}")
    private String version;

    @OnGroup
    @Filter(value = "^\\s*", atBot = true, matchType = MatchType.REGEX_MATCHES)
    public void botInfoToGroup(GroupMsg groupMsg, MsgSender sender) {
        sender.SENDER.sendGroupMsg(groupMsg, "查看更新详细信息请前往github: https://github.com/HuPeng333/QQSignBot2\n当前版本:" + version);
    }

    @OnPrivate
    @Filter(value = "帮助", matchType = MatchType.CONTAINS)
    public void botInfoToPrivate(PrivateMsg privateMsg, MsgSender sender) {
        sender.SENDER.sendPrivateMsg(privateMsg, "详细帮助请前往github查看: https://github.com/HuPeng333/QQSignBot2" +
                "\n当前版本: " + version);
    }

}
