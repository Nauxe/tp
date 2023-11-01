package seedu.address.logic.commands.addcommands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.commandresults.CommandResult;
import seedu.address.logic.commands.commandresults.RegularCommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.company.Company;
import seedu.address.model.company.internship.Internship;

/**
 * Adds an internship to a company in the address book.
 */
public class AddInternshipCommand extends AddCommand {

    public static final String MESSAGE_SUCCESS = "New internship added: %1$s";
    public static final String MESSAGE_DUPLICATE_INTERNSHIP = "This internship already exists in the address book";
    private final Index index;
    private final Internship internship;

    /**
     * Creates an AddCommand to add the specified {@code Internship} to a {@code Company}
     * @param index The index of the company to add the internship to as shown in the last shown list.
     * @param internship The internship to add to the company.
     */
    public AddInternshipCommand(Index index, Internship internship) {
        requireAllNonNull(index, internship);

        this.index = index;
        this.internship = internship;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Company> lastShownList = model.getFilteredCompanyList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_COMPANY_DISPLAYED_INDEX);
        }

        Company companyToAddTo = lastShownList.get(index.getZeroBased());

        if (companyToAddTo.hasInternship(internship)) {
            throw new CommandException(MESSAGE_DUPLICATE_INTERNSHIP);
        }

        companyToAddTo.addInternship(internship);
        return new RegularCommandResult(String.format(MESSAGE_SUCCESS, Messages.formatInternship(internship)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddInternshipCommand)) {
            return false;
        }

        AddInternshipCommand otherAddInternshipCommand = (AddInternshipCommand) other;
        return index.equals(otherAddInternshipCommand.index)
                && internship.equals(otherAddInternshipCommand.internship);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("internship", internship)
                .toString();
    }
}
