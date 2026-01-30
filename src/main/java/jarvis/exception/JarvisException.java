package jarvis.exception;

/**
 * Base exception class for all Jarvis-specific exceptions.
 */
public class JarvisException extends Exception {
    public JarvisException(String message) {
        super(message);
    }
}
