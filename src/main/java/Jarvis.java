import java.util.Scanner;

public class Jarvis {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[100];
        int taskCount = 0;

        System.out.println("____________________________________________________________");
        System.out.println("Hello Master! I'm Jarvis your personal assistance!");
        System.out.println("How may I serve you?");
        System.out.println("____________________________________________________________");

        String input = "";
        while (!input.equals("bye")) {
            input = scanner.nextLine();

            if (input.equals("bye")) {
                System.out.println("____________________________________________________________");
                System.out.println("Goodbye Master. Hope to see you again soon!");
                System.out.println("____________________________________________________________");
            } else if (input.equals("list")) {
                System.out.println("____________________________________________________________");
                System.out.println("Here are the tasks in your archive:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("mark ")) {
                int taskIndex = Integer.parseInt(input.substring(5)) - 1;
                tasks[taskIndex].markAsDone();
                System.out.println("____________________________________________________________");
                System.out.println("Nice! I've marked this task as completed:");
                System.out.println("  " + tasks[taskIndex]);
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("unmark ")) {
                int taskIndex = Integer.parseInt(input.substring(7)) - 1;
                tasks[taskIndex].markAsNotDone();
                System.out.println("____________________________________________________________");
                System.out.println("OK, I've revert the task completion");
                System.out.println("  " + tasks[taskIndex]);
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("todo ")) {
                String description = input.substring(5);
                Task newTask = new Todo(description);
                tasks[taskCount] = newTask;
                taskCount++;
                System.out.println("____________________________________________________________");
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + newTask);
                System.out.println("Now you have " + taskCount + " tasks in the list.");
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("deadline ")) {
                String[] parts = input.substring(9).split(" /by ");
                String description = parts[0];
                String by = parts[1];
                Task newTask = new Deadline(description, by);
                tasks[taskCount] = newTask;
                taskCount++;
                System.out.println("____________________________________________________________");
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + newTask);
                System.out.println("Now you have " + taskCount + " tasks in the list.");
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("event ")) {
                String[] parts = input.substring(6).split(" /from | /to ");
                String description = parts[0];
                String from = parts[1];
                String to = parts[2];
                Task newTask = new Event(description, from, to);
                tasks[taskCount] = newTask;
                taskCount++;
                System.out.println("____________________________________________________________");
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + newTask);
                System.out.println("Now you have " + taskCount + " tasks in the list.");
                System.out.println("____________________________________________________________");
            } else {
                System.out.println("____________________________________________________________");
                System.out.println("I'm sorry Master, I don't understand that command.");
                System.out.println("____________________________________________________________");
            }
        }

        scanner.close();
    }
}
