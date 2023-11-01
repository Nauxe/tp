package seedu.address.model.company;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.company.internship.Internship;
import seedu.address.model.company.internship.InternshipInterviewDateTime;
import seedu.address.model.company.internship.UniqueInternshipList;
import seedu.address.model.tag.Tag;

/**
 * Represents a Company in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Company {
    // Identity fields
    private final CompanyName companyName;
    private final CompanyPhone phone;
    private final CompanyEmail email;

    // Data fields
    private final CompanyDescription companyDescription;
    private final Set<Tag> tags = new HashSet<>();
    private final UniqueInternshipList internships;

    /**
     * Constructs a company without any Internships.
     * Every field must be present and not null.
     */
    public Company(CompanyName companyName, CompanyPhone phone,
                   CompanyEmail email, CompanyDescription companyDescription,
                   Set<Tag> tags) {
        requireAllNonNull(companyName, phone, email, companyDescription, tags);
        this.companyName = companyName;
        this.phone = phone;
        this.email = email;
        this.companyDescription = companyDescription;
        this.tags.addAll(tags);
        this.internships = new UniqueInternshipList();
    }

    /**
     * Constructs a Company with a given List of Internships.
     * Every field must be present and not null.
     */
    public Company(CompanyName companyName, CompanyPhone phone,
                   CompanyEmail email, CompanyDescription companyDescription,
                   Set<Tag> tags, List<Internship> internships) {
        requireAllNonNull(companyName, phone, email, companyDescription, tags);
        this.companyName = companyName;
        this.phone = phone;
        this.email = email;
        this.companyDescription = companyDescription;
        this.tags.addAll(tags);
        this.internships = new UniqueInternshipList();

        for (Internship i : internships) {
            this.internships.add(i);
        }
    }

    public CompanyName getCompanyName() {
        return companyName;
    }

    public CompanyPhone getCompanyPhone() {
        return phone;
    }

    public CompanyEmail getCompanyEmail() {
        return email;
    }

    public CompanyDescription getCompanyDescription() {
        return companyDescription;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Adds an internship to the company in the address book.
     * The internship must not already exist in the address book.
     */
    public void addInternship(Internship toAdd) {
        internships.add(toAdd);
    }

    /**
     * Returns true if the comapany already has this internship.
     */
    public boolean hasInternship(Internship internship) {
        return this.internships.contains(internship);
    }

    /**
     * Returns the Internship at the specified index.
     */
    public Internship getInternshipAtIndex(int i) {
        return this.internships.get(i);
    }

    /**
     * Sets the Internship at {@code target} with {@code internship}.
     */
    public void setInternship(Internship target, Internship internship) {
        this.internships.setInternship(target, internship);
    }

    /**
     * Returns an ObservableList of internships.
     */
    public ObservableList<Internship> getInternshipList() {
        return internships.asUnmodifiableObservableList();
    }

    /**
     * Returns the internship with the closest and most urgent Interview Date and Time.
     *
     * @return The most urgent Internship.
     */
    public Optional<Internship> getMostUrgentInternship() {
        LocalDateTime now = LocalDateTime.now();
        Optional<Internship> mostUrgent = Optional.empty();

        for (Internship internship : internships.asUnmodifiableObservableList()) {
            Optional<InternshipInterviewDateTime> interviewDateTime = internship.getInternshipDateTime();

            if (interviewDateTime.isPresent()) {
                LocalDateTime interviewDate = interviewDateTime.get().getInternshipDateTime();

                if (interviewDate.isAfter(now)) {
                    if (mostUrgent.isEmpty() || interviewDate.isBefore(mostUrgent.get().getInternshipDateTime()
                            .get().getInternshipDateTime())) {
                        mostUrgent = Optional.of(internship);
                    }
                }
            }
        }

        return mostUrgent;
    }

    /**
     * Returns an ObservableList of Internship objects, sorted by the one with the most recent date and time first.
     *
     * @return The ObservableList of Internship objects under this company.
     */
    public ObservableList<Internship> getInternshipsAsSortedObservableList() {
        return this.internships.getInternshipsAsSortedObservableList();
    }

    /**
     * Returns true if both companies have the same name.
     * This defines a weaker notion of equality between two companies.
     */
    public boolean isSameCompany(Company otherCompany) {
        if (otherCompany == this) {
            return true;
        }

        return otherCompany != null
            && otherCompany.getCompanyName().equals(getCompanyName());
    }

    /**
     * Returns true if both companys have the same identity and data fields.
     * This defines a stronger notion of equality between two companys.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Company)) {
            return false;
        }

        Company otherCompany = (Company) other;
        return companyName.equals(otherCompany.companyName)
            && phone.equals(otherCompany.phone)
            && email.equals(otherCompany.email)
            && companyDescription.equals(otherCompany.companyDescription)
            && tags.equals(otherCompany.tags)
            && internships.equals(otherCompany.internships);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(companyName, phone, email, companyDescription, tags, internships);
    }

    /**
     * Prints a list of internships for viewing.
     *
     * @return String of List of internships
     */
    public String toStringInternships() {
        StringBuilder result = new StringBuilder("[");
        for (Internship i : this.internships.asUnmodifiableObservableList()) {
            result.append(i.toString()).append(", ");
        }

        if (result.length() >= 2) {
            result.delete(result.length() - 2, result.length());
        }
        result.append("]");
        return result.toString();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .add("\nname", companyName)
            .add("\nphone", phone)
            .add("\nemail", email)
            .add("\ncompanyDescription", companyDescription)
            .add("\ntags", tags)
            .add("\ninternships", toStringInternships())
            .toString();
    }
}
