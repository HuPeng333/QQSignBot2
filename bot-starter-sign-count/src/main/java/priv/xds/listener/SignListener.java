package priv.xds.listener;

import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.results.GroupMemberInfo;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MatchType;
import org.apache.dubbo.config.annotation.DubboReference;
import priv.xds.annotation.CheckRole;
import priv.xds.exception.NoSuchUserException;
import priv.xds.exception.UnNecessaryInvokeException;
import priv.xds.pojo.Sign;
import priv.xds.service.SignService;
import priv.xds.util.GroupMessageUtil;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author DeSen Xu
 * @date 2021-10-17 16:29
 */
public class SignListener {

    @DubboReference
    private SignService signService;


    @OnGroup
    @Filter(value = "打卡", matchType = MatchType.EQUALS)
    public void sign(GroupMsg groupMsg, MsgSender sender) {
        String qq = groupMsg.getAccountInfo().getAccountCode();
        String groupCode = groupMsg.getGroupInfo().getGroupCode();
        try {
            signService.sign(qq, groupCode);
            sender.SENDER.sendGroupMsg(groupMsg, GroupMessageUtil.atSomeone(qq) + "打卡成功!");
        } catch (UnNecessaryInvokeException e) {
            sender.SENDER.sendGroupMsg(groupMsg, GroupMessageUtil.atSomeone(qq) + "你今天已经打卡了哦!");
        }
    }

    @OnGroup
    @Filter(value = "忽略", matchType = MatchType.STARTS_WITH)
    @CheckRole(2)
    public void ignoreUser(GroupMsg groupMsg, MsgSender sender) {
        final int expectedKeyWordCount = 2;
        String[] keyWords = groupMsg.getText().split(" ");
        if (keyWords.length != expectedKeyWordCount) {
            sender.SENDER.sendGroupMsg(groupMsg, GroupMessageUtil.atSomeone(groupMsg) + "你输入的格式有误" +
                    "\n正确格式: '忽略 [目标用户qq]'");
            return;
        }
        GroupMemberInfo memberInfo;
        try {
            memberInfo = sender.GETTER.getMemberInfo(groupMsg.getGroupInfo().getGroupCode(), keyWords[1]);
        } catch (NoSuchElementException e) {
            sender.SENDER.sendGroupMsg(groupMsg, GroupMessageUtil.atSomeone(groupMsg) + "没有找到该用户!");
            return;
        }

        try {
            signService.ignoreUser(memberInfo.getAccountCode(), memberInfo.getGroupInfo().getGroupCode());
            sender.SENDER.sendGroupMsg(groupMsg, "成功忽略对" + memberInfo.getAccountCode() + "(" + memberInfo.getAccountNicknameAndRemark() + ")的打卡统计!");
        } catch (UnNecessaryInvokeException e) {
            sender.SENDER.sendGroupMsg(groupMsg, GroupMessageUtil.atSomeone(groupMsg) + "该用户已经被忽略了!");
        } catch (NoSuchUserException e) {
            sender.SENDER.sendGroupMsg(groupMsg, GroupMessageUtil.atSomeone(groupMsg) + "数据库中没有找到该用户信息!" +
                    "\n你可以尝试发送'注册群组'来为其注册");
        }
    }

    @OnGroup
    @Filter(value = "取消忽略", matchType = MatchType.STARTS_WITH)
    public void cancelIgnore(GroupMsg groupMsg, MsgSender sender) {
        final int expectedKeyWordCount = 2;
        String[] keyWords = groupMsg.getText().split(" ");
        if (keyWords.length != expectedKeyWordCount) {
            sender.SENDER.sendGroupMsg(groupMsg, GroupMessageUtil.atSomeone(groupMsg) + "你输入的格式有误" +
                    "\n正确格式: '取消忽略 [目标用户qq]'");
            return;
        }
        GroupMemberInfo memberInfo;
        try {
            memberInfo = sender.GETTER.getMemberInfo(groupMsg.getGroupInfo().getGroupCode(), keyWords[1]);
        } catch (NoSuchElementException e) {
            sender.SENDER.sendGroupMsg(groupMsg, GroupMessageUtil.atSomeone(groupMsg) + "没有找到该用户!");
            return;
        }

        try {
            signService.reStatisticsUser(memberInfo.getAccountCode(), memberInfo.getGroupInfo().getGroupCode());
            sender.SENDER.sendGroupMsg(groupMsg, "已经重新开始统计" + memberInfo.getAccountCode() + "(" + memberInfo.getAccountNicknameAndRemark() + ")的打卡!");
        } catch (UnNecessaryInvokeException e) {
            sender.SENDER.sendGroupMsg(groupMsg, GroupMessageUtil.atSomeone(groupMsg) + "该用户没有被忽略!");
        } catch (NoSuchUserException e) {
            sender.SENDER.sendGroupMsg(groupMsg, GroupMessageUtil.atSomeone(groupMsg) + "数据库中没有找到该用户信息!" +
                    "\n你可以尝试发送'注册群组'来为其注册");
        }
    }

    @OnGroup
    @Filter(value = "打卡情况", matchType = MatchType.EQUALS)
    public void getUnsignedUsers(GroupMsg groupMsg, MsgSender sender) {
        List<Sign> unsignedUser = signService.getUnsignedUser(groupMsg.getGroupInfo().getGroupCode());
        if (unsignedUser == null) {
            sender.SENDER.sendGroupMsg(groupMsg, "所有人都已经打卡了!");
            return;
        }
        StringBuilder builder = new StringBuilder(unsignedUser.size() * 15);
        builder.append("以下成员还没有打卡:\n");
        for (Sign sign : unsignedUser) {
            GroupMemberInfo memberInfo = sender.GETTER.getMemberInfo(sign.getQq(), sign.getQq());
            builder.append(memberInfo.getAccountCode());
            builder.append(memberInfo.getAccountNicknameAndRemark());
            builder.append("\n");
        }
        sender.SENDER.sendGroupMsg(groupMsg, builder.toString());
    }

}
