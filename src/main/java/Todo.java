/**
 * Represents a simple to-do task with only a description.
 */
public class Todo extends Task {
    /**
     * Creates a new to-do task with the given description.
     *
     * @param description The description of the to-do.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toFileString() {
        return "T | " + super.toFileString();
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
