package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.model.company.Company;
import seedu.address.model.company.CompanyDescription;
import seedu.address.model.company.CompanyEmail;
import seedu.address.model.company.CompanyName;
import seedu.address.model.company.CompanyPhone;
import seedu.address.model.company.internship.Internship;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Company objects.
 */
public class CompanyBuilder {

    public static final String DEFAULT_NAME = "TechCorp Inc.";
    public static final String DEFAULT_PHONE = "12345678";
    public static final String DEFAULT_EMAIL = "contact@techcorp.com";
    public static final String DEFAULT_DESCRIPTION = "A leading tech company.";

    private CompanyName companyName;
    private CompanyPhone companyPhone;
    private CompanyEmail companyEmail;
    private CompanyDescription companyDescription;
    private Set<Tag> tags;
    private List<Internship> internships;

    /**
     * Creates a {@code CompanyBuilder} with the default details.
     */
    public CompanyBuilder() {
        companyName = new CompanyName(DEFAULT_NAME);
        companyPhone = new CompanyPhone(DEFAULT_PHONE);
        companyEmail = new CompanyEmail(DEFAULT_EMAIL);
        companyDescription = new CompanyDescription(DEFAULT_DESCRIPTION);
        tags = new HashSet<>();
        internships = new ArrayList<>();
    }

    /**
     * Initializes the CompanyBuilder with the data of {@code companyToCopy}.
     */
    public CompanyBuilder(Company companyToCopy) {
        internships = new ArrayList<>();
        companyName = companyToCopy.getCompanyName();
        companyPhone = companyToCopy.getCompanyPhone();
        companyEmail = companyToCopy.getCompanyEmail();
        companyDescription = companyToCopy.getCompanyDescription();
        tags = new HashSet<>(companyToCopy.getTags());
        internships.addAll(companyToCopy.getInternshipList());
    }

    /**
     * Sets the {@code Name} of the {@code Company} that we are building.
     */
    public CompanyBuilder withCompanyName(String companyName) {
        this.companyName = new CompanyName(companyName);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Company} that we are building.
     */
    public CompanyBuilder withTags(String... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Parses the {@code internships} into a {@code Set<Internship>}
     * and set it to the {@code Company} that we are building.
     */
    public CompanyBuilder withInternships(Internship... internships) {
        this.internships.addAll(Arrays.asList(internships));
        return this;
    }

    /**
     * Sets the {@code internships} to the {@code Company} that we are building.
     */
    public CompanyBuilder withInternships(List<Internship> internships) {
        this.internships = internships;
        return this;
    }

    /**
     * Removes the {@code Set<Internship>} from the {@code Company} that we are building.
     */
    public CompanyBuilder withoutInternships() {
        this.internships = new ArrayList<>();
        return this;
    }

    /**
     * Sets the {@code companyDescription} of the {@code Company} that we are building.
     */
    public CompanyBuilder withDescription(String companyDescription) {
        this.companyDescription = new CompanyDescription(companyDescription);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Company} that we are building.
     */
    public CompanyBuilder withCompanyPhone(String companyPhone) {
        this.companyPhone = new CompanyPhone(companyPhone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Company} that we are building.
     */
    public CompanyBuilder withCompanyEmail(String companyEmail) {
        this.companyEmail = new CompanyEmail(companyEmail);
        return this;
    }

    public Company build() {
        return new Company(companyName, companyPhone, companyEmail, companyDescription, tags, internships);
    }
}
