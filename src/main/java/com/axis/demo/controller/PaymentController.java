package com.axis.demo.controller;

import com.axis.demo.service.PaymentService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private DdcController ddcController;
    private static String enrollmentResponse, validationResponse, authorizationResponse, refundResponse, JWT;

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
    
    public static String getJWT() {
        return JWT;
    }


    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;

    }


    @PostMapping("/setup")
    public ModelAndView setUpRequest(Model model, @RequestBody String req) {
        try {

            String httpMethod = "POST";
            String url = "https://apitest.cybersource.com/risk/v1/authentication-setups";
            String requestBody = req;
            String setUpResponse = paymentService.sendPaymentRequest(requestBody, url, httpMethod);
            
            System.out.println("SetUp Method Response : "+setUpResponse);

            System.out.println("Kindly paste this URL in browser and load it to run DDC call : http://localhost:8080/ddc");
            
            JWT = extractJwtFromResponse(setUpResponse);

            if (JWT == null || JWT.isEmpty()) {
                model.addAttribute("error", "Failed to retrieve JWT");
                return new ModelAndView("error");
            }
            model.addAttribute("JWT", JWT);


        } catch (Exception e) {
            e.printStackTrace();
//            return ResponseEntity.status(500).body("Failed to send payment request: " + e.getMessage());
        }
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
            System.out.println("JWT is : " + jwtNode);
            if (jwtNode != null && !jwtNode.isNull()) {

                return jwtNode.asText();
            } else {
                System.out.println("No JWT");
                return "";
            }
        } catch (IOException e) {

            return "";
        }
    }

    @PostMapping("/processCardinalResponse")
    public String processCardinalResponse(@RequestBody Map<String, Object> request) throws JsonProcessingException {
        System.out.println("Received data from Cardinal: " + request.get("data"));
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> values = mapper.readValue(String.valueOf(request.get("data")), new TypeReference<Map<String, Object>>() {
        });

        String requestBody ="{    \"clientReferenceInformation\": {        \"code\": \"Ecom_Feb_04\"    },    \"orderInformation\": {        \"amountDetails\": {            \"currency\": \"INR\",            \"totalAmount\": \"1.17\"        },        \"billTo\": {            \"country\": \"IN\",            \"firstName\": \"Superpe\",            \"lastName\": \"Customer\",            \"phoneNumber\": \"0987654321\",            \"address2\": \"Rajajinagar\",            \"address1\": \"Floor3\",            \"postalCode\": \"560029\",            \"locality\": \"Banglore\",            \"administrativeArea\": \"Karnataka\",            \"email\": \"test@ecom.in\"        }    },    \"paymentInformation\": {        \"card\": {            \"type\": \"002\",            \"expirationMonth\": \"12\",            \"expirationYear\": \"2025\",            \"number\": \"2223000000000007\"        }    },    \"buyerInformation\": {        \"mobilePhone\": \"1245789632\"    },    \"consumerAuthenticationInformation\": {        \"referenceId\": \""+values.get("SessionId")+"\",        \"returnUrl\": \"http://localhost:8080/IFrameResponse1\"    }}";
        // Handle the received data as needed
        System.out.println("Enrolment requestBody"+ requestBody);
        return "null";
    }

    @PostMapping("/enrollment")
    public ResponseEntity<String> enrollmentRequest(@RequestBody String requestBody) {
        try {

            String httpMethod = "POST";
            String url = "https://apitest.cybersource.com/risk/v1/authentications";

            enrollmentResponse = paymentService.sendPaymentRequest(requestBody, url, httpMethod);
            System.out.println("Enrollment Method Executed!");

            System.out.println("Kindly paste this URL in browser and load it to run DDC call : http://localhost:8080/stepup-iframe");
            System.out.println("Enrolment response - "+enrollmentResponse);
            return ResponseEntity.ok("stepup-iframe");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to send payment request: " + e.getMessage());
        }
    }

    @PostMapping("/validation")
    public ResponseEntity<String> validationRequest(@RequestBody String requestBody, HttpServletRequest request, Model model) {
        try {

            String httpMethod = "POST";
            String url = "https://apitest.cybersource.com/risk/v1/authentication-results";

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
            String url = "https://apitest.cybersource.com/pts/v2/payments";

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
            String url = "https://apitest.cybersource.com/pts/v2/payments/" + refundId + "/refunds";

            refundResponse = paymentService.sendPaymentRequest(requestBody, url, httpMethod);
            System.out.println("Refund Method Executed!");

            return ResponseEntity.ok(refundResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to send payment request: " + e.getMessage());
        }
    }

    @GetMapping("/index")
    public ModelAndView home() {
        return new ModelAndView("index");
    }

}
