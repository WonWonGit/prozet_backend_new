package com.example.prozet;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// @RequestMapping("v1/api/user")
public class MainController {

    @GetMapping("/")
    public String test(){
        return "test";
    }

    @GetMapping("/v1/user/test")
    public String UserTest(){
        return "test";
    }
    
}
