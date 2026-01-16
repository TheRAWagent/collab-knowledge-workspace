package com.dj.ckw.userservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.*;

@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final SesV2Client sesV2Client;
    private final String fromEmail;

    public EmailService(@Value("${SES_FROM_EMAIL}") String fromEmail) {
        this.sesV2Client = SesV2Client.builder().build();
        this.fromEmail = fromEmail;
    }

    public void sendAccountVerificationOtpEmail(String toEmail, String otp) {
        Destination destination = Destination.builder().toAddresses(toEmail).build();

        Content subject = Content.builder()
                .data("Account Verification OTP")
                .build();

        Content bodyContent = Content.builder()
                .data("""
                        <html>
                            <body>
                                <h1>Your OTP for Account Verification</h1>
                                <p>Your OTP is: <strong>%s</strong></p>
                                <p>This OTP is valid for 15 minutes.</p>
                            </body>
                        </html>
                        """.formatted(otp))
                .build();

        Body body = Body.builder()
                .html(bodyContent)
                .build();

        Message msg = Message.builder()
                .subject(subject)
                .body(body)
                .build();

        EmailContent emailContent = EmailContent.builder()
                .simple(msg)
                .build();

        SendEmailRequest emailRequest = SendEmailRequest.builder()
                .destination(destination)
                .content(emailContent)
                .fromEmailAddress(fromEmail)
                .build();

        log.info("Attempting to send an email through Amazon SES "
                + "using the AWS SDK for Java...");
        sesV2Client.sendEmail(emailRequest);
        log.info("email was sent");
    }
}
