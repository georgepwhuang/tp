package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {
    public static final String COMMAND_WORD = "clear";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Clears the information of your choice\n"
            + "clear person all -- removes all people\n"
            + "clear person selected -- removes displayed people\n"
            + "clear meeting all -- unschedules all meetings\n"
            + "clear meeting expired -- removes expired meetings";


    public static final String MESSAGE_ALL_PERSON_SUCCESS = "Address book has been cleared!";
    public static final String MESSAGE_SELECTED_PERSON_SUCCESS = "Selected people have been removed";
    public static final String MESSAGE_ALL_MEETING_SUCCESS = "All meetings are removed!";
    public static final String MESSAGE_EXPIRED_MEETING_SUCCESS = "Expired meetings are removed!";
    public static final String MESSAGE_FAILURE = "Encountered error in clearing address book";

    private final String args;

    /**
     * @param args Type of clearing to execute
     */
    public ClearCommand(String args) {
        requireNonNull(args);
        this.args = args;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = new ArrayList<>(model.getFilteredPersonList());
        List<Person> meetingList = new ArrayList<>(model.getAddressBook().getMeetingList());
        try {
            ClearType clearType = ClearType.wrap(args);
            switch (clearType) {
            case ALLPERSON:
                model.setAddressBook(new AddressBook());
                return new CommandResult(MESSAGE_ALL_PERSON_SUCCESS);
            case SELECTEDPERSON:
                lastShownList.forEach(model::deletePerson);
                model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
                return new CommandResult(MESSAGE_SELECTED_PERSON_SUCCESS);
            case EXPIREDMEETING:
                LocalDateTime now = LocalDateTime.now();
                meetingList.stream()
                        .filter(person -> person.getMeeting().get().dateTime.isBefore(now))
                        .forEach(person -> model.setPerson(person, person.setMeeting(Optional.empty())));
                return new CommandResult(MESSAGE_ALL_MEETING_SUCCESS);
            case ALLMEETING:
                meetingList.forEach(person -> model.setPerson(person, person.setMeeting(Optional.empty())));
                return new CommandResult(MESSAGE_EXPIRED_MEETING_SUCCESS);
            default:
                throw new NoSuchElementException();
            }
        } catch (NoSuchElementException ex) {
            throw new CommandException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
        }
    }

    public enum ClearType {
        ALLPERSON("person all"),
        SELECTEDPERSON("person selected"),
        ALLMEETING("meeting all"),
        EXPIREDMEETING("meeting expired");

        public final String value;

        ClearType(String value) {
            this.value = value;
        }

        /**
         * Wraps the input string into an enum
         */
        public static ClearType wrap(String string) throws NoSuchElementException {
            return Arrays.stream(ClearType.values())
                    .filter(x -> x.value.equals(string.toLowerCase()))
                    .findAny()
                    .orElseThrow();
        }
    }
}
