package com.letsdolunch.letsdolunch;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@EnableAutoConfiguration
public class LunchEventController {
    private ArrayList<Event> events = new ArrayList<>();

    @RequestMapping(value = "/events", method = RequestMethod.GET)
    public @ResponseBody List<Event> events() {
        return events;
    }

    @RequestMapping(value = "/events", method = RequestMethod.POST)
    ResponseEntity<?> createEvent(@RequestParam String name, @RequestParam String location, @RequestParam String time) {
        Event newEvent = new Event(events.size(), name, location, time);
        events.add(newEvent);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/events/{id}", method = RequestMethod.POST)
    ResponseEntity<?> addPerson(@PathVariable int id, @RequestParam String name) {
        Event event = events.get(id);
        event.people.add(name);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/comments", method = RequestMethod.POST)
    ResponseEntity<?> addComment(@RequestParam int id, @RequestParam String comment) {
        Event event = events.get(id);
        event.comments.add(comment);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
