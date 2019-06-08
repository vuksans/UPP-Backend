package com.example.ScienceStationProject.delegate;

import com.example.ScienceStationProject.service.magazine.MagazineService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CheckMagazineType implements JavaDelegate{

    /*User has selected a magazine, check if it's open access or not*/
    @Autowired
    private MagazineService magazineService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("--- Starting process CheckMagazineType ---");

        Long magazineId = (Long)delegateExecution.getVariable("magazineId");
        if(magazineService.findById(magazineId).isOpenAccess())
            delegateExecution.setVariable("openAccess",true);
        else
            delegateExecution.setVariable("openAccess",false);

        System.out.println("--- Ending process CheckMagazineType ---");
    }


}
