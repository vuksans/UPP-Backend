package com.example.ScienceStationProject.component;

import com.example.ScienceStationProject.util.ApplicationParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void confirmRegistration(String token,String recipientMail){
        String subject = "Registration Conformation";
        String conformationUrl = ApplicationParams.clientUrl+"/confirm/"+token;
        String message = "Welcome to ScienceStation! Please confirm your email by clicking the link below.\n"+conformationUrl;

        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(recipientMail);
        emailMessage.setSubject(subject);
        emailMessage.setText(message);
        javaMailSender.send(emailMessage);
    }

    public void sendSpecificMail(String message, String recipientMail){
        String subject = "Notification from Science Station";

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(recipientMail);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);
    }

}
