package bg.sofia.uni.fmi.mjt.olympics;

import bg.sofia.uni.fmi.mjt.olympics.competition.Competition;
import bg.sofia.uni.fmi.mjt.olympics.competition.CompetitionResultFetcher;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Medal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MJTOlympicsTest {

    private MJTOlympics olympics;
    private Competitor c1, c2, c3;
    private CompetitionResultFetcher fetcherMock = mock(CompetitionResultFetcher.class);
    private Competition competition;

    @BeforeEach
    void setUp() {
        // Sample competitors
        c1 = new Athlete("1", "Alice", "USA");
        c2 = new Athlete("2", "Bob", "Germany");
        c3 = new Athlete("3", "Charlie", "UK");

        Set<Competitor> competitors = Set.of(c1, c2, c3);
        competition = new Competition("Chilli", "100m Sprint", competitors);

        olympics = new MJTOlympics(competitors, fetcherMock);
    }

    // getTotalMedals Tests
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

    @ParameterizedTest
    @ValueSource(strings = {"", "  "})
    @NullSource
    void testGetTotalMedalsWithInvalidNationThrows(String input) {
        assertThrows(IllegalArgumentException.class, () -> olympics.getTotalMedals(input));
    }

    // updateMedalStatistics Tests
    @Test
    void testUpdateMedalStatisticsWithNullCompetitionThrows() {
        assertThrows(IllegalArgumentException.class, () -> olympics.updateMedalStatistics(null));
    }

    @Test
    void testUpdateMedalStatisticsCheckIfGetResultIsCalledOnlyOnce() {
        Competition competition = mock(Competition.class);
        TreeSet<Competitor> emptyRanking = new TreeSet<>();

        when(fetcherMock.getResult(competition)).thenReturn(emptyRanking);

        olympics.updateMedalStatistics(competition);

        verify(fetcherMock, times(1)).getResult(competition);
    }

    @Test
    void testUpdateMedalStatisticsAssignsCorrectMedalsToTop3() {
        Competitor c1 = mock(Competitor.class);
        when(c1.getNationality()).thenReturn("USA");

        Competitor c2 = mock(Competitor.class);
        when(c2.getNationality()).thenReturn("Germany");

        Competitor c3 = mock(Competitor.class);
        when(c3.getNationality()).thenReturn("Japan");

        TreeSet<Competitor> ranking = new TreeSet<>((a, b) -> {
            if (a == c1) return -1;
            if (a == c2 && b != c1) return -1;
            return 1;
        });
        ranking.add(c1);
        ranking.add(c2);
        ranking.add(c3);

        Competition competition = mock(Competition.class);
        when(fetcherMock.getResult(competition)).thenReturn(ranking);

        MJTOlympics olympics = new MJTOlympics(Set.of(c1, c2, c3), fetcherMock);

        olympics.updateMedalStatistics(competition);

        verify(c1).addMedal(Medal.GOLD);
        verify(c2).addMedal(Medal.SILVER);
        verify(c3).addMedal(Medal.BRONZE);

        Map<String, EnumMap<Medal, Integer>> medalTable = olympics.getNationsMedalTable();
        assertEquals(-1, medalTable.get("USA").get(Medal.GOLD));
        assertEquals(-1, medalTable.get("Germany").get(Medal.SILVER));
        assertEquals(-1, medalTable.get("Japan").get(Medal.BRONZE));
    }

    // getNationsRackList Test
    @Test
    void testGetNationsRankListReturnsCorrectRanking() {
        MJTOlympics olympics = new MJTOlympics(Set.of(), null);

        Map<String, EnumMap<Medal, Integer>> table = olympics.getNationsMedalTable();

        EnumMap<Medal, Integer> usaMedals = new EnumMap<>(Medal.class);
        usaMedals.put(Medal.GOLD, 3);
        usaMedals.put(Medal.SILVER, 1);
        table.put("USA", usaMedals);

        EnumMap<Medal, Integer> germanyMedals = new EnumMap<>(Medal.class);
        germanyMedals.put(Medal.GOLD, 2);
        germanyMedals.put(Medal.SILVER, 2);
        table.put("Germany", germanyMedals);

        EnumMap<Medal, Integer> japanMedals = new EnumMap<>(Medal.class);
        japanMedals.put(Medal.GOLD, 1);
        table.put("Japan", japanMedals);

        TreeSet<String> rankList = olympics.getNationsRankList();

        List<String> expectedOrder = List.of("Japan", "Germany", "USA");
        assertEquals(expectedOrder, new ArrayList<>(rankList));
    }
}
