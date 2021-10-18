package priv.xds.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import priv.xds.exception.NoSuchUserException;
import priv.xds.exception.UnNecessaryInvokeException;
import priv.xds.mapper.AutoSignMapper;
import priv.xds.pojo.AutoSign;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
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
    public void updateLastSignTime(String qq) {
        AutoSign autoSign = new AutoSign();
        autoSign.setQq(qq);
        autoSign.setLastSign(new java.util.Date());
        try {
            updateUserInfo(autoSign);
        } catch (NoSuchUserException ignored) {
        }
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
        QueryWrapper<AutoSign> sql = new QueryWrapper<>();
        List<AutoSign> autoSigns = autoSignMapper.selectList(sql.ne(AutoSignMapper.LAST_SIGN_COLUMN, new Date(System.currentTimeMillis())).eq(AutoSignMapper.ACTIVE_COLUMN, true));
        if (autoSigns.size() == 0) {
            return null;
        }
        return autoSigns;
    }
}
