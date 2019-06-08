package com.example.ScienceStationProject.delegate;

import com.example.ScienceStationProject.model.Magazine;
import com.example.ScienceStationProject.model.User;
import com.example.ScienceStationProject.service.magazine.MagazineService;
import com.example.ScienceStationProject.service.user.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionPayment implements JavaDelegate {

    /*Since I don't have a payment sub-process I'll just put the user in the subscribers*/

    @Autowired
    private UserService userService;

    @Autowired
    private MagazineService magazineService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("--- Starting process SubscriptionPayment ---");

        Long magazineId = (Long)delegateExecution.getVariable("magazineId");
        Long userId = Long.parseLong((String)delegateExecution.getVariable("authorId"));
        User author = userService.findById(userId);
        magazineService.subscribeUser(author,magazineId);

        System.out.println("--- Ending process SubscriptionPayment ---");

    }
}
