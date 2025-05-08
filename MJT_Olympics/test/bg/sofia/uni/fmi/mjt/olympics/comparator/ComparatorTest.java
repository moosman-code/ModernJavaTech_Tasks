package bg.sofia.uni.fmi.mjt.olympics.comparator;

import bg.sofia.uni.fmi.mjt.olympics.MJTOlympics;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ComparatorTest {

    private MJTOlympics olympicsMock = Mockito.mock(MJTOlympics.class);

    private final NationMedalComparator classUnderTest = new NationMedalComparator(olympicsMock);

    @Test
    void testCompareWhenNation1IsNull() {
        assertThrows(IllegalArgumentException.class, () -> classUnderTest.compare(null, "nation2"));
    }

    @Test
    void testCompareWhenNation2IsNull() {
        assertThrows(IllegalArgumentException.class, () -> classUnderTest.compare("nation1", null));
    }

    @Test
    void testWhenNation1IsGreaterThanNation2() {
        when(olympicsMock.getTotalMedals("Chilli")).thenReturn(2);
        when(olympicsMock.getTotalMedals("Brazil")).thenReturn(3);

        assertEquals(-1, classUnderTest.compare("Chilli", "Brazil"));

    }

    @Test
    void testWhenNation2IsGreaterThanNation1() {
        when(olympicsMock.getTotalMedals("Chilli")).thenReturn(3);
        when(olympicsMock.getTotalMedals("Brazil")).thenReturn(2);

        assertEquals(1, classUnderTest.compare("Chilli", "Brazil"));
    }

    @Test
    void testWhenNationsAreEquivalent() {
        when(olympicsMock.getTotalMedals("Chilli")).thenReturn(2);
        when(olympicsMock.getTotalMedals("Brazil")).thenReturn(2);

        assertEquals(1, classUnderTest.compare("Chilli", "Brazil"));
    }
}
