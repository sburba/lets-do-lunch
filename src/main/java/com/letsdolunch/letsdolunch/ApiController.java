package com.letsdolunch.letsdolunch;

import com.google.common.base.Strings;
import com.letsdolunch.letsdolunch.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@EnableAutoConfiguration
@RequestMapping("/api/v1")
public class ApiController {
    private final EventService events;

    @Autowired
    public ApiController(EventService events) {
        this.events = events;
    }

    @ResponseBody
    @RequestMapping(value = "/events", method = RequestMethod.GET)
    public List<Event> events() {
        return getTodaysEvents();
    }

    @ResponseBody
    @RequestMapping(value = "/events", method = RequestMethod.POST)
    public Event createEvent(@RequestBody Event event) {
        return events.create(event);
    }

    @RequestMapping(value = "/events/{id}/people", method = RequestMethod.PUT)
    public ResponseEntity<Event> addPersonToEvent(@PathVariable int id, @RequestParam String name) {
        if (Strings.isNullOrEmpty(name)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Event event = events.update(events.get(id).withPerson(name));
        return new ResponseEntity<>(event, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/events/{id}/comments", method = RequestMethod.PUT)
    public ResponseEntity<Event> addCommentToEvent(@PathVariable int id, @RequestParam String comment) {
        Event event = events.update(events.get(id).withComment(comment));
        return new ResponseEntity<>(event, HttpStatus.CREATED);
    }


    public List<Event> getTodaysEvents() {
        return events.getAll().stream()
                .filter(event -> event.time().isAfter(startOfToday()))
                .collect(Collectors.toList());
    }

    private static OffsetDateTime startOfToday() {
        return OffsetDateTime.now().with(LocalTime.of(0, 0, 0));
    }
}
