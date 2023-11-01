package seedu.address.logic.commands.clearcommands;

import static seedu.address.testutil.TypicalCompanies.APPLE;
import static seedu.address.testutil.TypicalCompanies.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON_OR_COMPANY;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CommandTestUtil;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.company.Company;
import seedu.address.testutil.CompanyBuilder;


public class ClearInternshipCommandTest {
    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Company validCompany = new CompanyBuilder().build();
        model.addCompany(validCompany);
    }

    @Test
    public void execute_noInternshipCompany_success() {
        ClearInternshipCommand clearInternshipCommand = new ClearInternshipCommand(INDEX_FIRST_PERSON_OR_COMPANY);

        String expectedMessage = String.format(
                ClearInternshipCommand.MESSAGE_SUCCESS, APPLE.getCompanyName());
        Model expectedModel = model;
        CommandTestUtil.assertRegularCommandSuccess(clearInternshipCommand, model, expectedMessage, expectedModel);

    }

}
