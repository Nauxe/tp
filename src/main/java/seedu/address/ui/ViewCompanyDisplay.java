package seedu.address.ui;

import java.util.Comparator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import seedu.address.model.company.Company;
import seedu.address.model.company.internship.Internship;
import seedu.address.model.person.Person;

public class ViewCompanyDisplay extends UiPart<Region> {
    private static final String FXML = "ViewCompanyDisplay.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Company company;

    private InternshipListPanel internshipListPanel;

    @FXML
    private HBox cardPane;
    @FXML
    private Label nameDisplayed;
    @FXML
    private Label phoneDisplayed;
    @FXML
    private Label emailDisplayed;
    @FXML
    private Label descriptionDisplayed;
    @FXML
    private FlowPane tagsDisplayed;
    @FXML
    private StackPane internshipListPlaceholder;
    @FXML
    private VBox yourInternshipsSection;
    @FXML
    private VBox companyDisplay;

    /**
     * Displays a company's details in the viewing box.
     */
    public ViewCompanyDisplay(Company company) {
        super(FXML);

        this.company = company;
        nameDisplayed.setText(company.getCompanyName().fullName);
        phoneDisplayed.setText(company.getCompanyPhone().value);
        emailDisplayed.setText(company.getCompanyEmail().value);
        descriptionDisplayed.setText(company.getDescription().value);

        company.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tagsDisplayed.getChildren().add(new Label(tag.tagName)));

        ObservableList<Internship> internList = company.getInternshipsAsSortedObservableList();

        if (internList.size() < 1) {
            this.companyDisplay.getChildren().remove(yourInternshipsSection);
        } else {
            internshipListPanel = new InternshipListPanel(company.getInternshipsAsSortedObservableList());
            this.internshipListPlaceholder.getChildren().add(internshipListPanel.getRoot());
        }
    }
}
