package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;

/** Changes or removes the remark of a person in the displayed list. */
public class RemarkCommand extends Command {

    public static final String COMMAND_WORD = "remark";
    public static final String MESSAGE_USAGE = "remark INDEX r/REMARK (use r/ alone to remove a remark)";
    public static final String MESSAGE_ADD_REMARK_SUCCESS = "Remark updated for: %1$s";
    public static final String MESSAGE_DELETE_REMARK_SUCCESS = "Remark removed from: %1$s";

    private final Index index;
    private final Remark remark;

    /** Creates a command to replace the remark at the displayed index. */
    public RemarkCommand(Index index, Remark remark) {
        this.index = requireNonNull(index);
        this.remark = requireNonNull(remark);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = personToEdit.withRemark(remark);
        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        String message = remark.value.isEmpty() ? MESSAGE_DELETE_REMARK_SUCCESS : MESSAGE_ADD_REMARK_SUCCESS;
        return new CommandResult(String.format(message, editedPerson.getName().fullName));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RemarkCommand otherCommand)) {
            return false;
        }
        return index.equals(otherCommand.index) && remark.equals(otherCommand.remark);
    }
}
