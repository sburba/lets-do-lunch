package com.letsdolunch.letsdolunch;

import com.letsdolunch.letsdolunch.model.Event;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@EnableAutoConfiguration
@RequestMapping("/api/v1")
public class ApiController {
    private List<Event> events = new ArrayList<>();

    @RequestMapping(value = "/events", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Event> events() {
        removeYesterdaysEvents();
        return events;
    }

    @RequestMapping(value = "/events", method = RequestMethod.POST)
    public ResponseEntity<Event> createEvent(@RequestParam String location, @RequestParam String time,
                                             @RequestParam String name, @RequestParam String comment) {
        try {
            Event newEvent = Event.builder()
                    .id(events.size())
                    .location(location)
                    .time(time)
                    .addPerson(name)
                    .addComment(comment)
                    .build();
            events.add(newEvent);
            return new ResponseEntity<>(newEvent, HttpStatus.CREATED);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/events/{id}/people", method = RequestMethod.PUT)
    public ResponseEntity<Event> addPersonToEvent(@PathVariable int id, @RequestParam String name) {
        if (StringUtils.isEmpty(name)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Event event = events.get(id);
        event.people.add(name);
        return new ResponseEntity<>(event, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/events/{id}/comments", method = RequestMethod.PUT)
    public ResponseEntity<Event> addCommentToEvent(@PathVariable int id, @RequestParam String comment) {
        Event event = events.get(id);
        event.comments.add(comment);
        return new ResponseEntity<>(event, HttpStatus.CREATED);
    }

    private void removeYesterdaysEvents() {
        events = withoutEventsFromYesterday(events);
    }

    private static List<Event> withoutEventsFromYesterday(List<Event> events) {
        return events.stream()
                .filter(event -> event.time.isAfter(startOfToday()))
                .collect(Collectors.toList());
    }

    private static LocalDateTime startOfToday() {
        return LocalDateTime.now().with(LocalTime.of(0, 0, 0));
    }
}
