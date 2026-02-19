package jarvis.ui;

import java.util.Scanner;

import jarvis.task.Task;
import jarvis.task.TaskList;

/**
 * Handles all user interface interactions.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";
    private Scanner scanner;

    /**
     * Creates a new Ui instance.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads the next command from user input.
     *
     * @return The user's input string.
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Displays the welcome message.
     */
    public void showWelcome() {
        showLine();
        System.out.println("Good day, Master. I am Jarvis, your devoted butler.");
        System.out.println("How may I be of service to you today?");
        showLine();
    }

    /**
     * Displays the goodbye message.
     */
    public void showGoodbye() {
        showLine();
        System.out.println("Farewell, Master. It has been an honour to serve you. Until next time.");
        showLine();
    }

    /**
     * Displays a horizontal line separator.
     */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Displays an error message.
     *
     * @param message The error message to display.
     */
    public void showError(String message) {
        showLine();
        System.out.println(message);
        showLine();
    }

    /**
     * Displays a general message.
     *
     * @param message The message to display.
     */
    public void showMessage(String message) {
        showLine();
        System.out.println(message);
        showLine();
    }

    /**
     * Displays a message indicating a task was added.
     *
     * @param task The task that was added.
     * @param totalTasks The total number of tasks after adding.
     */
    public void showTaskAdded(Task task, int totalTasks) {
        showLine();
        System.out.println("Very well, Master. I have added this task to your list:");
        System.out.println("  " + task);
        System.out.println("You now have " + totalTasks + " tasks in your schedule, Master.");
        showLine();
    }

    /**
     * Displays a message indicating a task was deleted.
     *
     * @param task The task that was deleted.
     * @param totalTasks The total number of tasks after deletion.
     */
    public void showTaskDeleted(Task task, int totalTasks) {
        showLine();
        System.out.println("As you wish, Master. I have removed this task:");
        System.out.println("  " + task);
        System.out.println("You now have " + totalTasks + " tasks remaining, Master.");
        showLine();
    }

    /**
     * Displays a message indicating a task was marked as done.
     *
     * @param task The task that was marked.
     */
    public void showTaskMarked(Task task) {
        showLine();
        System.out.println("Splendid, Master. I have marked this task as completed:");
        System.out.println("  " + task);
        showLine();
    }

    /**
     * Displays a message indicating a task was unmarked.
     *
     * @param task The task that was unmarked.
     */
    public void showTaskUnmarked(Task task) {
        showLine();
        System.out.println("Understood, Master. I have reverted the task status:");
        System.out.println("  " + task);
        showLine();
    }

    /**
     * Displays all tasks in the task list.
     *
     * @param tasks The task list to display.
     */
    public void showTaskList(TaskList tasks) {
        showLine();
        System.out.println("Here are the tasks in your schedule, Master:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        showLine();
    }

    /**
     * Displays the tasks that match a search keyword.
     *
     * @param tasks The task list containing matching tasks.
     */
    public void showFoundTasks(TaskList tasks) {
        showLine();
        if (tasks.size() == 0) {
            System.out.println("My apologies, Master. No matching tasks were found.");
        } else {
            System.out.println("Here are the matching tasks, Master:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + "." + tasks.get(i));
            }
        }
        showLine();
    }

    /**
     * Closes the scanner.
     */
    public void close() {
        scanner.close();
    }
}
