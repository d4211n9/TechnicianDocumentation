package exceptions;

public class DALException extends Exception {

    public DALException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
