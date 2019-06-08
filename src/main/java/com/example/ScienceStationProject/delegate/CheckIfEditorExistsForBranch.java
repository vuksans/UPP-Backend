package com.example.ScienceStationProject.delegate;

import com.example.ScienceStationProject.model.User;
import com.example.ScienceStationProject.service.magazine.MagazineService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CheckIfEditorExistsForBranch implements JavaDelegate {

    @Autowired
    private MagazineService magazineService;


    /*I need to check if a editor exists for a branch and a magazine*/
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("--- Starting process CheckIfEditorExistsForBranch ---");

        Long magazineId = (Long)delegateExecution.getVariable("magazineId");
        Long mainEditorId = Long.valueOf((String)delegateExecution.getVariable("mainEditorId"));
        Long branchId = Long.valueOf((String)delegateExecution.getVariable("branch"));
        User u = magazineService.checkForBranchEditor(branchId,magazineId);

        if(u != null){
            delegateExecution.setVariable("editorExists",true);
            delegateExecution.setVariable("editorId",u.getId().toString());
        }
        else {
            delegateExecution.setVariable("editorExists", false);
            delegateExecution.setVariable("editorId",mainEditorId.toString());
        }
        System.out.println("--- Ending process CheckIfEditorExistsForBranch ---");
    }
}
