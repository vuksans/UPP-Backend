package com.example.ScienceStationProject.delegate;

import com.example.ScienceStationProject.model.Magazine;
import com.example.ScienceStationProject.model.User;
import com.example.ScienceStationProject.service.magazine.MagazineService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CheckIfReviewersExistForBranch implements JavaDelegate {

    @Autowired
    private MagazineService magazineService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("--- Starting process CheckIfReviewersExistForBranch ---");

        Long magazineId = (Long)delegateExecution.getVariable("magazineId");
        Long branchId = Long.valueOf((String)delegateExecution.getVariable("branch"));
        Long mainEditorId = Long.valueOf((String)delegateExecution.getVariable("mainEditorId"));
        Magazine m = magazineService.findById(magazineId);
        List<String> reviewers = new ArrayList<>();
        for(User u: m.getReviewers()){
            if(u.getScienceBranch().getId() == branchId){
                reviewers.add(u.getId().toString());
            }
        }
        if(reviewers.isEmpty()){
            reviewers.add(mainEditorId.toString());
            delegateExecution.setVariable("reviewersExist", false);
            delegateExecution.setVariable("reviewers", reviewers);
        }
        else {
            delegateExecution.setVariable("reviewersExist", true);
            delegateExecution.setVariable("availableReviewers", reviewers);
        }
        System.out.println("--- Ending process CheckIfReviewersExistForBranch ---");
    }
}
