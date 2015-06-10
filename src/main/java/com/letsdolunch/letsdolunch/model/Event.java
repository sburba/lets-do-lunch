package com.letsdolunch.letsdolunch.model;

import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Event {
    public final long id;
    public final String location;
    public final LocalDateTime time;
    public final List<String> people;
    public final List<String> comments;

    public Event(long id, String location, LocalDateTime time, List<String> people, List<String> comments) {
        this.id = id;
        this.location = location;
        this.time = time;
        this.people = people;
        this.comments = comments;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long id;
        private String location;
        private LocalDateTime time;
        private List<String> people = new ArrayList<>();
        private List<String> comments = new ArrayList<>();

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder time(String timeString) throws DateTimeParseException {
            LocalTime time = guessIntendedTime(timeString);
            this.time = time.atDate(LocalDate.now());
            return this;
        }

        public Builder addPerson(String name) {
            this.people.add(name);
            return this;
        }

        public Builder addComment(String comment) {
            if (!StringUtils.isEmpty(comment)) {
                this.comments.add(comment);
            }
            return this;
        }

        public LocalTime guessIntendedTime(String timeStr) {
            timeStr = timeStr.replaceAll("\\s+", "").toUpperCase(Locale.US);
            // If they actually specify AM or PM then they meant it
            if (timeStr.contains("AM") || timeStr.contains("PM")) {
                return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("h:mma"));
            }

            // If they didn't specify am or pm, force it to be between 8am and 8pm
            LocalTime time = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("H:mm"));
            if (time.isBefore(LocalTime.of(8, 0))) {
                return time.plusHours(12);
            } else if (time.isAfter(LocalTime.of(20, 0))) {
                return time.minusHours(12);
            } else {
                return time;
            }
        }

        public Event build() {
            return new Event(id, location, time, people, comments);
        }
    }
}
