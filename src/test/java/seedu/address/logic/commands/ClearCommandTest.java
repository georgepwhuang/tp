package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.VALID_MEETING_PRANK;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MEETING_STH;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class ClearCommandTest {

    @Test
    public void execute_emptyAddressBook_clear_person_all_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearCommand("person all"), model,
                ClearCommand.MESSAGE_ALL_PERSON_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyAddressBook_clear_person_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setAddressBook(new AddressBook());

        assertCommandSuccess(new ClearCommand("person all"), model,
                ClearCommand.MESSAGE_ALL_PERSON_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyAddressBook_clear_one_person_selected_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        showPersonAtIndex(model, INDEX_THIRD_PERSON);
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(new ClearCommand("person selected"), model,
                ClearCommand.MESSAGE_SELECTED_PERSON_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyAddressBook_clear_all_person_selected_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setAddressBook(new AddressBook());

        assertCommandSuccess(new ClearCommand("person selected"), model,
                ClearCommand.MESSAGE_SELECTED_PERSON_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyAddressBook_clear_all_meeting_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Person personToSchedule;
        PersonBuilder personInList;
        Person editedPerson;

        personToSchedule = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        personInList = new PersonBuilder(personToSchedule);
        editedPerson = personInList.withMeeting(VALID_MEETING_STH).build();
        model.setPerson(personToSchedule, editedPerson);

        personToSchedule = model.getFilteredPersonList().get(INDEX_THIRD_PERSON.getZeroBased());
        personInList = new PersonBuilder(personToSchedule);
        editedPerson = personInList.withMeeting(VALID_MEETING_PRANK).build();
        model.setPerson(personToSchedule, editedPerson);

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        assertCommandSuccess(new ClearCommand("meeting all"), model,
                ClearCommand.MESSAGE_ALL_MEETING_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyAddressBook_clear_selected_meeting_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Person personToSchedule;
        PersonBuilder personInList;
        Person editedPerson;

        String past_meeting = "Past @ " +
                LocalDateTime.MIN.withYear(1900).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String future_meeting = "Future @ " +
                LocalDateTime.MAX.withYear(9999).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        personToSchedule = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        personInList = new PersonBuilder(personToSchedule);
        editedPerson = personInList.withMeeting(past_meeting).build();
        model.setPerson(personToSchedule, editedPerson);

        personToSchedule = model.getFilteredPersonList().get(INDEX_THIRD_PERSON.getZeroBased());
        personInList = new PersonBuilder(personToSchedule);
        editedPerson = personInList.withMeeting(future_meeting).build();
        model.setPerson(personToSchedule, editedPerson);

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToSchedule, editedPerson);

        assertCommandSuccess(new ClearCommand("meeting expired"), model,
                ClearCommand.MESSAGE_EXPIRED_MEETING_SUCCESS, expectedModel);
    }
}
