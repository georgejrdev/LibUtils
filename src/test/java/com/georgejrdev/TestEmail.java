package com.georgejrdev;

import javax.mail.MessagingException;

import org.junit.Before;
import org.junit.Test;

import com.georgejrdev.auxiliar.email.Email;


public class TestEmail {

    @Before
    public void setUp() throws MessagingException{
        Email.initialize(System.getenv("EMAIL_SERVER"),System.getenv("EMAIL"),System.getenv("EMAIL_APP_PASSWORD"));
    }


    @Test
    public void testSendNewEmail(){
        Email.sendEmail("Hi","how are you?","mail@iconscout.com");
    }


    @Test(expected = RuntimeException.class)
    public void testSendNewEmailWithInvalidEmail() {
        Email.sendEmail("Hi", "how are you?", "invalid-email");
    }


    @Test(expected = RuntimeException.class)
    public void testSendNewEmailWithoutAtSing() {
        Email.sendEmail("Hi", "how are you?", "invalidgmail.com");
    }


    @Test(expected = RuntimeException.class)
    public void testSendNewEmailNoDot() {
        Email.sendEmail("Hi", "how are you?", "invalid@gmailcom");
    }
}