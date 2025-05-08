package bg.sofia.uni.fmi.mjt.olympics.competition;

import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class CompetitionTest {
    private Competitor athlete;
    private Set<Competitor> competitors;
    private Competition firstCompetition;
    private Competition thirdCompetition;

    @BeforeEach
    void setUp() {
        competitors = new HashSet<>();
        athlete = new Athlete("1", "Marcus", "Chilli");
        competitors.add(athlete);

        firstCompetition = new Competition("Salvador", "Swimming", competitors);
        thirdCompetition = new Competition("Salvador", "Swimming", competitors);
    }

    // Constructor Tests
    @Test
    void testCompetitionWithNullName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Competition(null, "Triathlon", new HashSet<>()),
                "IllegalArgumentException exception not thrown");
    }

    @Test
    void testCompetitionWithNullDiscipline() {
        assertThrows(IllegalArgumentException.class,
                () -> new Competition("Tuer du France", null, competitors),
                "IllegalArgumentException exception not thrown");
    }

    @Test
    void testCompetitionWithNullCompetitors() {
        assertThrows(IllegalArgumentException.class,
                () -> new Competition("Tuer du France", "Triathlon", null),
                "IllegalArgumentException exception not thrown");
    }

    @Test
    void testCompetitionWithEmptyCompetitors() {
        assertThrows(IllegalArgumentException.class,
                () -> new Competition("Tuer du France", "Triathlon", new HashSet<>()),
                "IllegalArgumentException exception not thrown");
    }

    // competitors Tests
    @Test
    void testIfCompetitorsReturnsCorrectly() {
        assertEquals(competitors, firstCompetition.competitors(),
                "Competitors function doesn't return a valid set");
    }

    @Test
    void testIfCompetitorsReturnsUnmodifiableSet() {
        assertThrows(UnsupportedOperationException.class,
                () -> firstCompetition.competitors().add(athlete),
                "UnsupportedOperationException exception not thrown");
    }

    // equals Tests
    @Test
    void testEqualsWhenNull() {
        assertFalse(firstCompetition.equals(null));
    }

    @Test
    void testEqualsForReflexivityAndSameReference() {
        assertTrue(firstCompetition.equals(firstCompetition));
    }

    @Test
    void testEqualsForSymmetry() {
        assertTrue(firstCompetition.equals(thirdCompetition) && thirdCompetition.equals(firstCompetition));
    }

    @Test
    void testEqualsForTransitivity() {
        Competition testCompetition = new Competition("Salvador", "Swimming", competitors);

        assertTrue(firstCompetition.equals(thirdCompetition)
                && thirdCompetition.equals(testCompetition)
                && firstCompetition.equals(testCompetition));
    }

    //  interface Tests
    @Test
    void testFetcherIfCompetitionIsNull() {
        CompetitionResultFetcher fetcher = new CompetitionResultFetcher() {};
        assertThrows(IllegalArgumentException.class, () -> fetcher.getResult(null));
    }

    @Test
    void testGetResultReturnsEmptyTreeSetWhenNoCompetitors() {
        Set<Competitor> emptySet = new HashSet<>();
        Competition competition = Mockito.mock(Competition.class);
        when(competition.competitors()).thenReturn(emptySet);

        CompetitionResultFetcher fetcher = new CompetitionResultFetcher() {};

        TreeSet<Competitor> result = fetcher.getResult(competition);

        assertTrue(result.isEmpty());
    }

    // hashCode Test
    @Test
    void testIfHashCodeAndEqualsCorrespond() {
        assertTrue(firstCompetition.equals(thirdCompetition));
        assertEquals(firstCompetition.hashCode(), thirdCompetition.hashCode());
    }
}
