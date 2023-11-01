package seedu.address.logic.commands.addcommands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCompanyCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertPersonCommandFailure;
import static seedu.address.testutil.TypicalCompanies.getTypicalAddressBook;
import static seedu.address.testutil.TypicalInternships.SOFTWARE_ENGINEER_WITH_DATETIME;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.CommandTestUtil;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.company.Company;
import seedu.address.model.company.internship.Internship;
import seedu.address.testutil.CompanyBuilder;
import seedu.address.testutil.InternshipBuilder;

public class AddInternshipCommandIntegrationTest {
    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    /**
     * Test for adding an internship to a company with no existing internships.
     */
    @Test
    public void execute_newInternshipCompanyNoInternships_success() {
        Internship validInternship = new InternshipBuilder().build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        // Getting the company and its internships.
        List<Company> lastShownList = expectedModel.getFilteredCompanyList();
        Company referencedCompany = lastShownList.get(3);
        List<Internship> updatedInternships = new ArrayList<>();
        updatedInternships.add(validInternship);

        // Make copies of the referencedCompany at the index 2.
        Company companyToAddTo = new CompanyBuilder(referencedCompany).build();
        Company updatedCompany = new CompanyBuilder(referencedCompany).withInternships(updatedInternships).build();

        // Set to copy of the company, so that it does not affect the actual company.
        model.setCompany(referencedCompany, companyToAddTo);
        expectedModel.setCompany(companyToAddTo, updatedCompany);

        CommandTestUtil.assertRegularCommandSuccess(
                new AddInternshipCommand(Index.fromOneBased(4), validInternship), model,
                String.format(AddInternshipCommand.MESSAGE_SUCCESS, Messages.formatInternship(validInternship)),
                expectedModel);
    }

    /**
     * Test for adding an internship to a company with existing internships.
     */
    @Test
    public void execute_newInternshipCompanyWithInternships_success() {
        Internship validInternship = new InternshipBuilder().build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        // Getting the company and its internships.
        List<Company> lastShownList = expectedModel.getFilteredCompanyList();
        Company referencedCompany = lastShownList.get(2);
        List<Internship> existingInternships = referencedCompany.getInternshipList();

        // Make copies of the referencedCompany at the index 2.
        Company companyToAddTo = new CompanyBuilder(referencedCompany).build();
        Company updatedCompany = new CompanyBuilder(referencedCompany).build();
        updatedCompany.addInternship(validInternship);

        // Set to copy of the company, so that it does not affect the actual company.
        model.setCompany(referencedCompany, companyToAddTo);
        expectedModel.setCompany(companyToAddTo, updatedCompany);

        CommandTestUtil.assertRegularCommandSuccess(
                new AddInternshipCommand(Index.fromOneBased(3), validInternship), model,
                String.format(AddInternshipCommand.MESSAGE_SUCCESS, Messages.formatInternship(validInternship)),
                expectedModel);
    }

    @Test
    public void execute_addDuplicateInternship_throwsCommandException() {
        Internship internshipInList = SOFTWARE_ENGINEER_WITH_DATETIME;
        assertCompanyCommandFailure(new AddInternshipCommand(Index.fromOneBased(1), internshipInList),
                model, AddInternshipCommand.MESSAGE_DUPLICATE_INTERNSHIP);
    }

    @Test
    public void execute_addInternshipWithNullModel_throwsNullPointerException() {
        Internship validInternship = new InternshipBuilder().build();
        assertThrows(NullPointerException.class, () -> new AddInternshipCommand(
                Index.fromOneBased(1), validInternship).execute(null));
    }

    @Test
    public void execute_addInternshipWithNullIndex_throwsNullPointerException() {
        Internship validInternship = new InternshipBuilder().build();
        assertThrows(NullPointerException.class, () -> new AddInternshipCommand(null,
                validInternship).execute(model));
    }

    @Test
    public void execute_addInternshipAtIndexExceedsListSize_exceptionThrown() {
        Internship validInternship = new InternshipBuilder().build();

        assertPersonCommandFailure(new AddInternshipCommand(Index.fromOneBased(99), validInternship), model,
                Messages.MESSAGE_INVALID_COMPANY_DISPLAYED_INDEX);
    }

    @Test
    public void execute_addInternshipWithNullInternship_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddInternshipCommand(Index.fromOneBased(1),
                null).execute(model));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        Internship validInternship = new InternshipBuilder().build();
        AddInternshipCommand addInternshipCommand = new AddInternshipCommand(
                Index.fromOneBased(1), validInternship);
        assertTrue(addInternshipCommand.equals(addInternshipCommand));
    }

    @Test
    public void equals_nullObject_returnsFalse() {
        Internship validInternship = new InternshipBuilder().build();
        AddInternshipCommand addInternshipCommand = new AddInternshipCommand(
                Index.fromOneBased(1), validInternship);
        assertFalse(addInternshipCommand.equals(null));
    }

    @Test
    public void equals_differentInternship_returnsFalse() {
        Internship internship1 = new InternshipBuilder().withInternshipName("Internship1").build();
        Internship internship2 = new InternshipBuilder().withInternshipName("Internship2").build();
        AddInternshipCommand addInternshipCommand1 = new AddInternshipCommand(
                Index.fromOneBased(1), internship1);
        AddInternshipCommand addInternshipCommand2 = new AddInternshipCommand(
                Index.fromOneBased(1), internship2);
        assertFalse(addInternshipCommand1.equals(addInternshipCommand2));
    }
}
