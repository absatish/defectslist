package com.defectlist.inwarranty.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b>@EmailServiceEnabled</b> annotation is used to send email notifications to the administrator in case if the
 * method on which this annotation is marked throws any kind of exception. <br><br>
 * The annotation used in implementing the advice is @AfterThrowing
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailServiceEnabled {

}
