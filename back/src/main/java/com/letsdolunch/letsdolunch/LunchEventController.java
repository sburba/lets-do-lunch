package com.letsdolunch.letsdolunch;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class LunchEventController {
    @RequestMapping(value = "/events", method = RequestMethod.POST)
    @ResponseBody
    String home(@RequestParam String person, @RequestParam String location, @RequestParam String time) {
        return String.format("Person: %s, Location: %s, Time: %s.", person, location, time);
    }
}
