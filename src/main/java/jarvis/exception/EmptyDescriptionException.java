package jarvis.exception;

/**
 * Exception thrown when a task description is empty.
 */
public class EmptyDescriptionException extends JarvisException {
    public EmptyDescriptionException(String taskType) {
        super("OOPS!!! The description of a " + taskType + " cannot be empty.");
    }
}
