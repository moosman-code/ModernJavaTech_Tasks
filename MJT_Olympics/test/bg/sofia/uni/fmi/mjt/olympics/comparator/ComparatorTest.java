package bg.sofia.uni.fmi.mjt.olympics.comparator;

import bg.sofia.uni.fmi.mjt.olympics.MJTOlympics;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class ComparatorTest {

    private MJTOlympics mjtOlympicsMock = Mockito.mock(MJTOlympics.class);

    private NationMedalComparator nationMedalComparator = new NationMedalComparator(mjtOlympicsMock);

    @Test
    void testComparatorIfNation1IsNull() {
        String firstNation = null;
        String secondNation = "Hungary";
        assertThrows(IllegalArgumentException.class, () -> nationMedalComparator.compare(firstNation, secondNation),
                "IllegalArgumentException was not thrown");
    }

    @Test
    void testComparatorIfNation2IsNull() {
        String firstNation = "Chilli";
        String secondNation = null;
        assertThrows(IllegalArgumentException.class, () -> nationMedalComparator.compare(firstNation, secondNation),
                "IllegalArgumentException was not thrown");
    }

    @Test
    void testComparatorIfNation1HasMoreMedals() {
        String firstNation = "Chilli";
        String secondNation = "Brazil";

        when(mjtOlympicsMock.getTotalMedals(firstNation)).thenReturn(3);
        when(mjtOlympicsMock.getTotalMedals(secondNation)).thenReturn(2);

        assertEquals(1, nationMedalComparator.compare(firstNation, secondNation));
    }

    @Test
    void testComparatorIfNationsHaveEqualsAmountOfMedals() {
        String firstNation = "Chilli";
        String secondNation = "Brazil";

        when(mjtOlympicsMock.getTotalMedals(firstNation)).thenReturn(2);
        when(mjtOlympicsMock.getTotalMedals(secondNation)).thenReturn(2);

        assertEquals(1, nationMedalComparator.compare(firstNation, secondNation));
    }
}
