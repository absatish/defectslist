package com.defectlist.inwarranty.aop;

import com.defectlist.inwarranty.email.EmailService;
import com.defectlist.inwarranty.exception.InvalidLoginRequestException;
import com.defectlist.inwarranty.exception.ProhibitedUserTriedToLoginException;
import com.defectlist.inwarranty.httprequestheaders.LoginRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

@Aspect
@Component
public class EmailSenderAOP {

    private static final Logger LOGGER = getLogger(EmailSenderAOP.class);

    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    @Autowired
    public EmailSenderAOP(final EmailService emailService, final ObjectMapper objectMapper) {
        this.emailService = emailService;
        this.objectMapper = objectMapper;
    }

    @Pointcut("execution(public String com.defectlist.inwarranty.InwarrantyDefectItemService.login(..))")
    public void login() {

    }

    @Pointcut("execution(public String com.defectlist.inwarranty.InwarrantyDefectItemService.loginAndLoadBillNumbers(..))")
    public void loginAndLoadBillNumbers() {

    }

    @Pointcut("@annotation(EmailServiceEnabled)")
    public void usernameValidation(){

    }

    @AfterReturning(pointcut = "login() || loginAndLoadBillNumbers()", returning = "result")
    public void sendEmail(JoinPoint joinPoint, String result) {
        final LoginRequest loginRequest = objectMapper.convertValue(joinPoint.getArgs()[0], LoginRequest.class);
        if (Boolean.FALSE.equals(result.contains("Something went"))) {
            LOGGER.info("user: {}({}) logged in successfully", loginRequest.getLoggedInUserName(), loginRequest.getUserId());
            emailService.sendEmail("Login Success - " + loginRequest.getLoggedInUserName() + "(" + loginRequest.getUserId() + ")", "<p>id : " + loginRequest.getUserId()
                    + "<br>User logged in successfully<br>Page: " + joinPoint.getSignature().getName() + " </p>");
        }
    }

    @AfterThrowing(pointcut = "usernameValidation()", throwing = "exception")
    public void handleException(JoinPoint joinPoint, Throwable exception) throws JsonProcessingException {
        final String loginRequestString = objectMapper.writeValueAsString(joinPoint.getArgs()[0]);
        final LoginRequest loginRequest = objectMapper.convertValue(joinPoint.getArgs()[0], LoginRequest.class);
        if (exception instanceof ProhibitedUserTriedToLoginException) {
            emailService.sendEmail("Prohibited User tried to login - " + loginRequest.getUserid(),
                    "user tried to login : " + loginRequestString);
        } else if(!(exception instanceof InvalidLoginRequestException)) {
            emailService.sendEmail(exception.getMessage() + " - " + loginRequest.getUserid(),
                    "invalid login attempt: " + loginRequestString);
        }
    }
}
