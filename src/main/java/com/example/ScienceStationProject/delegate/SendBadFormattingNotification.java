package com.example.ScienceStationProject.delegate;

import com.example.ScienceStationProject.component.EmailSender;
import com.example.ScienceStationProject.model.User;
import com.example.ScienceStationProject.service.user.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendBadFormattingNotification implements JavaDelegate{

    @Autowired
    private UserService userService;

    @Autowired
    private EmailSender emailSender;

    /*The main editor has decided that the author needs to format the paper so I just need to send him the notification to do so*/
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("--- Starting process SendBadFormattingNotification ---");

        Long userId = Long.valueOf((String)delegateExecution.getVariable("authorId"));
        User author = userService.findById(userId);
        String message = "Hey your paper seems acceptable. Please format it with these comments:";

        emailSender.sendSpecificMail(message,author.getEmail());

        System.out.println("--- Ending process SendBadFormattingNotification ---");
    }
}
