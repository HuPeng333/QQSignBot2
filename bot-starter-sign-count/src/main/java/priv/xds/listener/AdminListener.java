package priv.xds.listener;

import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.message.results.GroupMemberInfo;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MatchType;
import org.apache.dubbo.config.annotation.DubboReference;
import priv.xds.annotation.CheckRole;
import priv.xds.exception.NoSuchUserException;
import priv.xds.exception.UnNecessaryInvokeException;
import priv.xds.pojo.Sign;
import priv.xds.service.SignService;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * @author DeSen Xu
 * @date 2021-10-17 21:46
 */
public class AdminListener {

    @DubboReference
    private SignService signService;

    @OnPrivate
    @Filter(value = "注册群组", matchType = MatchType.STARTS_WITH)
    @CheckRole(3)
    public void registerGroup(PrivateMsg privateMsg, MsgSender sender) {
        final int expectedKeyWordCount = 2;
        String[] keyWords = privateMsg.getText().split(" ");
        if (keyWords.length != expectedKeyWordCount) {
            sender.SENDER.sendPrivateMsg(privateMsg, "你输入的格式有误" +
                    "\n正确格式: '注册群组 [群号]'");
            return;
        }
        try {
        List<Sign> users = sender.GETTER.getGroupMemberList(keyWords[1]).stream().map(groupMemberInfo -> {
            Sign sign = new Sign();
            sign.setQq(groupMemberInfo.getAccountCode());
            sign.setGroupCode(groupMemberInfo.getGroupInfo().getGroupCode());
            sign.setLastSign(new Date());
            return sign;
        }).collect(Collectors.toList());
            int i = signService.initGroup(keyWords[1], users);
            sender.SENDER.sendPrivateMsg(privateMsg, "新注册了" + i + "个人员");
        } catch (UnNecessaryInvokeException e) {
            sender.SENDER.sendPrivateMsg(privateMsg, "该群内成员已经全部注册了!");
        } catch (NoSuchElementException e) {
            sender.SENDER.sendPrivateMsg(privateMsg, "没有找到该群组!请确保群号正确或者让机器人先进群!");
        }
    }

    @OnPrivate
    @Filter(value = "打卡忽略", matchType = MatchType.STARTS_WITH)
    @CheckRole(3)
    public void ignoreUser(PrivateMsg privateMsg, MsgSender sender) {
        final int expectedKeyWordCount = 3;
        String[] keyWords = privateMsg.getText().split(" ");
        if (keyWords.length != expectedKeyWordCount) {
            sender.SENDER.sendPrivateMsg(privateMsg, "你输入的格式有误" +
                    "\n正确格式: '打卡忽略 [群号] [目标用户qq]'");
            return;
        }
        try {
            GroupMemberInfo memberInfo = sender.GETTER.getMemberInfo(keyWords[1], keyWords[2]);
            signService.ignoreUser(memberInfo.getAccountCode(), memberInfo.getGroupInfo().getGroupCode());
            sender.SENDER.sendPrivateMsg(privateMsg, "成功忽略" + memberInfo.getAccountCode() + memberInfo.getAccountNicknameAndRemark() + "的打卡统计");
        } catch (NoSuchElementException e) {
            sender.SENDER.sendPrivateMsg(privateMsg, "没有找到qq群或者目标用户!请检查后重新输入");
        } catch (NoSuchUserException e) {
            sender.SENDER.sendPrivateMsg(privateMsg, "该用户在群内还没有注册!请先为其注册!");
        } catch (UnNecessaryInvokeException e) {
            sender.SENDER.sendPrivateMsg(privateMsg, "该用户已经被忽略了");
        }
    }

    @OnPrivate
    @Filter(value = "取消打卡忽略", matchType = MatchType.STARTS_WITH)
    @CheckRole(3)
    public void cancelIgnore(PrivateMsg privateMsg, MsgSender sender) {
        final int expectedKeyWordCount = 3;
        String[] keyWords = privateMsg.getText().split(" ");
        if (keyWords.length != expectedKeyWordCount) {
            sender.SENDER.sendPrivateMsg(privateMsg, "你输入的格式有误" +
                    "\n正确格式: '取消打卡忽略 [群号] [目标用户qq]'");
            return;
        }
        try {
            GroupMemberInfo memberInfo = sender.GETTER.getMemberInfo(keyWords[1], keyWords[2]);
            signService.reStatisticsUser(memberInfo.getAccountCode(), memberInfo.getGroupInfo().getGroupCode());
        } catch (NoSuchElementException e) {
            sender.SENDER.sendPrivateMsg(privateMsg, "没有找到qq群或者目标用户!请先让机器人进群或者检查你的输入");
        } catch (NoSuchUserException e) {
            sender.SENDER.sendPrivateMsg(privateMsg, "该用户在群内还没有注册!请先为其注册!");
        } catch (UnNecessaryInvokeException e) {
            sender.SENDER.sendPrivateMsg(privateMsg, "该用户没有被忽略");
        }
    }


}
