public class MissingArgumentException extends JarvisException {
    public MissingArgumentException(String command, String usage) {
        super("OOPS!!! Please specify " + command + ". Usage: " + usage);
    }
}