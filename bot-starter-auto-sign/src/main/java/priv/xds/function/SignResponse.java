package priv.xds.function;


/**
 * 自动打卡请求的响应
 * @author DeSen Xu
 * @date 2021-10-16 17:07
 */
public class SignResponse {

    /**
     * 帮助打卡的用户qq
     */
    private String user;

    private String message;

    private String errorCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    /**
     * 判断当前请求是否成功
     * @return 返回true表示成功
     */
    public boolean isSuccess() {
        final String successCode = "0x00000000";
        return successCode.equals(errorCode);
    }

}
