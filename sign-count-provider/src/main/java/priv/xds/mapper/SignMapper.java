package priv.xds.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import priv.xds.pojo.Sign;

import java.util.List;

/**
 * @author DeSen Xu
 * @date 2021-10-17 15:09
 */
@Component
public interface SignMapper extends BaseMapper<Sign> {

    String QQ_COLUMN = "qq";

    String LAST_SIGN_COLUMN = "last_sign";

    String GROUP_CODE_COLUMN = "group_code";

    String ROLE_COLUMN = "role";

    String SIGN_IGNORE_COLUMN = "sign_ignore";

    /**
     * 添加用户
     * @param users 用户列表
     */
    @Insert({"<script>",
            "insert into t_sign(qq, last_sign, group_code) ",
            "value ",
            "<foreach collection=\"users\" item=\"item\" separator=\",\">",
            "(#{item.qq}, #{item.lastSign}, #{item.groupCode})",
            "</foreach>",
            "</script>"
    })
    void insertUsers(@Param("users") List<Sign> users);

}
