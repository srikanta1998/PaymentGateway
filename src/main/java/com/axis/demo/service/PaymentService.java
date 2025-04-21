 package com.axis.demo.service;

import org.springframework.http.HttpEntity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Service
public class PaymentService {

    private final RestTemplate restTemplate;

    public PaymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public String sendPaymentRequest(String requestBody, String url, String httpMethod) throws Exception {
    	
    	try {
    	
        String keyId = "6075962a-58b4-4a23-a7de-009ba4d42727";
        String secret = "zApzhBUQzJpLJMRFOHvM3P1NLv6yPVUssKfTfilwjb4=";
        String merchantId = "testecom019";
        

        HttpHeaders headers = createHeaders(keyId, secret, merchantId, requestBody, url, httpMethod);
        
        //System.out.println("Headers - "+headers);
        
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        return response.getBody();
         
    	
    	} catch (Exception e){
    		
    		System.out.println("Error - "+e);
    		
    		return e.toString();
    	}
    }
    private HttpHeaders createHeaders(String keyId, String apiSecret, String merchantId, String body, String url, String httpMethod) throws Exception {
        HttpHeaders headers = new HttpHeaders();

        String digest = createDigest(body);

        String currentDate = getCurrentDate();

        String signature = createSignature(keyId, apiSecret, merchantId, url, currentDate, digest, httpMethod);

        headers.add("host", "apitest.cybersource.com");
        headers.add("signature", signature);
        headers.add("digest", digest);
        headers.add("v-c-merchant-id", merchantId);
        headers.add("date", currentDate);

        // Additional Headers
        headers.add("User-Agent", "Java-SpringBootClient");
        headers.setContentType(MediaType.APPLICATION_JSON); // Corrected Content-Type header syntax

        return headers;
    }

    private String getCurrentDate() {
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        
        //System.out.println("date"+dateFormat.format(new Date()));
        
        return dateFormat.format(new Date());
        
    }

    private String createSignature(String keyId, String secret, String merchantId, String url, String date, String digest, String httpMethod) throws Exception {
    	
        String path = url.replace("https://apitest.cybersource.com", "");

        StringBuilder signingString = new StringBuilder();
        signingString.append("host: apitest.cybersource.com\n");
        signingString.append("date: ").append(date).append("\n");
        signingString.append("(request-target): ").append(httpMethod.toLowerCase()).append(" ").append(path).append("\n");
        
        if (digest != null && !digest.isEmpty()) {
            signingString.append("digest: ").append(digest).append("\n");
        }
        
        signingString.append("v-c-merchant-id: ").append(merchantId);

        byte[] secretKeyBytes = Base64.getDecoder().decode(secret);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, "HmacSHA256");
        
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);

        byte[] signatureBytes = mac.doFinal(signingString.toString().getBytes(StandardCharsets.UTF_8));
        String signatureValue = Base64.getEncoder().encodeToString(signatureBytes);
        
        String result = String.format(
                "keyid=\"%s\", algorithm=\"HmacSHA256\", headers=\"host date (request-target) digest v-c-merchant-id\", signature=\"%s\"",
                keyId, signatureValue);

        //System.out.println("Signature is: \n" + result);
        
        return result;
    }

    private String createDigest(String body) throws Exception {

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(body.getBytes(StandardCharsets.UTF_8));

        String encodedHash = Base64.getEncoder().encodeToString(hash);
        
        //System.out.println("Digest is : SHA-256="+ encodedHash);
        
        return "SHA-256=" + encodedHash;
        }

}
