package com.georgejrdev.lib.email;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Email{

    private String emailServer;
    private String emailServerHost;
    private Session session;
    private String email;
    private String password;

    public Email(String emailServer, String email, String password) throws MessagingException {
        setEmailServer(emailServer);
        setEmail(email);
        setPassword(password);
        configureEmailHost();
        configureEmailSending();
    }


    public void sendEmail(String subject, String body, String to) {
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


    private void checkEmailValidity(String email) {
        String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }


    private void configureEmailHost() throws MessagingException {
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


    private void configureEmailSending() {
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


    private void setEmailServer(String emailServer) {
        this.emailServer = emailServer;
    }


    private void setEmailServerHost(String emailServerHost) {
        this.emailServerHost = emailServerHost;
    }


    private String getEmailServerHost() {
        return emailServerHost;
    }


    private Session getSession() {
        return session;
    }


    private void setEmail(String email) {
        this.email = email;
    }


    private String getEmail() {
        return email;
    }


    private void setPassword(String password) {
        this.password = password;
    }


    private String getPassword() {
        return password;
    }
}