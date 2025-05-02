import bg.sofia.uni.fmi.mjt.eventbus.EventBus;
import bg.sofia.uni.fmi.mjt.eventbus.EventBusImpl;
import bg.sofia.uni.fmi.mjt.eventbus.events.Event;
import bg.sofia.uni.fmi.mjt.eventbus.events.EventImpl;
import bg.sofia.uni.fmi.mjt.eventbus.events.Payload;
import bg.sofia.uni.fmi.mjt.eventbus.events.PayloadImpl;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.DeferredEventSubscriber;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.Subscriber;

import java.time.Instant;
import java.time.InstantSource;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        // Payload Test
        Payload<Integer> integers = new PayloadImpl<>(1, 12);
        Payload<String> strings = new PayloadImpl<>(12, "Hello World!");
        Payload<HashSet<String>> list = new PayloadImpl<>(3, new HashSet<>());
        list.getPayload().add("a");
        list.getPayload().add("tiny");
        list.getPayload().add("test");

        // Event Test
        Event<Payload<Integer>> firstEvent = new EventImpl<>(Instant.now(), 1, "Custom", integers);
        Event<Payload<String>> secondEvent = new EventImpl<>(Instant.now(), 2, "Fabric", strings);
        Event<Payload<HashSet<String>>> thirdEvent = new EventImpl<>(Instant.now().plus(2, ChronoUnit.DAYS), 3, "Synthetic", list);

        // Subscriber Test
        Subscriber<Event<Payload<Integer>>> mark = new DeferredEventSubscriber<>();
        Subscriber<Event<Payload<String>>> sean = new DeferredEventSubscriber<>();
        Subscriber<Event<Payload<HashSet<String>>>> felix = new DeferredEventSubscriber<>();
//        mark.onEvent(firstEvent);
//        sean.onEvent(secondEvent);
//        felix.onEvent(thirdEvent);

        // Subscriber Iterator Test
//        DeferredEventSubscriber<Event<?>> random = new DeferredEventSubscriber<>();
//        random.onEvent(firstEvent);
//        random.onEvent(secondEvent);
//        random.onEvent(thirdEvent);
//        Iterator<Event<?>> iterator = random.iterator();
//        while (iterator.hasNext()) {
//            Event<?> element = iterator.next();
//            System.out.println(element.getPriority());
//        }

        // EventBud Test
        EventBus eventBus = new EventBusImpl();
        eventBus.publish(firstEvent);
        eventBus.publish(secondEvent);
        eventBus.publish(thirdEvent);

        eventBus.<Event<Payload<Integer>>>subscribe((Class<Event<Payload<Integer>>>) firstEvent.getClass(), mark);
        eventBus.<Event<Payload<Integer>>>unsubscribe((Class<Event<Payload<Integer>>>) firstEvent.getClass(), mark);

        Instant yesterday = Instant.now().minus(1, ChronoUnit.DAYS);
        Instant tomorrow = Instant.now().plus(1, ChronoUnit.DAYS);
        for (Event<?> event : eventBus.getEventLogs((Class<? extends Event<?>>)firstEvent.getClass(), yesterday, tomorrow)) {
            System.out.println(event.getPriority());
        }

        for (Subscriber<?> subscriber : eventBus.getSubscribersForEvent(firstEvent.getClass())) {
            System.out.println(subscriber.hashCode());
        }
    }
}