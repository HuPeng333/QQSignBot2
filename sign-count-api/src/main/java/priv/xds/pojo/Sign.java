package priv.xds.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

/**
 * @author DeSen Xu
 * @date 2021-10-16 22:36
 */
@Data
@TableName("t_sign")
public class Sign {

    private String qq;

    private Date lastSign;

    private String groupCode;

    private int role;

    private boolean signIgnore;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Sign sign = (Sign) o;

        if (!Objects.equals(qq, sign.qq)) {
            return false;
        }
        return Objects.equals(groupCode, sign.groupCode);
    }

    @Override
    public int hashCode() {
        int result = qq != null ? qq.hashCode() : 0;
        result = 31 * result + (groupCode != null ? groupCode.hashCode() : 0);
        return result;
    }
}
