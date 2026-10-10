package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;
import seedu.address.testutil.TypicalPersons;

public class RemarkCommandTest {

    @Test
    public void execute_filteredStudent_updatesRemarkAndPreservesFields() throws CommandException {
        Person first = new Person("Amy Bee", "81234567", null, List.of("Math"), "JC 1");
        Person second = new Person("Ryan Tan", "92345678", "ryan@example.com",
                List.of("Physics", "Chemistry"), "Secondary 4");
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(first);
        addressBook.addPerson(second);
        ModelManager model = new ModelManager(addressBook, new UserPrefs());
        model.updateFilteredPersonList(person -> person.getName().fullName.equals("Ryan Tan"));

        CommandResult result = new RemarkCommand(Index.fromOneBased(1), new Remark("Likes baseball"))
                .execute(model);

        assertEquals("Remark updated for: Ryan Tan", result.getFeedbackToUser());
        assertEquals(first, model.getFilteredPersonList().get(0));
        assertEquals(second.withRemark(new Remark("Likes baseball")), model.getFilteredPersonList().get(1));
        assertEquals(2, model.getFilteredPersonList().size());
    }

    @Test
    public void execute_legacyPerson_addsThenRemovesRemark() throws CommandException {
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(TypicalPersons.ALICE);
        ModelManager model = new ModelManager(addressBook, new UserPrefs());
        Index first = Index.fromOneBased(1);

        new RemarkCommand(first, new Remark("Old friend")).execute(model);
        assertEquals(TypicalPersons.ALICE.withRemark(new Remark("Old friend")), model.getFilteredPersonList().get(0));

        CommandResult result = new RemarkCommand(first, new Remark("")).execute(model);
        assertEquals("Remark removed from: Alice Pauline", result.getFeedbackToUser());
        assertEquals(TypicalPersons.ALICE, model.getFilteredPersonList().get(0));
    }

    @Test
    public void execute_invalidDisplayedIndex_preservesModel() {
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(TypicalPersons.ALICE);
        ModelManager model = new ModelManager(addressBook, new UserPrefs());
        assertCommandFailure(new RemarkCommand(Index.fromOneBased(2), new Remark("Note")), model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        model.updateFilteredPersonList(person -> false);
        assertTrue(model.getFilteredPersonList().isEmpty());
        assertCommandFailure(new RemarkCommand(Index.fromOneBased(1), new Remark("Note")), model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }
}
