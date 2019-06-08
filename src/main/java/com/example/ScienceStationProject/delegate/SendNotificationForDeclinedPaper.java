package com.example.ScienceStationProject.delegate;

import com.example.ScienceStationProject.component.EmailSender;
import com.example.ScienceStationProject.model.User;
import com.example.ScienceStationProject.service.user.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendNotificationForDeclinedPaper implements JavaDelegate {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailSender emailSender;

    /*The paper is declined. I need to inform the author about it.*/
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("--- Starting process SendNotificationForDeclinedPaper ---");

        Long authorId = Long.valueOf((String)delegateExecution.getVariable("authorId"));
        User author = userService.findById(authorId);
        String message = "You journal submission on Science Station has been declined.";

        emailSender.sendSpecificMail(message,author.getEmail());

        System.out.println("--- Ending process SendNotificationForDeclinedPaper ---");
    }
}
