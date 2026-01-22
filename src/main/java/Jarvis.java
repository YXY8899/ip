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
            } else {
                Task newTask = new Task(input);
                tasks[taskCount] = newTask;
                taskCount++;
                System.out.println("____________________________________________________________");
                System.out.println("'" + input + "'" + " has been stored");
                System.out.println("____________________________________________________________");
            }
        }

        scanner.close();
    }
}
