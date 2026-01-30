package jarvis.exception;

/**
 * Exception thrown when an unrecognized command is entered.
 */
public class InvalidCommandException extends JarvisException {
    public InvalidCommandException() {
        super("OOPS!!! I'm sorry Master, but I don't know what that means :-(");
    }
}
