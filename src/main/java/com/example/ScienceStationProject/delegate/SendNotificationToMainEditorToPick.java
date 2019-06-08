package com.example.ScienceStationProject.delegate;

import com.example.ScienceStationProject.component.EmailSender;
import com.example.ScienceStationProject.model.User;
import com.example.ScienceStationProject.service.user.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendNotificationToMainEditorToPick implements JavaDelegate {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailSender emailSender;
    /*The system didn't find a editor for that magazine branch combination, so the main editor needs to choose the reviewers*/

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("--- Starting process SendNotificationToMainEditorToPick ---");

        Long mainEditorId = Long.valueOf((String)delegateExecution.getVariable("mainEditorId"));
        User mainEditor = userService.findById(mainEditorId);
        String mainEditorMessage = "There isn't a editor for this journal branch, you need to pick them yourself!";
        emailSender.sendSpecificMail(mainEditorMessage,mainEditor.getEmail());

        System.out.println("--- Ending process SendNotificationToMainEditorToPick ---");
    }
}
