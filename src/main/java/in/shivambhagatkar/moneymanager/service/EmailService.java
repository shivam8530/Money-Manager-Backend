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

    @Value("${BREVO_API_KEY}")
    private String brevoApiKey;

    private static final String BREVO_URL = "https://api.brevo.com/v3/smtp/email";

    public void sendEmail(String to, String subject, String body) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // sender info (must be your verified Brevo sender email)
            Map<String, String> sender = new HashMap<>();
            sender.put("name", "Money Manager App");
            sender.put("email", "shivambhagatkar34@gmail.com");

            // recipient info
            Map<String, String> recipient = new HashMap<>();
            recipient.put("email", to);
            recipient.put("name", to);

            // build body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("sender", sender);
            requestBody.put("to", List.of(recipient));
            requestBody.put("subject", subject);
            requestBody.put("htmlContent", "<html><body>" + body + "</body></html>");

            // headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", brevoApiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response =
                    restTemplate.postForEntity(BREVO_URL, entity, String.class);

            if (response.getStatusCode() == HttpStatus.ACCEPTED) {
                System.out.println("✅ Email sent successfully to " + to);
            } else {
                System.out.println("⚠️ Email failed with response: " + response.getBody());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Error sending email via Brevo API: " + e.getMessage());
        }
    }
}
