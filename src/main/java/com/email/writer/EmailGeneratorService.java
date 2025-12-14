package com.email.writer;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Service
public class EmailGeneratorService {
    @Value("${gemini.api.url}")
    private String geminiApiUrl;
    @Value("${gemini.api.key}")
    private String geminiApiKey;


    private final WebClient webClient;

    public EmailGeneratorService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String generateEmailReply(EmailRequest emailRequest) {
        //Build Prompt
        String prompt = buildPrompt(emailRequest);
        //Prepare raw json Body
        Map<String,Object> requestBody=Map.of(
                "contents",new Object[] {
                        Map.of("parts",new Object[]{
                                Map.of("text",prompt)
                        })
                }
        );

        //send request and get response
        String response = webClient.post()
                .uri(geminiApiUrl + "?key=" + geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();


        //Extract Response and return

        return extractResponseContent(response);
    }

    private String extractResponseContent(String response) {
        try{
            ObjectMapper mapper =new ObjectMapper();
            JsonNode rootNode=mapper.readTree(response);
            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asString();
        }catch (Exception e){
            return "Error processing request: "+ e.getMessage();
        }

    }

    private String buildPrompt(EmailRequest emailRequest) {
        StringBuilder prompt =new StringBuilder();
        prompt.append("Generate a professional email reply for the following email:");
        if (emailRequest.getTone() != null && !emailRequest.getEmailContent().isEmpty()) {
            prompt.append("Use a ").append(emailRequest.getTone()).append(" tone");
            //Use a casual tone
        }
        prompt.append("Original Email: \n").append(emailRequest.getEmailContent());
        return prompt.toString();
    }

}
