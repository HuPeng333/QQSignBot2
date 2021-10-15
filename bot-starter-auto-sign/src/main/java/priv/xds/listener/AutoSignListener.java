package priv.xds.listener;

import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MatchType;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import priv.xds.config.AutoSignAutoConfiguration;
import priv.xds.service.AutoSignService;

/**
 * @author DeSen Xu
 * @date 2021-10-15 22:31
 */
@Component
@ConditionalOnBean(AutoSignAutoConfiguration.class)
public class AutoSignListener {

    @DubboReference
    private AutoSignService autoSignService;


    /**
     * 获取自动打卡帮助
     */
    @OnPrivate
    @Filter(value = "自动打卡", trim = true, matchType = MatchType.EQUALS)
    public void registerAutoSign(PrivateMsg privateMsg, MsgSender msgSender) {
        msgSender.SENDER.sendPrivateMsg(privateMsg, "自动打卡可用功能:" +
                "\n注册自动打卡 token 学号 联系电话 家长姓名 家长联系电话" +
                "\n开启自动打卡" +
                "\n关闭自动打卡" +
                "\n修改 [相关数据] [新值] (例子: '修改 token xxx'; 请注意,也可以修改你要打卡的地点,格式: '修改 打卡地点 月球')" +
                "\n查看提交信息");
    }

}
