package bg.sofia.uni.fmi.mjt.olympics.comparator;

import bg.sofia.uni.fmi.mjt.olympics.MJTOlympics;
import bg.sofia.uni.fmi.mjt.olympics.Olympics;

import java.util.Comparator;

public class NationMedalComparator implements Comparator<String> {

    private MJTOlympics olympics;

    public NationMedalComparator(MJTOlympics olympics) {
        this.olympics = olympics;
    }

    @Override
    public int compare(String nation1, String nation2) {
        if (nation1 == null ||  nation2 == null) {
            throw new IllegalArgumentException("Nations cannot be null");
        }

        int totalMedals1 = olympics.getTotalMedals(nation1);
        int totalMedals2 = olympics.getTotalMedals(nation2);

        if (totalMedals1 != totalMedals2) {
            return Integer.compare(totalMedals1, totalMedals2);
        }

        return nation1.compareTo(nation2);
    }
}