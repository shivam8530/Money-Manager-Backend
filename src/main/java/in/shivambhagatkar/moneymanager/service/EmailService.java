package in.shivambhagatkar.moneymanager.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    @Value("${SENDGRID_API_KEY}")
    private String sendGridApiKey;

    private static final String SENDGRID_URL = "https://api.sendgrid.com/v3/mail/send";

    public void sendEmail(String to, String subject, String body) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // sender info
            Map<String, String> from = new HashMap<>();
            from.put("email", "shivambhagatkar34@gmail.com");
            from.put("name", "Money Manager App");

            // recipient info
            Map<String, String> toMap = new HashMap<>();
            toMap.put("email", to);

            // content
            Map<String, String> contentMap = new HashMap<>();
            contentMap.put("type", "text/html");
            contentMap.put("value", body);

            Map<String, Object> message = new HashMap<>();
            message.put("personalizations", List.of(Map.of("to", List.of(toMap))));
            message.put("from", from);
            message.put("subject", subject);
            message.put("content", List.of(contentMap));

            // headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(sendGridApiKey);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(message, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(SENDGRID_URL, request, String.class);

            if (response.getStatusCode() == HttpStatus.ACCEPTED) {
                System.out.println("✅ Email sent successfully to " + to);
            } else {
                System.out.println("⚠️ Email failed: " + response.getBody());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Error sending email via SendGrid: " + e.getMessage());
        }
    }
}
