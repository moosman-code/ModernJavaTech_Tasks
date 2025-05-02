package bg.sofia.uni.fmi.mjt.eventbus.events;

import java.time.Instant;

public class EventImpl<T extends Payload<?>> implements Event<T> {
    private Instant timestamp;
    private int priority;
    private String source;
    private T payload;

    public EventImpl(Instant timestamp, int priority, String source, T payload) {
        this.timestamp = timestamp;
        this.priority = priority;
        this.source = source;
        this.payload = payload;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public T getPayload() {
        return payload;
    }

    @Override
    public int compareTo(Event<T> other) {
        int priorityComparison = Integer.compare(other.getPriority(), this.priority);
        if (priorityComparison != 0) {
            return priorityComparison;
        }

        return this.timestamp.compareTo(other.getTimestamp());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Event<T> other = (Event<T>) obj;
        return priority == other.getPriority() && 
                source.equals(other.getSource()) &&
                timestamp.equals(other.getTimestamp()) &&
                (payload == null ? other.getPayload() == null : payload.equals(other.getPayload()));
    }

    @Override
    public int hashCode() {
        return timestamp.hashCode() + payload.hashCode();
    }
}
