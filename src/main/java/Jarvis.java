import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main class for the Jarvis task management application.
 */
public class Jarvis {
    private static final String FILE_PATH = "./data/jarvis.txt";
    private static ArrayList<Task> tasks = new ArrayList<>();
    private static Storage storage = new Storage(FILE_PATH);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        tasks = storage.load();

        System.out.println("____________________________________________________________");
        System.out.println("Hello Master! I'm Jarvis your personal assistance!");
        System.out.println("How may I serve you?");
        System.out.println("____________________________________________________________");

        String input = "";
        while (!input.equals("bye")) {
            input = scanner.nextLine().trim();

            try {
                if (input.equals("bye")) {
                    System.out.println("____________________________________________________________");
                    System.out.println("Goodbye Master. Hope to see you again soon!");
                    System.out.println("____________________________________________________________");
                } else if (input.equals("list")) {
                    handleList();
                } else if (input.startsWith("mark ")) {
                    handleMark(input);
                } else if (input.startsWith("unmark ")) {
                    handleUnmark(input);
                } else if (input.startsWith("delete ")) {
                    handleDelete(input);
                } else if (input.startsWith("todo ")) {
                    handleTodo(input);
                } else if (input.startsWith("deadline ")) {
                    handleDeadline(input);
                } else if (input.startsWith("event ")) {
                    handleEvent(input);
                } else if (input.isEmpty()) {
                    // Do nothing for empty input
                } else {
                    throw new InvalidCommandException();
                }
            } catch (JarvisException e) {
                System.out.println("____________________________________________________________");
                System.out.println(e.getMessage());
                System.out.println("____________________________________________________________");
            }
        }

        scanner.close();
    }

    private static void handleList() {
        System.out.println("____________________________________________________________");
        System.out.println("Here are the tasks in your archive:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        System.out.println("____________________________________________________________");
    }

    private static void handleMark(String input) throws JarvisException {
        String indexStr = input.substring(5).trim();

        if (indexStr.isEmpty()) {
            throw new MissingArgumentException("which task to mark", "mark [task number]");
        }

        try {
            int taskIndex = Integer.parseInt(indexStr) - 1;

            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                throw new InvalidTaskNumberException(taskIndex + 1, tasks.size());
            }

            tasks.get(taskIndex).markAsDone();
            storage.save(tasks);
            System.out.println("____________________________________________________________");
            System.out.println("Nice! I've marked this task as completed:");
            System.out.println("  " + tasks.get(taskIndex));
            System.out.println("____________________________________________________________");
        } catch (NumberFormatException e) {
            throw new InvalidTaskNumberException("Please provide a valid task number. Usage: mark [task number]");
        }
    }

    private static void handleUnmark(String input) throws JarvisException {
        String indexStr = input.substring(7).trim();

        if (indexStr.isEmpty()) {
            throw new MissingArgumentException("which task to unmark", "unmark [task number]");
        }

        try {
            int taskIndex = Integer.parseInt(indexStr) - 1;

            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                throw new InvalidTaskNumberException(taskIndex + 1, tasks.size());
            }

            tasks.get(taskIndex).markAsNotDone();
            storage.save(tasks);
            System.out.println("____________________________________________________________");
            System.out.println("OK, I've revert the task completion");
            System.out.println("  " + tasks.get(taskIndex));
            System.out.println("____________________________________________________________");
        } catch (NumberFormatException e) {
            throw new InvalidTaskNumberException("Please provide a valid task number. Usage: unmark [task number]");
        }
    }

    private static void handleDelete(String input) throws JarvisException {
        String indexStr = input.substring(7).trim();

        if (indexStr.isEmpty()) {
            throw new MissingArgumentException("which task to delete", "delete [task number]");
        }

        try {
            int taskIndex = Integer.parseInt(indexStr) - 1;

            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                throw new InvalidTaskNumberException(taskIndex + 1, tasks.size());
            }

            Task removedTask = tasks.remove(taskIndex);
            storage.save(tasks);
            System.out.println("____________________________________________________________");
            System.out.println("Noted. I've removed this task:");
            System.out.println("  " + removedTask);
            System.out.println("Now you have " + tasks.size() + " tasks in the list.");
            System.out.println("____________________________________________________________");
        } catch (NumberFormatException e) {
            throw new InvalidTaskNumberException("Please provide a valid task number. Usage: delete [task number]");
        }
    }

    private static void handleTodo(String input) throws JarvisException {
        String description = input.substring(5).trim();

        if (description.isEmpty()) {
            throw new EmptyDescriptionException("todo");
        }

        Task newTask = new Todo(description);
        tasks.add(newTask);
        storage.save(tasks);
        System.out.println("____________________________________________________________");
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + newTask);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }

    private static void handleDeadline(String input) throws JarvisException {
        String details = input.substring(9).trim();

        if (details.isEmpty()) {
            throw new EmptyDescriptionException("deadline");
        }

        if (!details.contains(" /by ")) {
            throw new InvalidFormatException("Please specify the deadline using '/by'. Usage: deadline [description] /by [date]");
        }

        String[] parts = details.split(" /by ", 2);
        String description = parts[0].trim();
        String by = parts.length > 1 ? parts[1].trim() : "";

        if (description.isEmpty()) {
            throw new EmptyDescriptionException("deadline");
        }

        if (by.isEmpty()) {
            throw new MissingArgumentException("when the task is due", "deadline [description] /by [date]");
        }

        Task newTask;
        try {
            newTask = new Deadline(description, by);
        } catch (IllegalArgumentException e) {
            throw new InvalidFormatException("Invalid date format. Please use yyyy-MM-dd or yyyy-MM-dd HHmm "
                    + "(e.g., 2019-10-15 or 2019-10-15 1800)");
        }
        tasks.add(newTask);
        storage.save(tasks);
        System.out.println("____________________________________________________________");
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + newTask);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }

    private static void handleEvent(String input) throws JarvisException {
        String details = input.substring(6).trim();

        if (details.isEmpty()) {
            throw new EmptyDescriptionException("event");
        }

        if (!details.contains(" /from ") || !details.contains(" /to ")) {
            throw new InvalidFormatException("Please specify the event time using '/from' and '/to'. Usage: event [description] /from [start] /to [end]");
        }

        String[] parts = details.split(" /from | /to ");

        if (parts.length < 3) {
            throw new InvalidFormatException("Please provide both start and end times. Usage: event [description] /from [start] /to [end]");
        }

        String description = parts[0].trim();
        String from = parts[1].trim();
        String to = parts[2].trim();

        if (description.isEmpty()) {
            throw new EmptyDescriptionException("event");
        }

        if (from.isEmpty()) {
            throw new MissingArgumentException("when the event starts", "event [description] /from [start] /to [end]");
        }

        if (to.isEmpty()) {
            throw new MissingArgumentException("when the event ends", "event [description] /from [start] /to [end]");
        }

        Task newTask;
        try {
            newTask = new Event(description, from, to);
        } catch (IllegalArgumentException e) {
            throw new InvalidFormatException("Invalid date format. Please use yyyy-MM-dd or yyyy-MM-dd HHmm "
                    + "(e.g., 2019-10-15 or 2019-10-15 1800)");
        }
        tasks.add(newTask);
        storage.save(tasks);
        System.out.println("____________________________________________________________");
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + newTask);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }
}
