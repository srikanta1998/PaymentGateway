package com.axis.demo.controller;

import com.axis.demo.service.PaymentService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
	
	private static String setUpResponse, enrollmentResponse, validationResponse, authorizationResponse, refundResponse;
    
    public static String getSetUpResponse() {
		return setUpResponse;
	}
    
    public static String getEnrollmentResponse() {
		return enrollmentResponse;
	}

	public String getValidationResponse() {
		return validationResponse;
	}

	public static String getAuthorizationResponse() {
		return authorizationResponse;
	}

	public static String getRefundResponse() {
		return refundResponse;
	}


	private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
        
    }

//    @PostMapping("/")
//    public ResponseEntity<String> sendPaymentRequest(@RequestBody String requestBody, HttpServletRequest request, Model model) {
//        try {
//        	
//        	String httpMethod = request.getMethod();
//        	String rawUrl = request.getRequestURL().toString();
//        	String Processedurl = rawUrl.substring(rawUrl.lastIndexOf('/') + 1);
//        	String url="abc";
//            
//            switch(Processedurl) {
//            
//            case "setup" : url = "https://apitest.cybersource.com/risk/v1/authentication-setups";
//            			   httpMethod = "POST";
//            break;
//            	
//            case "enrollment" : url = "https://apitest.cybersource.com/risk/v1/authentications";
//								httpMethod = "POST";
//			break;
//			
//            case "validation" : url = "https://apitest.cybersource.com/risk/v1/authentication-results";
//								httpMethod = "POST";
//			break;
//			
//            case "authorization" : url = "https://apitest.cybersource.com/pts/v2/payments";
//								   httpMethod = "POST";
//			break;
//			
//            case "refund" : url = "https://apitest.cybersource.com/pts/v2/payments/7345013166986666104951/refunds";
//								   httpMethod = "POST";
//			break;
//			
//			default : throw new IllegalArgumentException("Invalid endpoint URL segment: " + url);
//			
//            }
//      
//            response = paymentService.sendPaymentRequest(requestBody, url, httpMethod);
////            System.out.println("response is : "+response);
//            
//            System.out.println("Kindly paste this URL in browser and load it to run DDC call : http://localhost:8080/ddc");
//            
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body("Failed to send payment request: " + e.getMessage());
//        }
//    }
    
    @PostMapping("/setup")
    public ResponseEntity<String> setUpRequest(@RequestBody String requestBody, HttpServletRequest request, Model model) {
        try {
        	
        	String httpMethod = "POST";
        	String url= "https://apitest.cybersource.com/risk/v1/authentication-setups";
      
        	setUpResponse = paymentService.sendPaymentRequest(requestBody, url, httpMethod);
            System.out.println("SetUp Method Executed!");
            
            System.out.println("Kindly paste this URL in browser and load it to run DDC call : http://localhost:8080/ddc");
            
            return ResponseEntity.ok(setUpResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to send payment request: " + e.getMessage());
        }
    }
    
    @PostMapping("/enrollment")
    public ResponseEntity<String> enrollmentRequest(@RequestBody String requestBody, HttpServletRequest request, Model model) {
        try {
        	
        	String httpMethod = "POST";
        	String url= "https://apitest.cybersource.com/risk/v1/authentications";
      
        	enrollmentResponse = paymentService.sendPaymentRequest(requestBody, url, httpMethod);
            System.out.println("Enrollment Method Executed!");
            
            System.out.println("Kindly paste this URL in browser and load it to run DDC call : http://localhost:8080/stepup-iframe");
            
            return ResponseEntity.ok(enrollmentResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to send payment request: " + e.getMessage());
        }
    }
    
    @PostMapping("/validation")
    public ResponseEntity<String> validationRequest(@RequestBody String requestBody, HttpServletRequest request, Model model) {
        try {
        	
        	String httpMethod = "POST";
        	String url= "https://apitest.cybersource.com/risk/v1/authentication-results";
      
        	validationResponse = paymentService.sendPaymentRequest(requestBody, url, httpMethod);
            System.out.println("Validation Method Executed!");
            
            return ResponseEntity.ok(validationResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to send payment request: " + e.getMessage());
        }
    }
    
    @PostMapping("/authorization")
    public ResponseEntity<String> authorizationRequest(@RequestBody String requestBody, HttpServletRequest request, Model model) {
        try {
        	
        	String httpMethod = "POST";
        	String url= "https://apitest.cybersource.com/pts/v2/payments";
      
        	authorizationResponse = paymentService.sendPaymentRequest(requestBody, url, httpMethod);
            System.out.println("Authorization Method Executed!");
            
            return ResponseEntity.ok(authorizationResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to send payment request: " + e.getMessage());
        }
    }
    
    @PutMapping("/refund/{id}")
    public ResponseEntity<String> refundRequest(@RequestBody String requestBody, HttpServletRequest request, Model model, @PathVariable String id) {
        try {
        	
        	String httpMethod = "POST";
        	String refundId = id;
        	String url= "https://apitest.cybersource.com/pts/v2/payments/"+refundId+"/refunds";
      
        	refundResponse = paymentService.sendPaymentRequest(requestBody, url, httpMethod);
            System.out.println("Refund Method Executed!");
            
            return ResponseEntity.ok(refundResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to send payment request: " + e.getMessage());
        }
    }
    @GetMapping("/index")
    public  ModelAndView home(){
        return new ModelAndView("index");
    }
	
}
