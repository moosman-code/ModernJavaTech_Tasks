package bg.sofia.uni.fmi.mjt.olympics.competitor;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AthleteTest {
    private Competitor firstAthlete;
    private Competitor secondAthlete;
    private Competitor thirdAthlete;

    @BeforeEach
    void setUp() {
        firstAthlete = new Athlete("1", "Marcus", "Chilli");
        firstAthlete.addMedal(Medal.SILVER);
        secondAthlete = new Athlete("2", "Augustus", "Argentina");
        thirdAthlete = new Athlete("3", "Marcus", "Chilli");
        thirdAthlete.addMedal(Medal.SILVER);
    }

    // addMedal Tests
    @Test
    void testAddMedalWithNullShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> firstAthlete.addMedal(null));
    }

    @Test
    void testAddMedalWithValue() {
        assertTrue(firstAthlete.getMedals().contains(Medal.SILVER));
    }

    // equals Tests
    @Test
    void testEqualsWhenNull() {
        assertFalse(firstAthlete.equals(null));
    }

    @Test
    void testEqualsForReflexivityAndSameReference() {
        assertTrue(firstAthlete.equals(firstAthlete));
    }

    @Test
    void testEqualsForSymmetry() {
        assertTrue(firstAthlete.equals(thirdAthlete) && thirdAthlete.equals(firstAthlete));
    }

    @Test
    void testEqualsForTransitivity() {
        Competitor testAthlete = new Athlete("4", "Marcus", "Chilli");
        testAthlete.addMedal(Medal.SILVER);

        assertTrue(firstAthlete.equals(thirdAthlete)
                && thirdAthlete.equals(testAthlete)
                && firstAthlete.equals(testAthlete));
    }

    // hashCode Test
    @Test
    void testIfHashCodeAndEqualsCorrespond() {
        assertTrue(firstAthlete.equals(thirdAthlete));
        assertEquals(firstAthlete.hashCode(), thirdAthlete.hashCode());
    }

    // compareTo Tests
    @Test
    void testCompareToIfOtherIsNull() {
        assertThrows(IllegalArgumentException.class, () -> firstAthlete.compareTo(null));
    }

    @Test
    void testCompareToIfFirstAthleteHasAllMedalsLikeSecondAthlete() {
        secondAthlete.addMedal(Medal.SILVER);
        assertEquals(12, firstAthlete.compareTo(secondAthlete));
    }

    @Test
    void testCompareToIfFirstAthleteHasMoreMedalsThanSecondAthlete() {
        firstAthlete.addMedal(Medal.GOLD);
        secondAthlete.addMedal(Medal.SILVER);
        assertEquals(1, firstAthlete.compareTo(secondAthlete));
    }
}
