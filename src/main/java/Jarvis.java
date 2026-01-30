/**
 * Main class for the Jarvis task management application.
 */
public class Jarvis {
    private static final String FILE_PATH = "./data/jarvis.txt";

    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Creates a new Jarvis instance.
     */
    public Jarvis() {
        ui = new Ui();
        storage = new Storage(FILE_PATH);
        tasks = storage.load();
    }

    /**
     * Runs the main application loop.
     */
    public void run() {
        ui.showWelcome();

        boolean isRunning = true;
        while (isRunning) {
            String input = ui.readCommand();
            String command = Parser.getCommand(input);

            try {
                switch (command) {
                case "bye":
                    ui.showGoodbye();
                    isRunning = false;
                    break;
                case "list":
                    ui.showTaskList(tasks);
                    break;
                case "mark":
                    handleMark(input);
                    break;
                case "unmark":
                    handleUnmark(input);
                    break;
                case "delete":
                    handleDelete(input);
                    break;
                case "todo":
                    handleTodo(input);
                    break;
                case "deadline":
                    handleDeadline(input);
                    break;
                case "event":
                    handleEvent(input);
                    break;
                case "":
                    // Do nothing for empty input
                    break;
                default:
                    throw new InvalidCommandException();
                }
            } catch (JarvisException e) {
                ui.showError(e.getMessage());
            }
        }

        ui.close();
    }

    private void handleMark(String input) throws JarvisException {
        int index = Parser.parseTaskIndex(input, 5, "mark");
        validateTaskIndex(index);

        tasks.get(index).markAsDone();
        storage.save(tasks);
        ui.showTaskMarked(tasks.get(index));
    }

    private void handleUnmark(String input) throws JarvisException {
        int index = Parser.parseTaskIndex(input, 7, "unmark");
        validateTaskIndex(index);

        tasks.get(index).markAsNotDone();
        storage.save(tasks);
        ui.showTaskUnmarked(tasks.get(index));
    }

    private void handleDelete(String input) throws JarvisException {
        int index = Parser.parseTaskIndex(input, 7, "delete");
        validateTaskIndex(index);

        Task removed = tasks.remove(index);
        storage.save(tasks);
        ui.showTaskDeleted(removed, tasks.size());
    }

    private void handleTodo(String input) throws JarvisException {
        String description = Parser.parseTodo(input);
        Task task = new Todo(description);
        addTask(task);
    }

    private void handleDeadline(String input) throws JarvisException {
        String[] args = Parser.parseDeadline(input);
        Task task;
        try {
            task = new Deadline(args[0], args[1]);
        } catch (IllegalArgumentException e) {
            throw new InvalidFormatException("Invalid date format. Please use yyyy-MM-dd or yyyy-MM-dd HHmm "
                    + "(e.g., 2019-10-15 or 2019-10-15 1800)");
        }
        addTask(task);
    }

    private void handleEvent(String input) throws JarvisException {
        String[] args = Parser.parseEvent(input);
        Task task;
        try {
            task = new Event(args[0], args[1], args[2]);
        } catch (IllegalArgumentException e) {
            throw new InvalidFormatException("Invalid date format. Please use yyyy-MM-dd or yyyy-MM-dd HHmm "
                    + "(e.g., 2019-10-15 or 2019-10-15 1800)");
        }
        addTask(task);
    }

    private void addTask(Task task) {
        tasks.add(task);
        storage.save(tasks);
        ui.showTaskAdded(task, tasks.size());
    }

    private void validateTaskIndex(int index) throws JarvisException {
        if (index < 0 || index >= tasks.size()) {
            throw new InvalidTaskNumberException(index + 1, tasks.size());
        }
    }

    /**
     * Main entry point for the application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new Jarvis().run();
    }
}
