package com.letsdolunch.letsdolunch.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.*;
import static com.google.common.base.Strings.isNullOrEmpty;

@AutoValue
public abstract class Event {
    @JsonProperty("id")
    public abstract Optional<Long> id();
    @JsonProperty("location")
    public abstract String location();
    @JsonProperty("time")
    public abstract OffsetDateTime time();
    @JsonProperty("people")
    public abstract ImmutableList<String> people();
    @JsonProperty("comments")
    public abstract ImmutableList<String> comments();

    Event(){}

    @JsonCreator
    private static Event create(@JsonProperty("location") String location, @JsonProperty("time") OffsetDateTime time,
                                @JsonProperty("creator") String creator) {
        return builder().location(location).time(time).addPerson(creator).build();
    }

    abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_Event.Builder()
                .id(Optional.empty())
                .people(ImmutableList.of())
                .comments(ImmutableList.of());
    }

    public Event withId(long id) {
        return toBuilder().id(id).build();
    }

    public Event withPerson(String person) {
        return toBuilder().addPerson(person).build();
    }

    public Event withComment(String comment) {
        return toBuilder().addComment(comment).build();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        private List<String> people = new ArrayList<>();
        private List<String> comments = new ArrayList<>();

        public abstract Builder id(Optional<Long> id);
        public abstract Builder location(String location);
        public abstract Builder time(OffsetDateTime time);
        public abstract Builder people(ImmutableList<String> people);
        public abstract Builder comments(ImmutableList<String> comments);
        public abstract Event autoBuild();

        abstract ImmutableList<String> people();
        abstract ImmutableList<String> comments();

        public Builder id(long id) {
            return id(Optional.of(id));
        }

        public Builder addPerson(@NotNull String name) {
            checkArgument(!isNullOrEmpty(name), "Name cannot be null or empty");
            this.people.add(name);
            return this;
        }

        public Builder addComment(@NotNull String comment) {
            checkArgument(!isNullOrEmpty(comment), "Comment cannot be null or empty");
            this.comments.add(comment);
            return this;
        }

        public Event build() {
            people.addAll(people());
            comments.addAll(comments());
            people(ImmutableList.copyOf(people));
            comments(ImmutableList.copyOf(comments));
            return autoBuild();
        }
    }
}
