package jarvis.exception;

/**
 * Exception thrown when a command has an invalid format.
 */
public class InvalidFormatException extends JarvisException {
    public InvalidFormatException(String message) {
        super("OOPS!!! " + message);
    }
}
