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

    // Replace sender@example.com with your "From" address.
    // This address must be verified with Amazon SES.
    private static final String FROM = "jajulasatish08@gmail.com";

    // Replace recipient@example.com with a "To" address. If your account
    // is still in the sandbox, this address must be verified.
    private static final String TO = "jajulasatish08@gmail.com";

    // The configuration set to use for this email. If you do not want to use a
    // configuration set, comment the following variable and the
    // .withConfigurationSetName(CONFIGSET); argument below.
    private static final String CONFIGSET = "ConfigSet";

    // The subject line for the email.
    private static final String SUBJECT = "Amazon SES test (AWS SDK for Java)";

    // The HTML body for the email.
    private static final String HTMLBODY = "<h1>Amazon SES test (AWS SDK for Java)</h1>"
            + "<p>This email was sent with <a href='https://aws.amazon.com/ses/'>"
            + "Amazon SES</a> using the <a href='https://aws.amazon.com/sdk-for-java/'>"
            + "AWS SDK for Java</a>";

    // The email body for recipients with non-HTML email clients.
    private static final String TEXTBODY = "This email was sent through Amazon SES "
            + "using the AWS SDK for Java.";

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
//                                    .withText(new Content()
//                                            .withCharset("UTF-8").withData(TEXTBODY)))
                                .withSubject(new Content()
                                        .withCharset("UTF-8").withData(subject)))
                        .withSource(FROM);
                amazonSimpleEmailService.sendEmail(request);
                //System.out.println("Email sent!");
            } catch (Exception exception) {
                LOGGER.error("The email was not sent. Error message: " + exception.getMessage());
            }
        }
    }
}
