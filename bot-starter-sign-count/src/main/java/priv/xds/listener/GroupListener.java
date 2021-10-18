package priv.xds.listener;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.annotation.OnGroupMemberReduce;
import love.forte.simbot.api.message.events.GroupMemberReduce;
import org.apache.dubbo.config.annotation.DubboReference;
import priv.xds.service.SignService;

/**
 * @author DeSen Xu
 * @date 2021-10-17 22:11
 */
@Slf4j
public class GroupListener {

    @DubboReference
    private SignService signService;

    @OnGroupMemberReduce
    public void deleteLeftUser(GroupMemberReduce reduce) {
        log.info("由于退出了群聊,删除用户: " + reduce.getAccountInfo().getAccountCode() + "在群" + reduce.getGroupInfo().getGroupCode() + "的数据");
        signService.deleteUser(reduce.getAccountInfo().getAccountCode(), reduce.getGroupInfo().getGroupCode());
    }

}
