package com.georgejrdev.auxiliar.email;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Email{

    private static String emailServer;
    private static String emailServerHost;
    private static Session session;
    private static String email;
    private static String password;

    private Email() {

    }


    public static void initialize(String emailServer, String email, String password) throws MessagingException {
        setEmailServer(emailServer);
        setEmail(email);
        setPassword(password);
        configureEmailHost();
        configureEmailSending();
    }


    public static void sendEmail(String subject, String body, String to) {
        try {
            checkEmailValidity(to);

            Message message = new MimeMessage(getSession());
            message.setFrom(new InternetAddress(email));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    private static void checkEmailValidity(String email) {
        String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }


    private static void configureEmailHost() throws MessagingException {
        switch (emailServer.toLowerCase()) {
            case "gmail":
                setEmailServerHost("smtp.gmail.com");
                break;
            case "outlook":
                setEmailServerHost("smtp.office365.com");
                break;
            case "yahoo":
                setEmailServerHost("smtp.mail.yahoo.com");
                break;
            case "zoho":
                setEmailServerHost("smtp.zoho.com");
                break;
            default:
                throw new MessagingException("Email Server Not Found");
        }
    }


    private static void configureEmailSending() {
        Properties props = new Properties();
        props.put("mail.smtp.host", getEmailServerHost());
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(getEmail(), getPassword());
            }
        });
    }


    private static void setEmailServer(String emailServer) {
        Email.emailServer = emailServer;
    }


    private static void setEmailServerHost(String emailServerHost) {
        Email.emailServerHost = emailServerHost;
    }


    private static String getEmailServerHost() {
        return emailServerHost;
    }


    private static Session getSession() {
        return session;
    }


    private static void setEmail(String email) {
        Email.email = email;
    }


    private static String getEmail() {
        return email;
    }


    private static void setPassword(String password) {
        Email.password = password;
    }


    private static String getPassword() {
        return password;
    }
}