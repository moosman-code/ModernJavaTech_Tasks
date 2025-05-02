package bg.sofia.uni.fmi.mjt.eventbus;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;
import bg.sofia.uni.fmi.mjt.eventbus.events.Payload;
import bg.sofia.uni.fmi.mjt.eventbus.exception.MissingSubscriptionException;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.Subscriber;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class EventBusImpl implements EventBus {

    private Map<Class<?>, Collection<Subscriber<?>>> subscribers;
    private Map<Class<?>, Set<Event<? extends Payload<?>>>> eventLog;

    public EventBusImpl() {
        this.subscribers = new HashMap<>();
        this.eventLog = new HashMap<>();
    }

    @Override
    public <T extends Event<?>> void subscribe(Class<T> eventType, Subscriber<? super T> subscriber) {
        if (eventType == null || subscriber == null) {
            throw new IllegalArgumentException("Event type and subscriber cannot be null");
        }

        if (!subscribers.containsKey(eventType)) {
            subscribers.put(eventType, new HashSet<>());
        }
        subscribers.get(eventType).add(subscriber);
    }

    @Override
    public <T extends Event<?>> void unsubscribe(Class<T> eventType, Subscriber<? super T> subscriber) throws MissingSubscriptionException {
        if (eventType == null || subscriber == null) {
            throw new IllegalArgumentException("Event type and subscriber cannot be null");
        }

        try {
            if (subscribers.containsKey(eventType)) {
                subscribers.get(eventType).remove(subscriber);
            }
        }
        catch(MissingSubscriptionException e) {
            throw e;
        }
    }

    @Override
    public <T extends Event<?>> void publish(T event) {
        if (!eventLog.containsKey(event.getClass())) {
            eventLog.put(event.getClass(), new HashSet<>());
        }
        eventLog.get(event.getClass()).add(event);

        if (subscribers.get(event.getClass()) != null) {
            for (Subscriber<?> subscriber : subscribers.get(event.getClass())) {
                Subscriber<T> castedSubscriber = (Subscriber<T>) subscriber;
                castedSubscriber.onEvent(event);
            }
        }
    }

    @Override
    public void clear() {
        subscribers.clear();
        eventLog.clear();
    }

    @Override
    public Collection<? extends Event<?>> getEventLogs(Class<? extends Event<?>> eventType, Instant from, Instant to) {
        if (eventType == null || from == null || to == null) {
            throw new IllegalArgumentException("Event type, start and end of date cannot be null");
        }

        List<Event<?>> filteredEvents = new ArrayList<>();
        if (eventLog.isEmpty()) {
            return List.copyOf(filteredEvents);
        }
        for (Event<?> event : eventLog.get(eventType)) {
            if (event.getTimestamp().isAfter(from) && event.getTimestamp().isBefore(to)) {
                filteredEvents.add(event);
            }
        }

        return List.copyOf(filteredEvents);
    }

    @Override
    public <T extends Event<?>> Collection<Subscriber<?>> getSubscribersForEvent(Class<T> eventType) {
        if (subscribers.isEmpty()) {
            return List.of();
        }

        return List.copyOf(subscribers.get(eventType));
    }
}
