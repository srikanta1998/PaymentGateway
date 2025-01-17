package com.axis.demo.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;

import com.axis.demo.service.PaymentService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class DdcController {
	
	@Autowired
	private PaymentController paymentController;

	@GetMapping("/ddc")
	public ModelAndView handleRequest(Model model) {
        
        	String responseBody = paymentController.getSetUpResponse();
		
        	String JWT = extractJwtFromResponse(responseBody);
           
        if (JWT == null || JWT.isEmpty()) {
            model.addAttribute("error", "Failed to retrieve JWT");
            return new ModelAndView("error");
        }
                model.addAttribute("JWT", JWT);
        
        return new ModelAndView("NewDDC");  
        
    }
    
    private String extractJwtFromResponse(String response) {
    	

    	if (response == null || response.isEmpty()) {
            throw new IllegalArgumentException("Response body is null or empty");
        }
		try {
            
            ObjectMapper objectMapper = new ObjectMapper();
            
            JsonNode jsonNode = objectMapper.readTree(response);
            
            JsonNode jwtNode = jsonNode.path("consumerAuthenticationInformation").path("accessToken");
            System.out.println("JWT is : "+jwtNode);
            if (jwtNode != null && !jwtNode.isNull()) {
            	
                return jwtNode.asText();
            } else {
                System.out.println("No JWT");
                return "";
            }
        } catch (IOException e) {
            
            e.printStackTrace();
            return "";
        }
    }
    
    @PostMapping("/processCardinalResponse")
    public ResponseEntity<String> processCardinalResponse(@RequestBody Map<String, Object> request) {
        System.out.println("Received data from Cardinal: " + request.get("data"));
        // Handle the received data as needed
        return ResponseEntity.ok("Response successfully processed!");
    }


}
