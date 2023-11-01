package seedu.address.logic.commands.editcommands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.DESC_GOOGLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_COMPANY_NAME_APPLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_COMPANY_NAME_ORACLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_COMPANY_PHONE_ORACLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_TECH;
import static seedu.address.logic.commands.CommandTestUtil.assertPersonCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.showCompanyAtIndex;
import static seedu.address.testutil.TypicalCompanies.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON_OR_COMPANY;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON_OR_COMPANY;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.CommandTestUtil;
import seedu.address.logic.commands.clearcommands.ClearAllCommand;
import seedu.address.logic.commands.editcommand.EditCompanyCommand;
import seedu.address.logic.commands.editcommand.EditCompanyCommand.EditCompanyDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.company.Company;
import seedu.address.model.company.internship.Internship;
import seedu.address.testutil.CompanyBuilder;
import seedu.address.testutil.EditCompanyDescriptorBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCompanyCommandIntegrationTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Company companyToEdit = model.getFilteredCompanyList().get(0);
        Company editedCompany = new CompanyBuilder().build();

        for (Internship i : companyToEdit.getInternshipList()) {
            editedCompany.addInternship(i);
        }

        EditCompanyDescriptor descriptor = new EditCompanyDescriptorBuilder(editedCompany).build();
        EditCompanyCommand editCompanyCommand = new EditCompanyCommand(INDEX_FIRST_PERSON_OR_COMPANY, descriptor);

        String expectedMessage = String.format(
                EditCompanyCommand.MESSAGE_SUCCESS, Messages.formatCompany(editedCompany));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setCompany(model.getFilteredCompanyList().get(0), editedCompany);

        CommandTestUtil.assertRegularCommandSuccess(editCompanyCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastCompany = Index.fromOneBased(model.getFilteredCompanyList().size());
        Company lastCompany = model.getFilteredCompanyList().get(indexLastCompany.getZeroBased());

        CompanyBuilder companyInList = new CompanyBuilder(lastCompany);
        Company editedCompany = companyInList.withCompanyName(VALID_COMPANY_NAME_ORACLE)
                .withCompanyPhone(VALID_COMPANY_PHONE_ORACLE)
                .withTags(VALID_TAG_TECH).build();

        EditCompanyDescriptor descriptor = new EditCompanyDescriptorBuilder()
                .withCompanyName(VALID_COMPANY_NAME_ORACLE)
                .withCompanyPhone(VALID_COMPANY_PHONE_ORACLE)
                .withTags(VALID_TAG_TECH).build();
        EditCompanyCommand editCompanyCommand = new EditCompanyCommand(indexLastCompany, descriptor);

        String expectedMessage = String.format(
                EditCompanyCommand.MESSAGE_SUCCESS, Messages.formatCompany(editedCompany));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setCompany(lastCompany, editedCompany);

        CommandTestUtil.assertRegularCommandSuccess(editCompanyCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCompanyCommand editCompanyCommand = new EditCompanyCommand(
                INDEX_FIRST_PERSON_OR_COMPANY, new EditCompanyDescriptor());
        Company editedCompany = model.getFilteredCompanyList().get(INDEX_FIRST_PERSON_OR_COMPANY.getZeroBased());

        String expectedMessage = String.format(
                EditCompanyCommand.MESSAGE_SUCCESS, Messages.formatCompany(editedCompany));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        CommandTestUtil.assertRegularCommandSuccess(editCompanyCommand, model, expectedMessage, expectedModel);
    }


    @Test
    public void execute_duplicateCompanyUnfilteredList_failure() {
        Company firstCompany = model.getFilteredCompanyList().get(INDEX_FIRST_PERSON_OR_COMPANY.getZeroBased());
        EditCompanyDescriptor descriptor = new EditCompanyDescriptorBuilder(firstCompany).build();
        EditCompanyCommand editCompanyCommand = new EditCompanyCommand(INDEX_SECOND_PERSON_OR_COMPANY, descriptor);

        assertPersonCommandFailure(editCompanyCommand, model, EditCompanyCommand.MESSAGE_DUPLICATE_COMPANY);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidCompanyIndexFilteredList_failure() {
        showCompanyAtIndex(model, INDEX_FIRST_PERSON_OR_COMPANY);
        Index outOfBoundIndex = INDEX_SECOND_PERSON_OR_COMPANY;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getCompanyList().size());

        EditCompanyCommand editCompanyCommand = new EditCompanyCommand(outOfBoundIndex,
                new EditCompanyDescriptorBuilder().withCompanyName(VALID_COMPANY_NAME_APPLE).build());

        assertPersonCommandFailure(editCompanyCommand, model, Messages.MESSAGE_INVALID_COMPANY_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditCompanyCommand standardCommand = new EditCompanyCommand(INDEX_FIRST_PERSON_OR_COMPANY, DESC_APPLE);

        // same values -> returns true
        EditCompanyDescriptor copyDescriptor = new EditCompanyDescriptor(DESC_APPLE);
        EditCompanyCommand commandWithSameValues =
                new EditCompanyCommand(INDEX_FIRST_PERSON_OR_COMPANY, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearAllCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCompanyCommand(INDEX_SECOND_PERSON_OR_COMPANY, DESC_APPLE)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCompanyCommand(INDEX_FIRST_PERSON_OR_COMPANY, DESC_GOOGLE)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditCompanyDescriptor editCompanyDescriptor = new EditCompanyDescriptor();
        EditCompanyCommand editCompanyCommand = new EditCompanyCommand(index, editCompanyDescriptor);
        String expected = EditCompanyCommand.class.getCanonicalName() + "{index=" + index + ", editCompanyDescriptor="
                + editCompanyDescriptor + "}";
        assertEquals(expected, editCompanyCommand.toString());
    }

}
