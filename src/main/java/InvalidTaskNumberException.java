public class InvalidTaskNumberException extends JarvisException {
    public InvalidTaskNumberException(int taskNumber, int totalTasks) {
        super("OOPS!!! Task number " + taskNumber + " does not exist. You have " + totalTasks + " tasks.");
    }

    public InvalidTaskNumberException(String message) {
        super(message);
    }
}
