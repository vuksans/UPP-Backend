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
public class CheckSubscription implements JavaDelegate {

    @Autowired
    private MagazineService magazineService;

    @Autowired
    private UserService userService;
    /*Magazine is not open access we need to check if the user is subscribed to his selected magazine*/

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("--- Starting process CheckSubscription ---");

        Long magazineId = (Long)delegateExecution.getVariable("magazineId");
        Magazine magazine = magazineService.findById(magazineId);
        Long userId = Long.parseLong((String)delegateExecution.getVariable("authorId"));
        User author = userService.findById(userId);

        boolean flag = false;
        for(User u:magazine.getSubscribers()){
            if(u.getId() == author.getId()){
                flag = true;
                delegateExecution.setVariable("activeSubscription",true);
            }
        }
        if(!flag)
            delegateExecution.setVariable("activeSubscription",false);

        System.out.println("--- Ending process CheckSubscription ---");
    }
}
