package seedu.address.model.company.internship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalInternships.DATA_ANALYST_WITH_DATETIME;
import static seedu.address.testutil.TypicalInternships.MARKETING_INTERN_WITHOUT_DATETIME;
import static seedu.address.testutil.TypicalInternships.SOFTWARE_ENGINEER_WITHOUT_DATETIME;
import static seedu.address.testutil.TypicalInternships.SOFTWARE_ENGINEER_WITH_DATETIME;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.model.company.exceptions.DuplicateInternshipException;
import seedu.address.model.company.exceptions.InternshipNotFoundException;
import seedu.address.testutil.InternshipBuilder;

public class UniqueInternshipListTest {

    @Test
    public void containsInternshipSuccess() {
        UniqueInternshipList uniqueInternshipList = new UniqueInternshipList();
        Internship internship = new InternshipBuilder(SOFTWARE_ENGINEER_WITH_DATETIME).build();
        uniqueInternshipList.add(internship);
        assertTrue(uniqueInternshipList.contains(internship));
    }

    @Test
    public void contains_nullInternship_throwsNullPointerException() {
        UniqueInternshipList uniqueInternshipList = new UniqueInternshipList();
        assertThrows(NullPointerException.class, () -> uniqueInternshipList.contains(null));
    }

    @Test
    public void contains_internshipNotInList_returnsFalse() {
        UniqueInternshipList uniqueInternshipList = new UniqueInternshipList();
        Internship internship = new InternshipBuilder(DATA_ANALYST_WITH_DATETIME).build();
        assertFalse(uniqueInternshipList.contains(internship));
    }

    @Test
    public void add_nullInternship_throwsNullPointerException() {
        UniqueInternshipList uniqueInternshipList = new UniqueInternshipList();
        assertThrows(NullPointerException.class, () -> uniqueInternshipList.add(null));
    }

    @Test
    public void add_sameInternship_throwsDuplicateInternshipException() {
        UniqueInternshipList uniqueInternshipList = new UniqueInternshipList();
        Internship internship = new InternshipBuilder(SOFTWARE_ENGINEER_WITH_DATETIME).build();
        uniqueInternshipList.add(internship);
        assertThrows(DuplicateInternshipException.class, () -> uniqueInternshipList.add(internship));
    }

    @Test
    public void add_duplicateNameInternship_throwsDuplicateInternshipException() {
        UniqueInternshipList uniqueInternshipList = new UniqueInternshipList();

        Internship internship = new InternshipBuilder(SOFTWARE_ENGINEER_WITH_DATETIME).build();
        Internship internshipWithSameName = new InternshipBuilder(SOFTWARE_ENGINEER_WITHOUT_DATETIME).build();

        uniqueInternshipList.add(internship);
        assertThrows(DuplicateInternshipException.class, () -> uniqueInternshipList.add(internshipWithSameName));
    }

    @Test
    public void removeValidInternshipSuccess() {
        UniqueInternshipList uniqueInternshipList = new UniqueInternshipList();
        Internship internship = new InternshipBuilder(SOFTWARE_ENGINEER_WITH_DATETIME).build();

        uniqueInternshipList.add(internship);
        uniqueInternshipList.remove(internship);
        assertFalse(uniqueInternshipList.contains(internship));
    }

    @Test
    public void remove_nullInternship_throwsNullPointerException() {
        UniqueInternshipList uniqueInternshipList = new UniqueInternshipList();
        assertThrows(NullPointerException.class, () -> uniqueInternshipList.remove(null));
    }

    @Test
    public void remove_internshipNotInList_throwsInternshipNotFoundException() {
        UniqueInternshipList uniqueInternshipList = new UniqueInternshipList();
        Internship internship = new InternshipBuilder().build();
        assertThrows(InternshipNotFoundException.class, () -> uniqueInternshipList.remove(internship));
    }

    @Test
    public void setInternshipValidEdit() {
        UniqueInternshipList uniqueInternshipList = new UniqueInternshipList();
        Internship firstInternship = new InternshipBuilder(SOFTWARE_ENGINEER_WITH_DATETIME).build();
        Internship secondInternship = new InternshipBuilder(DATA_ANALYST_WITH_DATETIME).build();

        uniqueInternshipList.add(firstInternship);
        uniqueInternshipList.setInternship(firstInternship, secondInternship);

        assertEquals(uniqueInternshipList.get(0), secondInternship);
    }
    @Test
    public void setInternship_nullTargetInternship_throwsNullPointerException() {
        UniqueInternshipList uniqueInternshipList = new UniqueInternshipList();
        Internship internship = new InternshipBuilder().build();
        assertThrows(NullPointerException.class, () -> uniqueInternshipList.setInternship(null, internship));
    }

    @Test
    public void setInternship_nullEditedInternship_throwsNullPointerException() {
        UniqueInternshipList uniqueInternshipList = new UniqueInternshipList();
        Internship internship = new InternshipBuilder().build();
        assertThrows(NullPointerException.class, () -> uniqueInternshipList.setInternship(internship, null));
    }

    @Test
    public void setInternship_targetInternshipNotInList_throwsInternshipNotFoundException() {
        UniqueInternshipList uniqueInternshipList = new UniqueInternshipList();
        Internship targetInternship = new InternshipBuilder().build();
        Internship editedInternship = new InternshipBuilder().build();
        assertThrows(InternshipNotFoundException.class, () -> uniqueInternshipList
                .setInternship(targetInternship, editedInternship));
    }

    @Test
    public void setInternship_editedInternshipIsDuplicate_throwsDuplicateInternshipException() {
        // This is the case where we try to add in 2 internships with the same name.
        UniqueInternshipList uniqueInternshipList = new UniqueInternshipList();

        Internship targetInternship = new InternshipBuilder(SOFTWARE_ENGINEER_WITH_DATETIME).build();
        Internship otherInternship = new InternshipBuilder(DATA_ANALYST_WITH_DATETIME).build();
        Internship editedInternship = new InternshipBuilder(DATA_ANALYST_WITH_DATETIME).build();

        uniqueInternshipList.add(targetInternship);
        uniqueInternshipList.add(otherInternship);

        assertThrows(DuplicateInternshipException.class, () -> uniqueInternshipList
                .setInternship(targetInternship, editedInternship));
    }

    @Test
    public void getInternshipsAsSortedObservableListCheckCorrectSorting() {
        UniqueInternshipList uniqueInternshipList = new UniqueInternshipList();
        Internship firstInternship = new InternshipBuilder(SOFTWARE_ENGINEER_WITH_DATETIME).build();
        Internship secondInternship = new InternshipBuilder(DATA_ANALYST_WITH_DATETIME).build();

        uniqueInternshipList.add(secondInternship);
        uniqueInternshipList.add(firstInternship);

        ObservableList<Internship> sortedList = uniqueInternshipList.getInternshipsAsSortedObservableList();
        assertEquals(firstInternship, sortedList.get(0));
        assertEquals(secondInternship, sortedList.get(1));
    }

    @Test
    public void setInternshipsWithUniqueInternshipListSuccess() {
        UniqueInternshipList uniqueInternshipList = new UniqueInternshipList();
        Internship firstInternship = new InternshipBuilder(SOFTWARE_ENGINEER_WITH_DATETIME).build();
        Internship secondInternship = new InternshipBuilder(DATA_ANALYST_WITH_DATETIME).build();

        uniqueInternshipList.add(secondInternship);
        uniqueInternshipList.add(firstInternship);

        UniqueInternshipList replacementInternshipList = new UniqueInternshipList();
        Internship thirdInternship = new InternshipBuilder(MARKETING_INTERN_WITHOUT_DATETIME).build();
        Internship fourthInternship = new InternshipBuilder(SOFTWARE_ENGINEER_WITHOUT_DATETIME).build();

        replacementInternshipList.add(thirdInternship);
        replacementInternshipList.add(fourthInternship);

        uniqueInternshipList.setInternships(replacementInternshipList);

        assertEquals(uniqueInternshipList, replacementInternshipList);
    }

    @Test
    public void setInternshipsWithNullUniqueInternshipList_throwsNullPointerException() {
        UniqueInternshipList uniqueInternshipList = new UniqueInternshipList();
        Internship firstInternship = new InternshipBuilder(SOFTWARE_ENGINEER_WITH_DATETIME).build();
        Internship secondInternship = new InternshipBuilder(DATA_ANALYST_WITH_DATETIME).build();

        uniqueInternshipList.add(secondInternship);
        uniqueInternshipList.add(firstInternship);


        assertThrows(NullPointerException.class, ()->uniqueInternshipList.setInternships((UniqueInternshipList) null));
    }

    @Test
    public void setInternshipsWithValidListSuccess() {
        UniqueInternshipList uniqueInternshipList = new UniqueInternshipList();
        Internship firstInternship = new InternshipBuilder(SOFTWARE_ENGINEER_WITH_DATETIME).build();
        Internship secondInternship = new InternshipBuilder(DATA_ANALYST_WITH_DATETIME).build();

        uniqueInternshipList.add(secondInternship);
        uniqueInternshipList.add(firstInternship);

        List<Internship> replacementInternshipList = new ArrayList<>();
        Internship thirdInternship = new InternshipBuilder(MARKETING_INTERN_WITHOUT_DATETIME).build();
        Internship fourthInternship = new InternshipBuilder(SOFTWARE_ENGINEER_WITHOUT_DATETIME).build();
        replacementInternshipList.add(thirdInternship);
        replacementInternshipList.add(fourthInternship);

        uniqueInternshipList.setInternships(replacementInternshipList);

        UniqueInternshipList uniqueInternshipListReplacement = new UniqueInternshipList();
        uniqueInternshipListReplacement.add(thirdInternship);
        uniqueInternshipListReplacement.add(fourthInternship);

        assertEquals(uniqueInternshipList, uniqueInternshipListReplacement);
    }

    @Test
    public void setInternshipsWithDuplicateListFailure_throwsDuplicateInternshipException() {
        UniqueInternshipList uniqueInternshipList = new UniqueInternshipList();
        Internship firstInternship = new InternshipBuilder(SOFTWARE_ENGINEER_WITH_DATETIME).build();
        Internship secondInternship = new InternshipBuilder(DATA_ANALYST_WITH_DATETIME).build();

        uniqueInternshipList.add(secondInternship);
        uniqueInternshipList.add(firstInternship);

        List<Internship> replacementInternshipList = new ArrayList<>();
        Internship thirdInternship = new InternshipBuilder(SOFTWARE_ENGINEER_WITHOUT_DATETIME).build();
        Internship fourthInternship = new InternshipBuilder(SOFTWARE_ENGINEER_WITHOUT_DATETIME).build();
        replacementInternshipList.add(thirdInternship);
        replacementInternshipList.add(fourthInternship);

        assertThrows(DuplicateInternshipException.class, () -> uniqueInternshipList
                .setInternships(replacementInternshipList));
    }

    @Test
    public void setInternshipsWithNullList_throwsNullPointerException() {
        UniqueInternshipList uniqueInternshipList = new UniqueInternshipList();
        Internship firstInternship = new InternshipBuilder(SOFTWARE_ENGINEER_WITH_DATETIME).build();
        Internship secondInternship = new InternshipBuilder(DATA_ANALYST_WITH_DATETIME).build();

        uniqueInternshipList.add(secondInternship);
        uniqueInternshipList.add(firstInternship);


        assertThrows(NullPointerException.class, ()->uniqueInternshipList.setInternships((List<Internship>) null));
    }

    @Test
    public void iterator_validInternships_success() {
        UniqueInternshipList uniqueInternshipList = new UniqueInternshipList();
        List<Internship> typicalInternships = new ArrayList<>();
        Internship firstInternship = new InternshipBuilder(SOFTWARE_ENGINEER_WITH_DATETIME).build();
        Internship secondInternship = new InternshipBuilder(DATA_ANALYST_WITH_DATETIME).build();

        typicalInternships.add(secondInternship);
        typicalInternships.add(firstInternship);

        uniqueInternshipList.setInternships(typicalInternships);

        // Test that the iterator correctly iterates over the internships
        Iterator<Internship> iterator = uniqueInternshipList.iterator();
        for (Internship internship : typicalInternships) {
            assertTrue(iterator.hasNext());
            assertEquals(internship, iterator.next());
        }
    }

    @Test
    public void equals_sameObject_true() {
        UniqueInternshipList uniqueInternshipList = new UniqueInternshipList();
        uniqueInternshipList.add(new InternshipBuilder().build());

        // Both references point to the same object, so they should be equal
        assertTrue(uniqueInternshipList.equals(uniqueInternshipList));
    }

    @Test
    public void equals_differentTypes_false() {
        UniqueInternshipList uniqueInternshipList = new UniqueInternshipList();

        // Comparing with a different type should return false
        assertFalse(uniqueInternshipList.equals(5));
    }

    @Test
    public void internshipListEqualsNull_returnFalse() {
        UniqueInternshipList uniqueInternshipList = new UniqueInternshipList();

        // Comparing with null should return false
        assertFalse(uniqueInternshipList.equals(null));
    }

    @Test
    public void equals_differentInternalList_false() {
        UniqueInternshipList uniqueInternshipList = new UniqueInternshipList();
        UniqueInternshipList otherUniqueInternshipList = new UniqueInternshipList();
        uniqueInternshipList.add(new InternshipBuilder(SOFTWARE_ENGINEER_WITH_DATETIME).build());
        otherUniqueInternshipList.add(new InternshipBuilder(DATA_ANALYST_WITH_DATETIME).build());

        // Both lists have different internal lists, so they should not be equal
        assertFalse(uniqueInternshipList.equals(otherUniqueInternshipList));
    }

    @Test
    public void toStringMethod() {
        UniqueInternshipList uniqueInternshipList = new UniqueInternshipList();
        Internship firstInternship = new InternshipBuilder(SOFTWARE_ENGINEER_WITH_DATETIME).build();
        Internship secondInternship = new InternshipBuilder(DATA_ANALYST_WITH_DATETIME).build();

        uniqueInternshipList.add(secondInternship);
        uniqueInternshipList.add(firstInternship);

        assertEquals(uniqueInternshipList.asUnmodifiableObservableList().toString(), uniqueInternshipList.toString());
    }
}
