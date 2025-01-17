package com.axis.demo.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;



@Controller
public class TestController {
    @GetMapping("/test")
    public ModelAndView test(Model model) {
    	
    	String JWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJhZTM5MGQxOS02Mjk4LTRjYmQtYTE3Zi00YmEwZmJiOTE0MTciLCJpYXQiOjE3MzMwNzgwMjAsImlzcyI6IjVkZDgzYmYwMGU0MjNkMTQ5OGRjYmFjYSIsImV4cCI6MTczMzA4MTYyMCwiT3JnVW5pdElkIjoiNWM1MWEwMDZhZmE4MGQyMjAwMDYzNjBlIiwiUmVmZXJlbmNlSWQiOiJkYWM4YmYzOS05MDU2LTQzZWMtYTdjOS03MzY0YTJmYWIwNGYifQ.-RgIuEcEUQ3HdGjnsc7p5tpgo0_w6vYqakVwSx_2hik";
        model.addAttribute("JWT", JWT);
        //System.out.println("after model : "+JWT);
        
        return new ModelAndView("test");
    }
    
//    @PostMapping("/processCardinalResponse")
//    public ResponseEntity<String> processCardinalResponse(@RequestBody Map<String, Object> request) {
//        System.out.println("Received data from Cardinal: " + request.get("data"));
//        // Handle the received data as needed
//        return ResponseEntity.ok("Response successfully processed!");
//    }

}
