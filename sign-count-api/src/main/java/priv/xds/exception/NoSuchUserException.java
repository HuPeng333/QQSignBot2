package priv.xds.exception;

/**
 * 没有找到指定用户
 * @author DeSen Xu
 * @date 2021-10-14 15:12
 */
public class NoSuchUserException extends Exception{

    public NoSuchUserException() {
    }

    public NoSuchUserException(String message) {
        super(message);
    }
}
