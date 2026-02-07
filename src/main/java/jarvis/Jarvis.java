package jarvis;

import jarvis.exception.InvalidCommandException;
import jarvis.exception.InvalidFormatException;
import jarvis.exception.InvalidTaskNumberException;
import jarvis.exception.JarvisException;
import jarvis.parser.Parser;
import jarvis.storage.Storage;
import jarvis.task.Deadline;
import jarvis.task.Event;
import jarvis.task.Task;
import jarvis.task.TaskList;
import jarvis.task.Todo;
import jarvis.ui.Ui;

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
     * Creates a new Jarvis instance with the specified file path.
     *
     * @param filePath The path to the storage file.
     */
    public Jarvis(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
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
                case "find":
                    handleFind(input);
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

    private void handleFind(String input) throws JarvisException {
        String keyword = Parser.parseFind(input);
        TaskList matchingTasks = tasks.findTasks(keyword);
        ui.showFoundTasks(matchingTasks);
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
     * Generates a response for the user's input.
     *
     * @param input The user input.
     * @return The response string.
     */
    public String getResponse(String input) {
        String command = Parser.getCommand(input);

        try {
            switch (command) {
            case "bye":
                return "Goodbye! Hope to see you again soon!";
            case "list":
                return getListResponse();
            case "mark":
                return getMarkResponse(input);
            case "unmark":
                return getUnmarkResponse(input);
            case "delete":
                return getDeleteResponse(input);
            case "todo":
                return getTodoResponse(input);
            case "deadline":
                return getDeadlineResponse(input);
            case "event":
                return getEventResponse(input);
            case "find":
                return getFindResponse(input);
            case "":
                return "";
            default:
                throw new InvalidCommandException();
            }
        } catch (JarvisException e) {
            return "Error: " + e.getMessage();
        }
    }

    private String getListResponse() {
        if (tasks.size() == 0) {
            return "Your task list is empty!";
        }
        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(String.format("%d. %s\n", i + 1, tasks.get(i)));
        }
        return sb.toString().trim();
    }

    private String getMarkResponse(String input) throws JarvisException {
        int index = Parser.parseTaskIndex(input, 5, "mark");
        validateTaskIndex(index);
        tasks.get(index).markAsDone();
        storage.save(tasks);
        return "Nice! I've marked this task as done:\n  " + tasks.get(index);
    }

    private String getUnmarkResponse(String input) throws JarvisException {
        int index = Parser.parseTaskIndex(input, 7, "unmark");
        validateTaskIndex(index);
        tasks.get(index).markAsNotDone();
        storage.save(tasks);
        return "OK, I've marked this task as not done yet:\n  " + tasks.get(index);
    }

    private String getDeleteResponse(String input) throws JarvisException {
        int index = Parser.parseTaskIndex(input, 7, "delete");
        validateTaskIndex(index);
        Task removed = tasks.remove(index);
        storage.save(tasks);
        return String.format("Noted. I've removed this task:\n  %s\nNow you have %d tasks in the list.",
                removed, tasks.size());
    }

    private String getTodoResponse(String input) throws JarvisException {
        String description = Parser.parseTodo(input);
        Task task = new Todo(description);
        return addTaskAndGetResponse(task);
    }

    private String getDeadlineResponse(String input) throws JarvisException {
        String[] args = Parser.parseDeadline(input);
        Task task;
        try {
            task = new Deadline(args[0], args[1]);
        } catch (IllegalArgumentException e) {
            throw new InvalidFormatException("Invalid date format. Please use yyyy-MM-dd or yyyy-MM-dd HHmm "
                    + "(e.g., 2019-10-15 or 2019-10-15 1800)");
        }
        return addTaskAndGetResponse(task);
    }

    private String getEventResponse(String input) throws JarvisException {
        String[] args = Parser.parseEvent(input);
        Task task;
        try {
            task = new Event(args[0], args[1], args[2]);
        } catch (IllegalArgumentException e) {
            throw new InvalidFormatException("Invalid date format. Please use yyyy-MM-dd or yyyy-MM-dd HHmm "
                    + "(e.g., 2019-10-15 or 2019-10-15 1800)");
        }
        return addTaskAndGetResponse(task);
    }

    private String getFindResponse(String input) throws JarvisException {
        String keyword = Parser.parseFind(input);
        TaskList matchingTasks = tasks.findTasks(keyword);
        if (matchingTasks.size() == 0) {
            return "No matching tasks found.";
        }
        StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:\n");
        for (int i = 0; i < matchingTasks.size(); i++) {
            sb.append(String.format("%d. %s\n", i + 1, matchingTasks.get(i)));
        }
        return sb.toString().trim();
    }

    private String addTaskAndGetResponse(Task task) {
        tasks.add(task);
        storage.save(tasks);
        return String.format("Got it. I've added this task:\n  %s\nNow you have %d tasks in the list.",
                task, tasks.size());
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
