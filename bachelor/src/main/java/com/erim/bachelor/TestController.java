package com.erim.bachelor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/test")
public class TestController {

    @GetMapping
    public String hello() {
        return "hello world";
    }

}
