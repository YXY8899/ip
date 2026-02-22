package jarvis.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents an event with a start and end time.
 */
public class Event extends Task {
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a");
    private static final DateTimeFormatter FILE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    protected LocalDateTime from;
    protected LocalDateTime to;

    /**
     * Creates a new event with the given description and time range.
     *
     * @param description The description of the event.
     * @param from The start time in yyyy-MM-dd or yyyy-MM-dd HHmm format.
     * @param to The end time in yyyy-MM-dd or yyyy-MM-dd HHmm format.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = parseDateTime(from);
        this.to = parseDateTime(to);
        if (this.to.isBefore(this.from)) {
            throw new IllegalArgumentException("Event end time cannot be earlier than start time.");
        }
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

    public LocalDateTime getFrom() {
        return from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public String getFromAsString() {
        return from.format(FILE_FORMAT);
    }

    public String getToAsString() {
        return to.format(FILE_FORMAT);
    }

    @Override
    public String toFileString() {
        return "E | " + super.toFileString() + " | " + from.format(FILE_FORMAT) + " | " + to.format(FILE_FORMAT);
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from.format(OUTPUT_FORMAT)
                + " to: " + to.format(OUTPUT_FORMAT) + ")";
    }

    @Override
    public Task copy() {
        Event copy = new Event(this.description, this.from.format(FILE_FORMAT), this.to.format(FILE_FORMAT));
        if (this.isDone) {
            copy.markAsDone();
        }
        return copy;
    }
}
