package com.georgejrdev;

import javax.mail.MessagingException;

import org.junit.Before;
import org.junit.Test;

import com.georgejrdev.lib.email.Email;


public class TestEmail {

    private Email email;

    @Before
    public void setUp() throws MessagingException{
        this.email = new Email(System.getenv("EMAIL_SERVER"),System.getenv("EMAIL"),System.getenv("EMAIL_APP_PASSWORD"));
    }


    @Test
    public void testSendNewEmail(){
        this.email.send("Hi","how are you?","mail@iconscout.com");
    }


    @Test(expected = RuntimeException.class)
    public void testSendNewEmailWithInvalidEmail() {
        this.email.send("Hi", "how are you?", "invalid-email");
    }


    @Test(expected = RuntimeException.class)
    public void testSendNewEmailWithoutAtSing() {
        this.email.send("Hi", "how are you?", "invalidgmail.com");
    }


    @Test(expected = RuntimeException.class)
    public void testSendNewEmailNoDot() {
        this.email.send("Hi", "how are you?", "invalid@gmailcom");
    }
}