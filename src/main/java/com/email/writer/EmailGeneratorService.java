package com.email.writer;

import org.springframework.stereotype.Service;

@Service
public class EmailGeneratorService {


    public String generateEmailReply(EmailRequest emailRequest) {
        //Build Prompt
        String prompt = buildPrompt(emailRequest);
        //Prepare raw json Body
        String requestBody= String.format("""
                   {
                                "contents": [
                        {
                            "parts": [
                            {
                                "text": "%s"
                            }
                        ]
                        }
                    ]
                        }
                """,prompt);
        //send request

        //Extract Response
        return "";
    }

    private String buildPrompt(EmailRequest emailRequest) {
        StringBuilder prompt =new StringBuilder();
        prompt.append("Generate a professional email reply for the following email:");
        if (emailRequest.getTone() != null && emailRequest.getEmailContent().isEmpty()) {
            prompt.append("Use a ").append(emailRequest.getTone()).append(" tone");
            //Use a casual tone
        }
        prompt.append("Original Email: \n").append(emailRequest.getEmailContent());
        return prompt.toString();
    }

}
