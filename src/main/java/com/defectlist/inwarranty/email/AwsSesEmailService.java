package com.defectlist.inwarranty.email;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class AwsSesEmailService implements EmailService {

    private static final Logger LOGGER = getLogger(AwsSesEmailService.class);

    private static final String FROM = "jajulasatish08@gmail.com";

    private static final String TO = "satish.jajula.rgukt@gmail.com";

    private final AmazonSimpleEmailService amazonSimpleEmailService;
    private final boolean sesEnabled;

    @Autowired
    public AwsSesEmailService(final AmazonSimpleEmailService amazonSimpleEmailService,
                              @Value("${aws.ses.enabled}") final boolean sesEnabled) {
        this.amazonSimpleEmailService = amazonSimpleEmailService;
        this.sesEnabled = sesEnabled;
    }

    @Override
    public void sendEmail(final String subject, final String message) {
        if (sesEnabled) {
            try {
                final SendEmailRequest request = new SendEmailRequest()
                        .withDestination(
                                new Destination().withToAddresses(TO))
                        .withMessage(new Message()
                                .withBody(new Body()
                                        .withHtml(new Content()
                                                .withCharset("UTF-8").withData(message)))
                                .withSubject(new Content()
                                        .withCharset("UTF-8").withData(subject)))
                        .withSource(FROM);
                amazonSimpleEmailService.sendEmail(request);
                LOGGER.info("Email sent!");
            } catch (final Exception exception) {
                LOGGER.error("The email was not sent. Error message: " + exception.getMessage());
            }
        }
    }
}
