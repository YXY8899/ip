package jarvis.parser;

import jarvis.exception.EmptyDescriptionException;
import jarvis.exception.InvalidFormatException;
import jarvis.exception.InvalidTaskNumberException;
import jarvis.exception.JarvisException;
import jarvis.exception.MissingArgumentException;

/**
 * Parses user input and extracts command information.
 */
public class Parser {

    /**
     * Parses the user input and returns the command type.
     *
     * @param input The user input string.
     * @return The command type.
     */
    public static String getCommand(String input) {
        return input.split(" ")[0].toLowerCase();
    }

    /**
     * Parses a todo command and returns the task description.
     *
     * @param input The user input string.
     * @return The todo description.
     * @throws JarvisException If the description is empty.
     */
    public static String parseTodo(String input) throws JarvisException {
        String description = input.substring(5).trim();
        if (description.isEmpty()) {
            throw new EmptyDescriptionException("todo");
        }
        return description;
    }

    /**
     * Parses a deadline command and returns the description and deadline.
     *
     * @param input The user input string.
     * @return An array containing [description, by].
     * @throws JarvisException If the format is invalid.
     */
    public static String[] parseDeadline(String input) throws JarvisException {
        String details = input.substring(9).trim();

        if (details.isEmpty()) {
            throw new EmptyDescriptionException("deadline");
        }

        if (!details.contains(" /by ")) {
            throw new InvalidFormatException(
                    "Please specify the deadline using '/by'. Usage: deadline [description] /by [date]");
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

        return new String[]{description, by};
    }

    /**
     * Parses an event command and returns the description, start, and end times.
     *
     * @param input The user input string.
     * @return An array containing [description, from, to].
     * @throws JarvisException If the format is invalid.
     */
    public static String[] parseEvent(String input) throws JarvisException {
        String details = input.substring(6).trim();

        if (details.isEmpty()) {
            throw new EmptyDescriptionException("event");
        }

        if (!details.contains(" /from ") || !details.contains(" /to ")) {
            throw new InvalidFormatException(
                    "Please specify the event time using '/from' and '/to'. "
                    + "Usage: event [description] /from [start] /to [end]");
        }

        String[] parts = details.split(" /from | /to ");

        if (parts.length < 3) {
            throw new InvalidFormatException(
                    "Please provide both start and end times. Usage: event [description] /from [start] /to [end]");
        }

        String description = parts[0].trim();
        String from = parts[1].trim();
        String to = parts[2].trim();

        if (description.isEmpty()) {
            throw new EmptyDescriptionException("event");
        }

        if (from.isEmpty()) {
            throw new MissingArgumentException("when the event starts",
                    "event [description] /from [start] /to [end]");
        }

        if (to.isEmpty()) {
            throw new MissingArgumentException("when the event ends",
                    "event [description] /from [start] /to [end]");
        }

        return new String[]{description, from, to};
    }

    /**
     * Parses a task index from commands like mark, unmark, delete.
     *
     * @param input The user input string.
     * @param commandLength The length of the command word.
     * @param commandName The name of the command for error messages.
     * @return The task index (0-based).
     * @throws JarvisException If the index is invalid.
     */
    public static int parseTaskIndex(String input, int commandLength, String commandName) throws JarvisException {
        String indexStr = input.substring(commandLength).trim();

        if (indexStr.isEmpty()) {
            throw new MissingArgumentException("which task to " + commandName,
                    commandName + " [task number]");
        }

        try {
            return Integer.parseInt(indexStr) - 1;
        } catch (NumberFormatException e) {
            throw new InvalidTaskNumberException(
                    "Please provide a valid task number. Usage: " + commandName + " [task number]");
        }
    }
}
