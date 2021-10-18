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

    String LAST_SIGN_COLUMN = "last_sign";

    String ACTIVE_COLUMN = "active";


    /**
     * 获取token即将过期的用户
     * @return token即将过期的用户
     */
    @Select("select * from t_auto_sign where CURDATE() - token_submit_time = 6;")
    List<AutoSign> getWillExpiredUser();

}
