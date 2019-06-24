package com.packtpub.customspringsecurity;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {

    @GetMapping
    public ResponseEntity<String> sayHello() {
        return new ResponseEntity<String>("hello", HttpStatus.OK);
    }
}
