package com.example.ScienceStationProject.delegate;

import com.example.ScienceStationProject.component.EmailSender;
import com.example.ScienceStationProject.model.User;
import com.example.ScienceStationProject.service.user.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendNotificationToRepickReviewer implements JavaDelegate {

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private UserService userService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("--- Starting process SendNotificationToRepickReviewer ---");

        Long editorId = Long.valueOf((String)delegateExecution.getVariable("editorId"));

        User u = userService.findById(editorId);
        String message = "A reviewer hasn't reviewed the paper in time, please select another reviewer!";
        emailSender.sendSpecificMail(message,u.getEmail());

        System.out.println("--- Ending process SendNotificationToRepickReviewer ---");
    }
}
