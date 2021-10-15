package priv.xds.aop;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.PrivateMsg;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import priv.xds.util.MessageUtil;

/**
 * 日志输出
 * @author DeSen Xu
 * @date 2021-10-15 22:48
 */
@Aspect
@Component
@Slf4j
public class Log {

    @After("execution(* priv.xds.listener.*.*(..)) && @annotation(love.forte.simbot.annotation.OnGroup)")
    public void groupMsgLog(JoinPoint joinPoint) {
        GroupMsg groupMsg = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof GroupMsg) {
                groupMsg = (GroupMsg) arg;
                break;
            }
        }
        if (groupMsg == null) {
            log.info(joinPoint.getSignature().getName() + "被调用了");
        } else {
            log.info("群" + MessageUtil.combineGroupIdAndName(groupMsg) + " 用户"+ MessageUtil.combineQqAndNickname(groupMsg) + "调用了: " + joinPoint.getSignature().getName() + "方法");
        }
    }

    @After("execution(* priv.xds.listener.*.*(..)) && @annotation(love.forte.simbot.annotation.OnPrivate)")
    public void privateMsgLog(JoinPoint joinPoint) {
        PrivateMsg privateMsg = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof PrivateMsg) {
                privateMsg = (PrivateMsg) arg;
                break;
            }
        }
        if (privateMsg == null) {
            log.info(joinPoint.getSignature().getName() + "被调用了");
        } else {
            log.info(" 用户"+ MessageUtil.combineQqAndNickname(privateMsg.getAccountInfo()) + "调用了: " + joinPoint.getSignature().getName() + "方法");
        }
    }



}
