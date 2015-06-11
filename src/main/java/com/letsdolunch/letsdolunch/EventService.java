package com.letsdolunch.letsdolunch;

import com.letsdolunch.letsdolunch.model.Event;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

@Service
public class EventService {
    private final List<Event> events = new ArrayList<>();

    /**
     * Takes an event without an id, inserts it into the database, and returns the inserted event
     */
    public Event create(Event event) {
        Event newEvent = event.withId(events.size());
        events.add(newEvent);
        return newEvent;
    }

    public List<Event> getAll() {
        return events;
    }

    public Event get(int eventId) {
        return events.get(eventId);
    }

    public Event update(Event event) {
        checkArgument(event.id().isPresent(), "Event must have an id to be updated");
        // Dirty cast to int since we don't have a real database
        int eventId = event.id().get().intValue();
        events.set(eventId, event);
        return event;
    }
}
