package seedu.address.logic.parser;

import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class ClearCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ClearCommand parse(String args) throws ParseException {
        return new ClearCommand(args.trim());
    }
}
