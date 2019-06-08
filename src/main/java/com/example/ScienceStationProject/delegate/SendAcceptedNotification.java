package com.example.ScienceStationProject.delegate;

import com.example.ScienceStationProject.component.EmailSender;
import com.example.ScienceStationProject.model.User;
import com.example.ScienceStationProject.service.user.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendAcceptedNotification implements JavaDelegate {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailSender emailSender;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("--- Starting process SendAcceptedNotification ---");

        Long userId = Long.valueOf((String)delegateExecution.getVariable("authorId"));
        User author = userService.findById(userId);
        String message = "Good news, your journal has need accepted!";

        emailSender.sendSpecificMail(message,author.getEmail());

        System.out.println("--- Ending process SendAcceptedNotification ---");
    }
}
