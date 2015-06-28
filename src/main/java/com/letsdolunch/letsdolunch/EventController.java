package com.letsdolunch.letsdolunch;

import com.letsdolunch.letsdolunch.db.CommentDao;
import com.letsdolunch.letsdolunch.db.UserDao;
import com.letsdolunch.letsdolunch.model.Comment;
import com.letsdolunch.letsdolunch.model.Event;
import com.letsdolunch.letsdolunch.db.EventDao;
import com.letsdolunch.letsdolunch.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;

@Controller
@EnableAutoConfiguration
@RequestMapping("/api/v1/events")
public class EventController {
    private final UserDao users;
    private final CommentDao comments;
    private final EventDao events;

    @Autowired
    public EventController(UserDao users, CommentDao comments, EventDao events) {
        this.users = users;
        this.comments = comments;
        this.events = events;
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Event> events() {
        return events.findEventsNewerThan(startOfToday());
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Event createEvent(@RequestBody Event event) {
        event.comments().stream().forEach(comments::save);
        event.people().stream().forEach(users::save);
        return events.save(event);
    }

    @RequestMapping(value = "/{id}/people", method = RequestMethod.PUT)
    public ResponseEntity<Event> addPersonToEvent(@PathVariable long id, @RequestBody User user) {
        Event oldEvent = events.findOne(id);
        oldEvent.addPerson(user);
        Event newEvent = events.save(oldEvent);
        return new ResponseEntity<>(newEvent, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}/comments", method = RequestMethod.PUT)
    public ResponseEntity<Event> addCommentToEvent(@PathVariable long id, @RequestBody Comment comment) {
        Event oldEvent = events.findOne(id);
        oldEvent.addComment(comment);
        Event newEvent = events.save(oldEvent);
        return new ResponseEntity<>(newEvent, HttpStatus.CREATED);
    }

    private static OffsetDateTime startOfToday() {
        return OffsetDateTime.now().with(LocalTime.of(0, 0, 0));
    }
}
