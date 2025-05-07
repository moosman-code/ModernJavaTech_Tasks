package bg.sofia.uni.fmi.mjt.olympics;

import bg.sofia.uni.fmi.mjt.olympics.competition.Competition;
import bg.sofia.uni.fmi.mjt.olympics.competition.CompetitionResultFetcher;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Medal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MJTOlympicsTest {

    private MJTOlympics olympics;
    private Competitor c1, c2, c3;
    private Competition competition;

    @BeforeEach
    void setUp() {
        // Sample competitors
        c1 = new Athlete("1", "Alice", "USA");
        c2 = new Athlete("2", "Bob", "Germany");
        c3 = new Athlete("3", "Charlie", "UK");

        Set<Competitor> competitors = Set.of(c1, c2, c3);
        competition = new Competition("Chilli", "100m Sprint", competitors);

        // Use real (default) implementation of the interface
        CompetitionResultFetcher fetcher = new CompetitionResultFetcher() {};

        olympics = new MJTOlympics(competitors, fetcher);
    }

    @Test
    void testGetTotalMedalsWithNoMedalsThrows() {
        // Add a nation with an empty EnumMap for medals
        olympics.getNationsMedalTable().put("Germany", new EnumMap<>(Medal.class));

        assertThrows(IllegalArgumentException.class, () -> olympics.getTotalMedals("Germany"));
    }

    @Test
    void testGetTotalMedalsReturnsCorrectCount() {
        EnumMap<Medal, Integer> medalCount = new EnumMap<>(Medal.class);
        medalCount.put(Medal.GOLD, 2);
        medalCount.put(Medal.SILVER, 1);

        olympics.getNationsMedalTable().put("USA", medalCount);

        int total = olympics.getTotalMedals("USA");
        assertEquals(3, total);
    }

    @Test
    void testUpdateMedalStatisticsWithNullCompetitionThrows() {
        assertThrows(IllegalArgumentException.class, () -> olympics.updateMedalStatistics(null));
    }

    @Test
    void testGetTotalMedalsWithInvalidNationThrows() {
        assertThrows(IllegalArgumentException.class, () -> olympics.getTotalMedals(null));
        assertThrows(IllegalArgumentException.class, () -> olympics.getTotalMedals(""));
        assertThrows(IllegalArgumentException.class, () -> olympics.getTotalMedals("  "));
    }
}
