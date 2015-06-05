package com.letsdolunch.letsdolunch;

import java.util.ArrayList;
import java.util.List;

public class Event {
    public long id;
    public List<String> people;
    public List<String> comments;
    public String location;
    public String time;

    public Event(long id, String person, String location, String time) {
        this.id = id;
        this.people = new ArrayList<>();
        this.people.add(person);
        this.location = location;
        this.time = time;
        this.comments = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (id != event.id) return false;
        if (people != null ? !people.equals(event.people) : event.people != null) return false;
        return !(location != null ? !location.equals(event.location) : event.location != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (people != null ? people.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", people=" + people +
                ", location='" + location + '\'' +
                '}';
    }
}
