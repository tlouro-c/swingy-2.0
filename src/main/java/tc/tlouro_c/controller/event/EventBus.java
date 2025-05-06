package tc.tlouro_c.controller.event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class EventBus {

    // This holds all listener wrappers — each keeps its own type info
    private final List<ListenerWrapper<?>> listeners = new ArrayList<>();

    // Generic method to subscribe a typed listener
    public <T> void subscribe(Class<T> eventType, Consumer<T> listener) {
        listeners.add(new ListenerWrapper<>(eventType, listener));
    }

    // Publish any event — will notify all matching (or superclass/interface) listeners
    public void publish(Object event) {
        for (ListenerWrapper<?> wrapper : listeners) {
            wrapper.invokeIfMatches(event);
        }
    }

    // Add to EventBus class
    public <T> void unsubscribe(Class<T> eventType) {
        listeners.removeIf(wrapper -> wrapper.eventType().equals(eventType));
    }

    // Inner class that wraps a listener with its expected type
        private record ListenerWrapper<T>(Class<T> eventType, Consumer<T> listener) {

        void invokeIfMatches(Object event) {
                if (eventType.isInstance(event)) {
                    T casted = eventType.cast(event); // safe cast
                    listener.accept(casted);
                }
            }
        }



        // Singleton
    private EventBus() {}

    private static class Holder {
        private static final EventBus INSTANCE = new EventBus();
    }

    public static EventBus getInstance() {
        return Holder.INSTANCE;
    }
}
