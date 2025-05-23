package bg.sofia.uni.fmi.mjt.olympics.competition;

import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;

import java.util.Set;
import java.util.TreeSet;

public interface CompetitionResultFetcher {

    /**
     * Fetches the results of a given competition.
     * The result is a ranking leaderboard that orders the Competitors by their performance in the competition.
     *
     * @param competition the competition to fetch the results from
     * @return a TreeSet of competitors ranked by their performance in the competition
     */
    default TreeSet<Competitor> getResult(Competition competition) {
        if (competition == null) {
            throw new IllegalArgumentException("Competition cannot be null");
        }

        return new TreeSet<>(competition.competitors());
    }
}