package priv.xds.service;

import org.springframework.lang.Nullable;
import priv.xds.exception.NoRepeatableException;
import priv.xds.exception.NoSuchUserException;
import priv.xds.exception.UnNecessaryInvokeException;
import priv.xds.pojo.Sign;
import java.util.List;

/**
 * 打卡服务
 * @author DeSen Xu
 * @date 2021-10-16 22:28
 */
public interface SignService {

    /**
     * 打卡,不管用户加了几个群,全部都会为其打卡
     * @param qq 用户qq
     * @param groupCode 群号
     * @throws UnNecessaryInvokeException 用户重复签到
     */
    void sign(String qq, String groupCode) throws UnNecessaryInvokeException;

    /**
     * 查询用户权限等级
     * @param qq 用户qq号
     * @param groupCode 群组
     * @return 1:普通用户, 2:管理员, 3:超级用户
     */
    int getUserRole(String qq, String groupCode);


    /**
     * 忽略某个用户的打卡情况
     * @param qq qq号
     * @param groupCode 群号
     * @throws UnNecessaryInvokeException 用户已经被忽略
     * @throws NoSuchUserException 没有找到该用户
     */
    void ignoreUser(String qq, String groupCode) throws UnNecessaryInvokeException, NoSuchUserException;

    /**
     * 重新统计某个用户的打卡情况
     * @param qq qq号
     * @param groupCode 群号
     * @throws UnNecessaryInvokeException 用户没有被忽略
     * @throws NoSuchUserException 没有找到该用户
     */
    void reStatisticsUser(String qq, String groupCode) throws UnNecessaryInvokeException, NoSuchUserException;


    /**
     * 注册成员
     * @param groupCode 群号
     * @param groupMemberList 群内的人员
     * @throws UnNecessaryInvokeException 已经全部添加了
     * @return 添加了多少人
     */
    int initGroup(String groupCode, List<Sign> groupMemberList) throws UnNecessaryInvokeException;

    /**
     * 获取未打卡的成员
     * @return 未打卡成员
     */
    @Nullable
    List<Sign> getUnsignedUser() ;

    /**
     * 获取某个群内没有打卡的成员
     * @param groupCode 群号
     * @return 未打卡成员
     */
    @Nullable
    List<Sign> getUnsignedUser(String groupCode);

    /**
     * 删除用户在数据库的信息
     * @param qq qq号
     * @param groupCode 群号
     */
    void deleteUser(String qq, String groupCode);

}
