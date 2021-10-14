package priv.xds.exception;

/**
 * 执行了不必要的操作
 * @author DeSen Xu
 * @date 2021-10-14 15:04
 */
public class UnNecessaryInvokeException extends Exception{

    public UnNecessaryInvokeException() {}

    public UnNecessaryInvokeException(String message) {
        super(message);
    }

}
