package com.letsdolunch.letsdolunch;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@EnableAutoConfiguration
public class LunchEventController {
    private ArrayList<Event> events = new ArrayList<>();
    
    @RequestMapping(value = "/events", method = RequestMethod.GET)
    List<Event> events() {
        return events;
    }

    @RequestMapping(value = "/events/{id}", method = RequestMethod.POST)
    void addPerson(@PathVariable int id, @RequestParam String name) {
        Event event = events.get(id);
        event.people.add(name);
    }

    @RequestMapping(value = "/events", method = RequestMethod.POST)
    void createEvent(@RequestParam String location, @RequestParam String name, @RequestParam String time) {
        Event newEvent = new Event(events.size(), name, location, time);
        events.add(newEvent);
    }
}
