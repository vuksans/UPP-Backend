package com.example.ScienceStationProject.delegate;

import com.example.ScienceStationProject.component.EmailSender;
import com.example.ScienceStationProject.model.User;
import com.example.ScienceStationProject.service.user.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendNotificationAfterPaperSubmission implements JavaDelegate {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailSender emailSender;
    /*The journal has been submitted, now I need to send a notification email to the mail editor and the author of the journal*/
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("--- Starting process SendNotificationAfterPaperSubmission ---");

        Long authorId = Long.valueOf((String)delegateExecution.getVariable("authorId"));
        User author = userService.findById(authorId);
        String authorMessage = "Your paper has been successfully submitted and pending review!";
        emailSender.sendSpecificMail(authorMessage,author.getEmail());

        Long mainEditorId = Long.valueOf((String)delegateExecution.getVariable("mainEditorId"));
        User mainEditor = userService.findById(mainEditorId);
        String mainEditorMessage = "You have a new journal to review!";
        emailSender.sendSpecificMail(mainEditorMessage,mainEditor.getEmail());

        System.out.println("--- Ending process SendNotificationAfterPaperSubmission ---");
    }
}
