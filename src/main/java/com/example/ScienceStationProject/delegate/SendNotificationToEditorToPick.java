package com.example.ScienceStationProject.delegate;

import com.example.ScienceStationProject.component.EmailSender;
import com.example.ScienceStationProject.model.User;
import com.example.ScienceStationProject.service.user.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendNotificationToEditorToPick implements JavaDelegate {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailSender emailSender;

    /*Sending a notification to the editor for that branch to pick reviewers*/
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("--- Starting process SendNotificationToEditorToPick ---");

        Long mainEditorId = Long.valueOf((String)delegateExecution.getVariable("editorId"));
        User mainEditor = userService.findById(mainEditorId);
        String mainEditorMessage = "Hey, please choose reviewers for this journal submission!";
        emailSender.sendSpecificMail(mainEditorMessage,mainEditor.getEmail());

        System.out.println("--- Ending process SendNotificationToEditorToPick ---");
    }
}
