package priv.xds.service;


import com.sun.istack.internal.NotNull;
import org.springframework.lang.Nullable;
import priv.xds.exception.NoSuchUserException;
import priv.xds.exception.UnNecessaryInvokeException;
import priv.xds.pojo.AutoSign;

/**
 * 测试接口
 * @author DeSen Xu
 * @date 2021-10-12 21:34
 */
public interface AutoSignService {

    /**
     * 注册自动打卡服务
     * @param autoSign 用户信息
     */
    void registerAutoSign(@NotNull AutoSign autoSign);

    /**
     * 获取用户保存在数据库的信息
     * @param qq qq号
     * @return 用户信息.<b>token的值将会被修改为null</b>
     */
    @Nullable
    AutoSign getSavedInfo(@NotNull String qq);

    /**
     * 修改保存在数据库的信息
     * @param autoSign 用户信息
     * @throws NoSuchUserException 没有找到该用户
     */
    void updateUserInfo(@NotNull AutoSign autoSign) throws NoSuchUserException;

    /**
     * 开启自动打卡
     * @param qq 用户qq
     * @throws UnNecessaryInvokeException 已经开启了自动打卡
     * @throws NoSuchUserException 该用户没有注册
     */
    void launchAutoSign(@NotNull String qq) throws UnNecessaryInvokeException, NoSuchUserException;

    /**
     * 关闭自动打卡
     * @param qq 用户qq
     * @throws UnNecessaryInvokeException 已经关闭了自动打卡
     * @throws NoSuchUserException 该用户没有注册
     */
    void stopAutoSign(@NotNull String qq) throws UnNecessaryInvokeException, NoSuchUserException;

}
