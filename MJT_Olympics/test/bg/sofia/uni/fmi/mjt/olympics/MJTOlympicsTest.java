package bg.sofia.uni.fmi.mjt.olympics;

import bg.sofia.uni.fmi.mjt.olympics.competition.Competition;
import bg.sofia.uni.fmi.mjt.olympics.competition.CompetitionResultFetcher;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Medal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MJTOlympicsTest {
    private Set<Competitor> registeredCompetitors;
    private final CompetitionResultFetcher competitionResultFetcherMock = Mockito.mock(CompetitionResultFetcher.class);

    private MJTOlympics mjtOlympics;

    @BeforeEach
    void setUp() {
        Competitor athlete = new Athlete("1", "Garen", "Demacia");
        Set<Competitor> competitors = new HashSet<>();
        competitors.add(athlete);
        registeredCompetitors = competitors;
            mjtOlympics = new MJTOlympics(registeredCompetitors, competitionResultFetcherMock);
    }

    // updateMedalStatistics Tests
    @Test
    void testUpdateMedalStatisticsWhenCompetitorsIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> mjtOlympics.updateMedalStatistics(null),
                "IllegalArgumentException was not thrown");
    }

    @Test
    void testUpdateMedalStatisticsWhenCompetitorIsNotRegistered() {
        Competitor notPresentAthlete = new Athlete("2", "Will", "Jamaica");

        Set<Competitor> notPresentCompetitors = new HashSet<>();
        notPresentCompetitors.add(notPresentAthlete);

        Competition competition = new Competition("New Delhi", "Bicycling", notPresentCompetitors);

        assertThrows(IllegalArgumentException.class,
                () -> mjtOlympics.updateMedalStatistics(competition),
                "IllegalArgumentException was not thrown");
    }

    // getTotalMedals
    @Test
    void testGetTotalMedalsWhenNationalityIsNull() {
        assertThrows(IllegalArgumentException.class, () -> mjtOlympics.getTotalMedals(null),
                "IllegalArgumentException was not thrown");
    }

    @Test
    void testGetTotalMedalsWhenNationalityIsNotRegistered() {
        Map<String, EnumMap<Medal, Integer>> nationsMedalsTable = new HashMap<>();
        EnumMap<Medal, Integer> medals = new EnumMap<>(Medal.class);
        medals.put(Medal.SILVER, 2);
        nationsMedalsTable.put("Chilli", medals);


        assertThrows(IllegalArgumentException.class, () -> mjtOlympics.getTotalMedals("Brazil"),
                "IllegalArgumentException was not thrown");
    }
}
