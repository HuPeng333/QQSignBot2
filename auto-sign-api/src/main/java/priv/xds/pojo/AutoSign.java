package priv.xds.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

/**
 * @author DeSen Xu
 * @date 2021-10-14 14:27
 */
@Data
@TableName("t_auto_sign")
public class AutoSign implements Serializable {

    private static final long serialVersionUID = 33333L;


    /**
     * 用户qq
     */
    @TableId(value = "qq")
    private String qq;

    /**
     * 保存的token
     */
    private String token;

    /**
     * 学号
     */
    private String yhm;

    /**
     * 学生联系电话
     */
    private String lxdh;

    /**
     * 家长姓名
     */
    private String jjlxr;

    /**
     * 家长联系电话
     */
    private String jjlxdh;

    /**
     * 要打卡的地点
     */
    private String location;

    /**
     * 是否开启自动打卡
     */
    private boolean active;

    /**
     * 上次自动打卡时间
     */
    private Date lastSign;

    /**
     * 提交token的时间
     * token有7天的有效期
     */
    private Date tokenSubmitTime;

    @Override
    public String toString() {
        return "已保存的信息:" +
                "\n学号: " + yhm +
                "\n联系电话: " + lxdh +
                "\n家长姓名: " + jjlxr +
                "\n家长联系电话: " + jjlxdh +
                "\n打卡地点: " + location +
                "\n开启状态: " + (active ? "开启中" : "关闭中") +
                "\n由于token过长,在此不显示!";
    }
}
