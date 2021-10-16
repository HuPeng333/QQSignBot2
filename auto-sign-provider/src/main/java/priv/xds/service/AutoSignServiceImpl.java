package priv.xds.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import priv.xds.exception.NoSuchUserException;
import priv.xds.exception.UnNecessaryInvokeException;
import priv.xds.mapper.AutoSignMapper;
import priv.xds.pojo.AutoSign;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

/**
 * @author DeSen Xu
 * @date 2021-10-12 22:02
 */
@Service
@DubboService
public class AutoSignServiceImpl implements AutoSignService {

    private final AutoSignMapper autoSignMapper;


    @Autowired
    public AutoSignServiceImpl(AutoSignMapper autoSignMapper) {
        this.autoSignMapper = autoSignMapper;
    }

    @Override
    @SentinelResource
    public void registerAutoSign(AutoSign autoSign) throws UnNecessaryInvokeException{
        AutoSign user = autoSignMapper.selectById(autoSign.getQq());
        if (user != null) {
            // 已经注册
            throw new UnNecessaryInvokeException();
        } else {
            autoSignMapper.insert(autoSign);
        }
    }

    @Override
    @SentinelResource
    @Nullable
    public AutoSign getSavedInfo(String qq) {
        return autoSignMapper.selectById(qq);
    }

    @Override
    @SentinelResource
    public void updateUserInfo(AutoSign autoSign) throws NoSuchUserException {
        AutoSign user = autoSignMapper.selectById(autoSign.getQq());
        if (user == null) {
            throw new NoSuchUserException();
        }

        autoSignMapper.updateById(autoSign);
    }

    @Override
    @SentinelResource
    public void launchAutoSign(String qq) throws UnNecessaryInvokeException, NoSuchUserException {
        AutoSign autoSign = autoSignMapper.selectById(qq);
        if (autoSign == null) {
            throw new NoSuchUserException();
        } else if (autoSign.isActive()) {
            throw new UnNecessaryInvokeException();
        }
        AutoSign temp = new AutoSign();
        temp.setQq(qq);
        temp.setActive(true);
        // update
        autoSignMapper.updateById(temp);
    }

    @Override
    @SentinelResource
    public void stopAutoSign(String qq) throws UnNecessaryInvokeException, NoSuchUserException {
        AutoSign autoSign = autoSignMapper.selectById(qq);
        if (autoSign == null) {
            throw new NoSuchUserException();
        } else if (!autoSign.isActive()) {
            throw new UnNecessaryInvokeException();
        }
        AutoSign temp = new AutoSign();
        temp.setQq(qq);
        temp.setActive(false);
        // update
        autoSignMapper.updateById(temp);
    }

    @Override
    public List<AutoSign> getWillExpiredUser() {
        List<AutoSign> willExpiredUser = autoSignMapper.getWillExpiredUser();
        return willExpiredUser.size() == 0 ? null : willExpiredUser;
    }

    @Override
    public List<AutoSign> getActiveUser() {
        List<AutoSign> autoSignUsers = autoSignMapper.getAutoSignUsers();
        return autoSignUsers.size() == 0 ? null : autoSignUsers;
    }
}
