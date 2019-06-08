package com.example.ScienceStationProject.delegate;

import com.example.ScienceStationProject.component.EmailSender;
import com.example.ScienceStationProject.model.User;
import com.example.ScienceStationProject.service.user.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendNotificationReviewDone implements JavaDelegate {

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private UserService userService;

    /*The reviewers have finished their work so I need to send a notification to the designated editor to make a final decision*/
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("--- Starting process SendNotificationReviewDone ---");

        Long editorId = Long.valueOf((String)delegateExecution.getVariable("editorId"));

        User u = userService.findById(editorId);
        String message = "The reviewers have finished their work, it's up to you now!";
        emailSender.sendSpecificMail(message,u.getEmail());

        System.out.println("--- Ending process SendNotificationReviewDone ---");
    }
}
