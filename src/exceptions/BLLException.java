package exceptions;

public class BLLException extends Exception {

    public BLLException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
