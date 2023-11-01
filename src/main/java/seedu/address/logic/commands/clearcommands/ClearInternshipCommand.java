package seedu.address.logic.commands.clearcommands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.commandresults.CommandResult;
import seedu.address.logic.commands.commandresults.RegularCommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.company.Company;

/**
 * Clears the internship list of a specific company.
 */
public class ClearInternshipCommand extends ClearCommand {
    public static final String MESSAGE_SUCCESS = "Internship list of company %1$s has been cleared!";

    private final Index targetIndex;

    /**
     * Creates an ClearInternshipCommand to clear the internship list of the specified {@code Company}.
     */
    public ClearInternshipCommand(Index targetIndex) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
    }
    public Index getTargetIndex() {
        return targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Company> lastShownList = model.getFilteredCompanyList();
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_COMPANY_DISPLAYED_INDEX);
        }
        Company companyToDelete = lastShownList.get(targetIndex.getZeroBased());
        Company updatedCompany = new Company(companyToDelete.getCompanyName(),
                companyToDelete.getCompanyPhone(), companyToDelete.getCompanyEmail(),
                companyToDelete.getCompanyDescription(), companyToDelete.getTags(),
                new ArrayList<>());

        model.setCompany(companyToDelete, updatedCompany);
        model.updateFilteredCompanyList(Model.PREDICATE_SHOW_ALL_COMPANIES);

        return new RegularCommandResult(String.format(MESSAGE_SUCCESS, companyToDelete.getCompanyName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof ClearInternshipCommand)) {
            return false;
        }
        ClearInternshipCommand otherClearInternshipCommand = (ClearInternshipCommand) other;
        return this.targetIndex.equals(otherClearInternshipCommand.getTargetIndex());
    }
}
