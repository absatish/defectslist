package com.defectlist.inwarranty.email;

public interface EmailService {

    void sendEmail(final String subject, final String message);

}
