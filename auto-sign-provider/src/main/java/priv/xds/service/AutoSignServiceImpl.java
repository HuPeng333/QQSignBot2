package priv.xds.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.sun.istack.internal.NotNull;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import priv.xds.exception.NoSuchUserException;
import priv.xds.exception.UnNecessaryInvokeException;
import priv.xds.mapper.AutoSignMapper;
import priv.xds.pojo.AutoSign;

import java.nio.charset.StandardCharsets;

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
    public void registerAutoSign(AutoSign autoSign) {
        autoSignMapper.insert(autoSign);
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
        AutoSign autoSign = autoSignMapper.selectById(qq.getBytes(StandardCharsets.UTF_8));
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
        AutoSign autoSign = autoSignMapper.selectById(qq.getBytes(StandardCharsets.UTF_8));
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


}
