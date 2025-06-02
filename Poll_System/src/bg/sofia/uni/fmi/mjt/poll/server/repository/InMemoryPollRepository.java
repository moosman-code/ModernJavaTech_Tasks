package bg.sofia.uni.fmi.mjt.poll.server.repository;

import bg.sofia.uni.fmi.mjt.poll.server.model.Poll;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryPollRepository implements PollRepository {

    public ConcurrentHashMap<Integer, Poll> polls;
    private Integer nextFreeId = 1;

    public InMemoryPollRepository() {
        polls = new ConcurrentHashMap<>();
    }

    @Override
    public int addPoll(Poll poll) {
        polls.put(nextFreeId, poll);
        return nextFreeId++;
    }

    @Override
    public Poll getPoll(int pollId) {
        return polls.get(pollId);
    }

    @Override
    public Map<Integer, Poll> getAllPolls() {
        return polls;
    }

    @Override
    public void clearAllPolls() {
        polls.clear();
    }
}
