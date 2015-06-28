package com.letsdolunch.letsdolunch.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue
    @JsonProperty
    private Long id;
    @NotNull
    @JsonProperty
    private String location;
    @NotNull
    @JsonProperty
    private OffsetDateTime time;
    @OneToMany
    @JsonProperty
    private List<User> people;
    @OneToMany
    @JsonProperty
    private List<Comment> comments;

    Event() {
    }

    public Event(Long id, String location, OffsetDateTime time, List<User> people, List<Comment> comments) {
        this.id = id;
        this.location = location;
        this.time = time;
        this.people = people;
        this.comments = comments;
    }

    public Optional<Long> id() {
        return Optional.ofNullable(id);
    }

    public String location() {
        return location;
    }

    public OffsetDateTime time() {
        return time;
    }

    public List<User> people() {
        return people;
    }

    public List<Comment> comments() {
        return comments;
    }

    public void addPerson(User person) {
        people.add(person);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }
}
