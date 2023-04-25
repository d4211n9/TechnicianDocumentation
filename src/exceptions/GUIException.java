package exceptions;

public class GUIException extends Exception {

    public GUIException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
