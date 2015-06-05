package com.letsdolunch.letsdolunch;

import java.util.ArrayList;
import java.util.List;

public class Event {
    public final long id;
    public final List<String> people;
    public final List<String> comments;
    public final String location;
    public final String time;

    public Event(long id, String person, String location, String time) {
        this.id = id;
        this.people = new ArrayList<>();
        this.people.add(person);
        this.location = location;
        this.time = time;
        this.comments = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", people=" + people +
                ", comments=" + comments +
                ", location='" + location + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (id != event.id) return false;
        if (people != null ? !people.equals(event.people) : event.people != null) return false;
        if (comments != null ? !comments.equals(event.comments) : event.comments != null) return false;
        if (location != null ? !location.equals(event.location) : event.location != null) return false;
        return !(time != null ? !time.equals(event.time) : event.time != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (people != null ? people.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }
}
