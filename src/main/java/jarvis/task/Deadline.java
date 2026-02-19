package jarvis.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a task with a deadline.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a");
    private static final DateTimeFormatter FILE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    protected LocalDateTime by;

    /**
     * Creates a new deadline task with the given description and due date.
     *
     * @param description The description of the task.
     * @param by The deadline for the task in yyyy-MM-dd or yyyy-MM-dd HHmm format.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = parseDateTime(by);
    }

    private LocalDateTime parseDateTime(String dateTimeStr) {
        try {
            return LocalDateTime.parse(dateTimeStr, INPUT_FORMAT);
        } catch (DateTimeParseException e) {
            try {
                return LocalDateTime.parse(dateTimeStr + " 0000", INPUT_FORMAT);
            } catch (DateTimeParseException e2) {
                throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd or yyyy-MM-dd HHmm");
            }
        }
    }

    public LocalDateTime getBy() {
        return by;
    }

    public String getByAsString() {
        return by.format(FILE_FORMAT);
    }

    @Override
    public String toFileString() {
        return "D | " + super.toFileString() + " | " + by.format(FILE_FORMAT);
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(OUTPUT_FORMAT) + ")";
    }

    @Override
    public Task copy() {
        Deadline copy = new Deadline(this.description, this.by.format(FILE_FORMAT));
        if (this.isDone) {
            copy.markAsDone();
        }
        return copy;
    }
}
