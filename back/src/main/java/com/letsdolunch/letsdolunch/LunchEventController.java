package com.letsdolunch.letsdolunch;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class LunchEventController {
    @RequestMapping("/lunchevent/{id}")
    @ResponseBody
    long home(@PathVariable long id) {
        return id;
    }
}
