package com.axis.demo.controller;

import com.axis.demo.model.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;

@CrossOrigin(origins="http://localhost:3000/")
@RestController
@RequestMapping("/login")
public class LoginController {


    @PostMapping("/validateUser")
    public ResponseEntity<String> validateUser(@RequestBody User user) throws Exception {
        System.out.println("Hii -> "+user);
        if ("user@axisbank.com".equals(user.getEmail()) && "Axis@123".equals(user.getPassword())) {
            return new ResponseEntity<>("Login successful", HttpStatus.ACCEPTED);
        }
        throw new Exception("Login Failed");
    }
}
