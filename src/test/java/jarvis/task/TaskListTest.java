package jarvis.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TaskListTest {

    private TaskList taskList;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
    }

    @Test
    public void size_emptyList_returnsZero() {
        assertEquals(0, taskList.size());
    }

    @Test
    public void add_singleTask_sizeIncreases() {
        taskList.add(new Todo("read book"));
        assertEquals(1, taskList.size());
    }

    @Test
    public void add_multipleTasks_sizeIncreases() {
        taskList.add(new Todo("read book"));
        taskList.add(new Todo("write essay"));
        taskList.add(new Todo("do homework"));
        assertEquals(3, taskList.size());
    }

    @Test
    public void get_validIndex_returnsTask() {
        Todo todo = new Todo("read book");
        taskList.add(todo);
        assertEquals(todo, taskList.get(0));
    }

    @Test
    public void get_multipleTasksValidIndex_returnsCorrectTask() {
        Todo todo1 = new Todo("task 1");
        Todo todo2 = new Todo("task 2");
        Todo todo3 = new Todo("task 3");
        taskList.add(todo1);
        taskList.add(todo2);
        taskList.add(todo3);

        assertEquals(todo1, taskList.get(0));
        assertEquals(todo2, taskList.get(1));
        assertEquals(todo3, taskList.get(2));
    }

    @Test
    public void get_invalidIndex_throwsException() {
        taskList.add(new Todo("read book"));
        assertThrows(IndexOutOfBoundsException.class, () -> taskList.get(5));
        assertThrows(IndexOutOfBoundsException.class, () -> taskList.get(-1));
    }

    @Test
    public void remove_validIndex_removesTask() {
        taskList.add(new Todo("task 1"));
        taskList.add(new Todo("task 2"));

        Task removed = taskList.remove(0);

        assertEquals("task 1", removed.getDescription());
        assertEquals(1, taskList.size());
        assertEquals("task 2", taskList.get(0).getDescription());
    }

    @Test
    public void remove_middleIndex_removesCorrectTask() {
        taskList.add(new Todo("task 1"));
        taskList.add(new Todo("task 2"));
        taskList.add(new Todo("task 3"));

        Task removed = taskList.remove(1);

        assertEquals("task 2", removed.getDescription());
        assertEquals(2, taskList.size());
        assertEquals("task 1", taskList.get(0).getDescription());
        assertEquals("task 3", taskList.get(1).getDescription());
    }

    @Test
    public void remove_invalidIndex_throwsException() {
        taskList.add(new Todo("read book"));
        assertThrows(IndexOutOfBoundsException.class, () -> taskList.remove(5));
        assertThrows(IndexOutOfBoundsException.class, () -> taskList.remove(-1));
    }

    @Test
    public void addDifferentTaskTypes_allTasksStored() {
        taskList.add(new Todo("todo task"));
        taskList.add(new Deadline("deadline task", "2024-12-01"));
        taskList.add(new Event("event task", "2024-12-01 1400", "2024-12-01 1600"));

        assertEquals(3, taskList.size());
        assertEquals("[T][ ] todo task", taskList.get(0).toString());
        assertEquals("[D][ ] deadline task (by: Dec 01 2024, 12:00 am)", taskList.get(1).toString());
    }

    @Test
    public void taskOperations_markAndUnmark_statusChanges() {
        taskList.add(new Todo("read book"));

        taskList.get(0).markAsDone();
        assertEquals("[T][X] read book", taskList.get(0).toString());

        taskList.get(0).markAsNotDone();
        assertEquals("[T][ ] read book", taskList.get(0).toString());
    }
}
