package priv.xds.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import priv.xds.pojo.AutoSign;

import java.sql.Wrapper;
import java.util.List;

/**
 * @author DeSen Xu
 * @date 2021-10-14 14:30
 */
@Component
public interface AutoSignMapper extends BaseMapper<AutoSign> {

    /**
     * 获取token即将过期的用户
     * @return token即将过期的用户
     */
    @Select("select * from t_auto_sign where CURDATE() - token_submit_time = 6;")
    List<AutoSign> getWillExpiredUser();

    /**
     * 获取开启了自动打卡的用户,每次仅限5个
     * @return 开起了自动打卡的用户
     */
    @Select("select * from t_auto_sign where active = 1 and signed = 0 limit 5;")
    List<AutoSign> getAutoSignUsers();
}
