package priv.xds.config;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.bot.BotManager;
import org.apache.dubbo.config.annotation.DubboReference;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import priv.xds.service.SignService;
import priv.xds.util.GroupMessageUtil;

import java.util.List;

/**
 * @author DeSen Xu
 * @date 2021-10-17 16:54
 */
@Aspect
@Slf4j
public class CheckRole {

    private final List<String> admins;

    private final BotManager botManager;

    @DubboReference
    private SignService signService;

    public CheckRole(BotManager botManager, List<String> admins) {
        this.botManager = botManager;
        this.admins = admins;
    }

    @Pointcut("@annotation(priv.xds.annotation.CheckRole)")
    public void getAnnotation(){}

    @Around("getAnnotation() && @annotation(checkRole)")
    public Object before(ProceedingJoinPoint joinPoint, priv.xds.annotation.CheckRole checkRole) throws Throwable {
        GroupMsg groupMsg = null;
        PrivateMsg privateMsg = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof GroupMsg) {
                groupMsg = (GroupMsg) arg;
                break;
            }
            if (arg instanceof PrivateMsg) {
                privateMsg = (PrivateMsg) arg;
                break;
            }

        }
        if (groupMsg == null && privateMsg == null) {
            log.warn("在监听器的参数中无法找到GroupMsg或PrivateMsg的实现类,已拒绝该请求");
            return null;
        }
        int curRole = 1;
        if (groupMsg != null) {
            // groupMsg
            if (admins.contains(groupMsg.getAccountInfo().getAccountCode())) {
                curRole = 3;
            } else if (groupMsg.getPermission().isAdmin() || groupMsg.getPermission().isOwner()) {
                curRole = 2;
            } else {
                curRole = signService.getUserRole(groupMsg.getAccountInfo().getAccountCode(), groupMsg.getGroupInfo().getGroupCode());
            }
            // 检查权限
            if (curRole >= checkRole.value()) {
                // 权限足够
                return joinPoint.proceed();
            } else {
                // 权限不够
                botManager.getDefaultBot().getSender().SENDER.sendGroupMsg(groupMsg,
                        GroupMessageUtil.atSomeone(groupMsg.getAccountInfo().getAccountCode()) + "你的权限不够哦!");
                return null;
            }
        } else {
            // privateMsg
            if (admins.contains(privateMsg.getAccountInfo().getAccountCode())) {
                curRole = 3;
            }
            // 检查权限
            if (curRole >= checkRole.value()) {
                // 权限足够
                return joinPoint.proceed();
            } else {
                // 权限不够
                botManager.getDefaultBot().getSender().SENDER.sendPrivateMsg(privateMsg, "你的权限不够哦!");
                return null;
            }
        }


    }

}
