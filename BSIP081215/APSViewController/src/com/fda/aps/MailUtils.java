package com.fda.aps;

import java.io.IOException;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Session;

public class MailUtils {
    public static void main(String[] args) throws IOException {


        final String username = "enter your username";
        final String password = "enter your password";

        Properties props = new Properties();
        //props.put("mail.smtp.auth", "true");
        //props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "fdsupmail.fda.gov");
        props.put("mail.smtp.port", "25");

//        Session session =
//            Session.getInstance(props, new javax.mail.Authenticator() {
//                protected PasswordAuthentication getPasswordAuthentication() {
//                    return new PasswordAuthentication(username, password);
//                }
//            });
        Session session =
            Session.getInstance(props,null);

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("Gareth.Paglinawan@fda.hhs.gov"));
            message.setRecipients(Message.RecipientType.TO,
                                  InternetAddress.parse("Gareth.Paglinawan@fda.hhs.gov"));
            message.setSubject("Sending Email from Test Server");
            message.setText("Request has been approved");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
