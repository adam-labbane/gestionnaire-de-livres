package com.example.cdi;

import com.example.cdi.Model.Book;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.SimpleEmail;

import java.io.InputStream;
import java.util.Properties;

public class EmailNotificationService {

    private Properties config;

    public EmailNotificationService() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            config = new Properties();
            config.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEmailNotification(String action, Book book) {
        try {
            String emailBody = "Cher utilisateur,\n\n" +
                    "Une action de " + action + " a été effectuée sur le livre suivant :\n\n" +
                    "Titre : " + book.getTitle() + "\n" +
                    "Auteur : " + book.getAuthor() + "\n" +
                    "ISBN : " + book.getIsbn() + "\n" +
                    "Année : " + book.getYear() + "\n" +
                    "Pages : " + book.getPages() + "\n" +
                    "Description : " + book.getDescription() + "\n\n" +
                    "Cordialement,\n" +
                    "Votre équipe";

            SimpleEmail email = new SimpleEmail();
            email.setHostName(config.getProperty("hostName"));
            email.setSmtpPort(Integer.parseInt(config.getProperty("smtpPort")));
            email.setAuthenticator(new DefaultAuthenticator(config.getProperty("username"), config.getProperty("password")));
            email.setStartTLSEnabled(true);
            email.setFrom(config.getProperty("fromEmail"));
            email.setSubject(config.getProperty("subject"));
            email.setMsg(emailBody);
            email.addTo(config.getProperty("toEmail"));

            email.send();
            System.out.println("Email sent");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
