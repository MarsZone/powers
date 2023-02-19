package com.mars.powers.ai;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping(value = "/hello", name = "返回Hello World")
    public String hello(@RequestParam(value = "name", required = false) String name) {
        return String.format("Hello %s! ", name == null ? "World" : name);
    }
}
