import java.util.Scanner;

public class Jarvis {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("____________________________________________________________");
        System.out.println("Hello! I'm Jarvis");
        System.out.println("What can I do for you?");
        System.out.println("____________________________________________________________");

        String input = "";
        while (!input.equals("bye")) {
            input = scanner.nextLine();

            if (input.equals("bye")) {
                System.out.println("____________________________________________________________");
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println("____________________________________________________________");
            } else {
                System.out.println("____________________________________________________________");
                System.out.println(input);
                System.out.println("____________________________________________________________");
            }
        }

        scanner.close();
    }
}
