package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.RemarkCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Remark;

public class RemarkCommandParserTest {

    private final RemarkCommandParser parser = new RemarkCommandParser();

    @Test
    public void parse_validRemark_returnsRemarkCommand() throws Exception {
        assertEquals(new RemarkCommand(Index.fromOneBased(2), new Remark("Likes baseball")),
                parser.parse(" 2 r/Likes baseball"));
        assertEquals(new RemarkCommand(Index.fromOneBased(2), new Remark("")), parser.parse(" 2 r/"));
    }

    @Test
    public void parse_missingPrefixOrInvalidIndex_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE);
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse(" 2"));
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse(" 0 r/Note"));
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse(" x r/Note"));
    }

    @Test
    public void parse_duplicateRemarkPrefix_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse(" 2 r/First r/Second"));
    }
}
