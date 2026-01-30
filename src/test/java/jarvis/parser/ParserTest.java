package jarvis.parser;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import jarvis.exception.EmptyDescriptionException;
import jarvis.exception.InvalidFormatException;
import jarvis.exception.JarvisException;
import jarvis.exception.MissingArgumentException;

public class ParserTest {

    @Test
    public void getCommand_validInput_returnsCommand() {
        assertEquals("todo", Parser.getCommand("todo read book"));
        assertEquals("deadline", Parser.getCommand("deadline return book /by 2024-12-01"));
        assertEquals("event", Parser.getCommand("event meeting /from 2024-12-01 /to 2024-12-02"));
        assertEquals("list", Parser.getCommand("list"));
        assertEquals("mark", Parser.getCommand("mark 1"));
        assertEquals("bye", Parser.getCommand("bye"));
    }

    @Test
    public void getCommand_emptyInput_returnsEmptyString() {
        assertEquals("", Parser.getCommand(""));
    }

    @Test
    public void getCommand_uppercaseInput_returnsLowercase() {
        assertEquals("todo", Parser.getCommand("TODO read book"));
        assertEquals("list", Parser.getCommand("LIST"));
    }

    @Test
    public void parseTodo_validInput_returnsDescription() throws JarvisException {
        assertEquals("read book", Parser.parseTodo("todo read book"));
        assertEquals("do homework", Parser.parseTodo("todo do homework"));
    }

    @Test
    public void parseTodo_emptyDescription_throwsException() {
        assertThrows(EmptyDescriptionException.class, () -> Parser.parseTodo("todo "));
        assertThrows(EmptyDescriptionException.class, () -> Parser.parseTodo("todo      "));
    }

    @Test
    public void parseDeadline_validInput_returnsArray() throws JarvisException {
        String[] result = Parser.parseDeadline("deadline return book /by 2024-12-01");
        assertArrayEquals(new String[]{"return book", "2024-12-01"}, result);
    }

    @Test
    public void parseDeadline_validInputWithTime_returnsArray() throws JarvisException {
        String[] result = Parser.parseDeadline("deadline submit essay /by 2024-12-01 1800");
        assertArrayEquals(new String[]{"submit essay", "2024-12-01 1800"}, result);
    }

    @Test
    public void parseDeadline_emptyDescription_throwsException() {
        assertThrows(EmptyDescriptionException.class, () ->
                Parser.parseDeadline("deadline "));
    }

    @Test
    public void parseDeadline_missingByKeyword_throwsException() {
        assertThrows(InvalidFormatException.class, () ->
                Parser.parseDeadline("deadline return book 2024-12-01"));
    }

    @Test
    public void parseDeadline_emptyDate_throwsException() {
        // When trimmed, " /by " pattern is not found, so InvalidFormatException is thrown
        assertThrows(InvalidFormatException.class, () ->
                Parser.parseDeadline("deadline return book /by "));
    }

    @Test
    public void parseEvent_validInput_returnsArray() throws JarvisException {
        String[] result = Parser.parseEvent("event meeting /from 2024-12-01 1400 /to 2024-12-01 1600");
        assertArrayEquals(new String[]{"meeting", "2024-12-01 1400", "2024-12-01 1600"}, result);
    }

    @Test
    public void parseEvent_emptyDescription_throwsException() {
        assertThrows(EmptyDescriptionException.class, () ->
                Parser.parseEvent("event "));
    }

    @Test
    public void parseEvent_missingFromKeyword_throwsException() {
        assertThrows(InvalidFormatException.class, () ->
                Parser.parseEvent("event meeting /to 2024-12-01"));
    }

    @Test
    public void parseEvent_missingToKeyword_throwsException() {
        assertThrows(InvalidFormatException.class, () ->
                Parser.parseEvent("event meeting /from 2024-12-01"));
    }

    @Test
    public void parseTaskIndex_validInput_returnsIndex() throws JarvisException {
        assertEquals(0, Parser.parseTaskIndex("mark 1", 5, "mark"));
        assertEquals(4, Parser.parseTaskIndex("delete 5", 7, "delete"));
        assertEquals(9, Parser.parseTaskIndex("unmark 10", 7, "unmark"));
    }

    @Test
    public void parseTaskIndex_emptyIndex_throwsException() {
        assertThrows(MissingArgumentException.class, () ->
                Parser.parseTaskIndex("mark ", 5, "mark"));
    }

    @Test
    public void parseTaskIndex_invalidNumber_throwsException() {
        assertThrows(JarvisException.class, () ->
                Parser.parseTaskIndex("mark abc", 5, "mark"));
    }
}
