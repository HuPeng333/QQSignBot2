package priv.xds.listener;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MatchType;
import org.apache.dubbo.config.annotation.DubboReference;
import priv.xds.exception.NoSuchUserException;
import priv.xds.exception.UnNecessaryInvokeException;
import priv.xds.pojo.AutoSign;
import priv.xds.service.AutoSignService;
import priv.xds.util.AutoSignPojoUtil;

import java.util.Arrays;
import java.util.Date;

/**
 * @author DeSen Xu
 * @date 2021-10-15 22:31
 */
@Slf4j
public class AutoSignListener {

    @DubboReference
    private AutoSignService autoSignService;


    /**
     * 获取自动打卡帮助
     */
    @OnPrivate
    @Filter(value = "自动打卡", trim = true, matchType = MatchType.EQUALS)
    public void getHelp(PrivateMsg privateMsg, MsgSender msgSender) {
        log.debug("获取了帮助");
        msgSender.SENDER.sendPrivateMsg(privateMsg, "自动打卡可用功能:" +
                "\n-注册自动打卡 token 学号 联系电话 家长姓名 家长联系电话" +
                "\n-开启自动打卡" +
                "\n-关闭自动打卡" +
                "\n-修改 [相关数据] [新值] (例子: '修改 token xxx'; 请注意,也可以修改你要打卡的地点,格式: '修改 打卡地点 月球')" +
                "\n-查看提交信息");
    }

    @OnPrivate
    @Filter(value = "注册自动打卡", matchType = MatchType.STARTS_WITH)
    public void registerAutoSign(PrivateMsg privateMsg, MsgSender msgSender) {
        log.debug("注册了自动打卡");
        // 期望有几个关键字
        final int expectedKeyWordCount = 6;
        String[] keyWords = privateMsg.getText().split(" ");
        if (keyWords.length != expectedKeyWordCount) {
            // 缺少关键字
            msgSender.SENDER.sendPrivateMsg(privateMsg, "格式有误!\n正确格式: '注册自动打卡 token 学号 联系电话 家长姓名 家长联系电话'");
            return;
        }
        // 注册
        AutoSign autoSign = new AutoSign();
        autoSign.setQq(privateMsg.getAccountInfo().getAccountCode());
        autoSign.setToken(keyWords[1]);
        autoSign.setYhm(keyWords[2]);
        autoSign.setLxdh(keyWords[3]);
        autoSign.setJjlxr(keyWords[4]);
        autoSign.setJjlxdh(keyWords[5]);
        autoSign.setTokenSubmitTime(new Date());
        try {
            autoSignService.registerAutoSign(autoSign);
            msgSender.SENDER.sendPrivateMsg(privateMsg, "注册成功!你可以输入'开启自动打卡'来启用该功能");
        } catch (UnNecessaryInvokeException e) {
            msgSender.SENDER.sendPrivateMsg(privateMsg, "注册失败!您已经注册或者格式有误!");
        }
    }

    @OnPrivate
    @Filter(value = "开启自动打卡", matchType = MatchType.EQUALS)
    public void enableAutoSign(PrivateMsg privateMsg, MsgSender msgSender) {
        try {
            autoSignService.launchAutoSign(privateMsg.getAccountInfo().getAccountCode());
            msgSender.SENDER.sendPrivateMsg(privateMsg, "开启成功!");
        } catch (UnNecessaryInvokeException e) {
            msgSender.SENDER.sendPrivateMsg(privateMsg, "你早已经开启了该功能!");
        } catch (NoSuchUserException e) {
            msgSender.SENDER.sendPrivateMsg(privateMsg, "你还没有注册,请先注册!输入'自动打卡'获取更多帮助");
        }
    }

    @OnPrivate
    @Filter(value = "关闭自动打卡", matchType = MatchType.EQUALS)
    public void disableAutoSign(PrivateMsg privateMsg, MsgSender msgSender) {
        try {
            autoSignService.stopAutoSign(privateMsg.getAccountInfo().getAccountCode());
            msgSender.SENDER.sendPrivateMsg(privateMsg, "关闭成功!");
        } catch (NoSuchUserException e) {
            msgSender.SENDER.sendPrivateMsg(privateMsg, "你还没有注册,请先注册!输入'自动打卡'获取更多帮助");
        } catch (UnNecessaryInvokeException e) {
            msgSender.SENDER.sendPrivateMsg(privateMsg, "你早已经关闭了该功能!");
        }
    }

    @OnPrivate
    @Filter(value = "修改", matchType = MatchType.STARTS_WITH)
    public void modifyAutoSign(PrivateMsg privateMsg, MsgSender msgSender) {
        String[] keyWords = privateMsg.getText().split(" ");
        AutoSign autoSign = new AutoSign();
        autoSign.setQq(privateMsg.getAccountInfo().getAccountCode());
        try {
            AutoSignPojoUtil.putAttributeToAutoSign(keyWords[1], keyWords[2], autoSign);
            autoSignService.updateUserInfo(autoSign);
            msgSender.SENDER.sendPrivateMsg(privateMsg, "信息修改成功!");
        } catch (IllegalAccessException e) {
            log.error(e.toString());
            msgSender.SENDER.sendPrivateMsg(privateMsg, "服务器错误,正在加紧修复中!");
        } catch (NoSuchUserException e) {
            msgSender.SENDER.sendPrivateMsg(privateMsg, "你还没有注册,请先注册!输入'自动打卡'获取更多帮助");
        } catch (NoSuchFieldException e) {
            msgSender.SENDER.sendPrivateMsg(privateMsg, "给定的相关字段名不存在或不可用!\n可用字段: token, 学号, 联系电话, 家长姓名, 家长联系电话, 打卡地点");
        }
    }

    @OnPrivate
    @Filter(value = "查看提交信息", matchType = MatchType.EQUALS)
    public void getAutoSignInfo(PrivateMsg privateMsg, MsgSender msgSender) {
        AutoSign savedInfo = autoSignService.getSavedInfo(privateMsg.getAccountInfo().getAccountCode());
        if (savedInfo == null) {
            msgSender.SENDER.sendPrivateMsg(privateMsg, "你还没有注册,请先注册!输入'自动打卡'获取更多帮助");
            return;
        }
        msgSender.SENDER.sendPrivateMsg(privateMsg, savedInfo.toString());
    }

}
