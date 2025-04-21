package com.axis.demo.controller;

import java.io.IOException;
import java.util.HashMap;
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

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class StepUPIFrame {

	@Autowired
	private PaymentController paymentController;

	@GetMapping("/stepup-iframe")
	public ModelAndView handleRequest(Model model) {
        
        	String responseBody = PaymentController.getEnrollmentResponse();
		
        	Map<String, String> result = extractJwtFromResponse(responseBody);
        	
        	String JWT = result.get("JWT");
    		String requestID = result.get("RequestID");
    		System.out.println("Called method Request ID is : "+requestID);
           
        if (JWT == null || JWT.isEmpty()) {
            model.addAttribute("error", "Failed to retrieve JWT");
            return new ModelAndView("error");
        }
        
        if (requestID == null || requestID.isEmpty()) {
            model.addAttribute("error", "Failed to retrieve Request ID");
            return new ModelAndView("error");
        }
        
                model.addAttribute("JWT", JWT);
                model.addAttribute("requestID", requestID);
        
        return new ModelAndView("StepUpIFrame");  
        
    }
    
    private Map<String, String> extractJwtFromResponse(String response) {

    	if (response == null || response.isEmpty()) {
            throw new IllegalArgumentException("Response body is null or empty");
        }
    	
    	Map<String, String> dummy = new HashMap<>();
    	dummy.put("","");
    	
		try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
            JsonNode extractJWT = jsonNode.path("consumerAuthenticationInformation").path("accessToken");
            JsonNode extractRequestID = jsonNode.path("id");
            System.out.println("JWT is : "+extractJWT+", Request ID is :"+extractRequestID);
            if (extractJWT == null || extractJWT.isNull()) {
            	System.out.println("No JWT");
                return dummy;   
            }
            if (extractRequestID == null || extractRequestID.isNull()) {
            	System.out.println("No Request ID");
                return dummy;   
            }
            String JWT = extractJWT.asText();
            String RequestID = extractRequestID.asText();
            
            Map<String, String> result = new HashMap<>();
            result.put("JWT", JWT);
            result.put("RequestID", RequestID);
            
              return result;
            
        } catch (IOException e) {
            
            e.printStackTrace();
            return dummy;
        }
    }
    
    @PostMapping("/IFrameResponse1")
    public ResponseEntity<String> processCardinalResponse1(@RequestBody String requestBody, HttpServletRequest request) {
    	
    	if (requestBody == null || requestBody.isEmpty()) {
            System.out.println("Received empty or null request body from Cardinal.");
            return ResponseEntity.badRequest().body("Empty or null request body received!");
        }
    	
        System.out.println("Received data from Cardinal after Re-Direction: " + requestBody);
        // Handle the received data as needed
        return ResponseEntity.ok("Response successfully processed!");
    }
    
    
    @PostMapping("/IFrameResponse")
    public ResponseEntity<String> processCardinalResponse(@RequestBody Map<String, Object> request) {
        System.out.println("Received data from Cardinal after Re-Direction: " + request.get("data"));
        // Handle the received data as needed
        return ResponseEntity.ok("Response successfully processed!");
    }


}

