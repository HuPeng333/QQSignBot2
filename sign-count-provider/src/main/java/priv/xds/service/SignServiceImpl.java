package priv.xds.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
import priv.xds.exception.NoSuchUserException;
import priv.xds.exception.UnNecessaryInvokeException;
import priv.xds.mapper.SignMapper;
import priv.xds.pojo.Sign;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author DeSen Xu
 * @date 2021-10-17 15:08
 */
@Service
@DubboService
public class SignServiceImpl implements SignService {

    private final SignMapper signMapper;

    public SignServiceImpl(SignMapper signMapper) {
        this.signMapper = signMapper;
    }

    @Override
    public void sign(String qq, String groupCode) throws UnNecessaryInvokeException {
        QueryWrapper<Sign> sql = new QueryWrapper<>();
        QueryWrapper<Sign> userSql = sql.eq(SignMapper.QQ_COLUMN, qq).eq(SignMapper.GROUP_CODE_COLUMN, groupCode);
        Sign user = signMapper.selectOne(userSql);
        if (user == null) {
            // 注册用户
            Sign newUser = new Sign();
            newUser.setQq(qq);
            newUser.setGroupCode(groupCode);
            newUser.setLastSign(new Date());
            signMapper.insert(newUser);
            return;
        }
        Instant now = Instant.now();
        Instant last = Instant.ofEpochMilli(user.getLastSign().getTime());
        long days = Duration.between(now, last).toDays();
        if (days == 0) {
            // 重复签到
            throw new UnNecessaryInvokeException();
        }
        // 更新
        user.setLastSign(new Date());
        signMapper.update(user, userSql);
    }

    @Override
    public int getUserRole(String qq, String groupCode) {
        QueryWrapper<Sign> sql = new QueryWrapper<>();
        return signMapper.selectOne(sql
                .select(SignMapper.ROLE_COLUMN)
                .eq(SignMapper.QQ_COLUMN, qq)
                .eq(SignMapper.GROUP_CODE_COLUMN, groupCode)
        ).getRole();
    }

    @Override
    public void ignoreUser(String qq, String groupCode) throws UnNecessaryInvokeException, NoSuchUserException {
        QueryWrapper<Sign> sql = new QueryWrapper<>();
        QueryWrapper<Sign> userSql = sql.eq(SignMapper.QQ_COLUMN, qq).eq(SignMapper.GROUP_CODE_COLUMN, groupCode);
        Sign user = signMapper.selectOne(userSql);
        if (user == null) {
            throw new NoSuchUserException();
        } else if (user.isSignIgnore()) {
            throw new UnNecessaryInvokeException();
        }
        // 忽略用户
        user.setSignIgnore(true);
        signMapper.update(user, userSql);
    }

    @Override
    public void reStatisticsUser(String qq, String groupCode) throws UnNecessaryInvokeException, NoSuchUserException {
        QueryWrapper<Sign> sql = new QueryWrapper<>();
        QueryWrapper<Sign> userSql = sql.eq(SignMapper.QQ_COLUMN, qq).eq(SignMapper.GROUP_CODE_COLUMN, groupCode);
        Sign user = signMapper.selectOne(userSql);
        if (user == null) {
            throw new NoSuchUserException();
        } else if (!user.isSignIgnore()) {
            throw new UnNecessaryInvokeException();
        }
        // 取消忽略用户
        user.setSignIgnore(false);
        signMapper.update(user, userSql);
    }

    @Override
    public int initGroup(String groupCode, List<Sign> groupMemberList) throws UnNecessaryInvokeException {
        QueryWrapper<Sign> sql = new QueryWrapper<>();
        List<Sign> signs = signMapper.selectList(sql.eq(SignMapper.GROUP_CODE_COLUMN, groupCode));
        List<Sign> noRepeatUsers = groupMemberList.stream().filter(sign -> !signs.contains(sign)).collect(Collectors.toList());
        if (noRepeatUsers.size() == 0) {
            throw new UnNecessaryInvokeException();
        }
        signMapper.insertUsers(noRepeatUsers);
        return noRepeatUsers.size();
    }

    @Override
    public List<Sign> getUnsignedUser() {
        QueryWrapper<Sign> sql = new QueryWrapper<>();
        List<Sign> signs = signMapper.selectList(sql.ne(SignMapper.LAST_SIGN_COLUMN, new Date()).eq(SignMapper.SIGN_IGNORE_COLUMN, false));
        if (signs.size() == 0) {
            return null;
        }
        return signs;
    }

    @Override
    public List<Sign> getUnsignedUser(String groupCode) {
        QueryWrapper<Sign> sql = new QueryWrapper<>();
        List<Sign> signs = signMapper.selectList(sql.ne(SignMapper.LAST_SIGN_COLUMN, new Date()).eq(SignMapper.SIGN_IGNORE_COLUMN, false).eq(SignMapper.GROUP_CODE_COLUMN, groupCode));
        if (signs.size() == 0) {
            return null;
        }
        return signs;
    }

    @Override
    public void deleteUser(String qq, String groupCode) {
        QueryWrapper<Sign> sql = new QueryWrapper<>();
        signMapper.delete(sql.eq(SignMapper.QQ_COLUMN, qq).eq(SignMapper.GROUP_CODE_COLUMN, groupCode));
    }
}
