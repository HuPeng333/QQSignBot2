package priv.xds.util;

import love.forte.simbot.api.message.events.GroupMsg;

/**
 * @author DeSen Xu
 * @date 2021-10-17 16:47
 */
public class GroupMessageUtil {


    /**
     * at一个人
     * @param qq qq
     * @return cat code
     */
    public static String atSomeone(String qq) {
        return "[CAT:at,code=" + qq + "]";
    }

    /**
     * at一个人
     * @param groupMsg groupMsg
     * @return cat code
     */
    public static String atSomeone(GroupMsg groupMsg) {
        return atSomeone(groupMsg.getAccountInfo().getAccountCode());
    }

    /**
     * at所有人
     * @return cat code
     */
    public static String atAll() {
        return "[CAT:at,all=true]";
    }
}
