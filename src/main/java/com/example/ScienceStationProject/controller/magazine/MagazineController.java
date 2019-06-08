package com.example.ScienceStationProject.controller.magazine;

import com.example.ScienceStationProject.model.Magazine;
import com.example.ScienceStationProject.model.User;
import com.example.ScienceStationProject.service.magazine.MagazineService;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/stationApi/magazines")
public class MagazineController {

    private MagazineService magazineService;

    private RuntimeService runtimeService;

    @Autowired
    public MagazineController(MagazineService magazineService, RuntimeService runtimeService) {
        this.magazineService = magazineService;
        this.runtimeService = runtimeService;
    }

    /*This is a endpoint where a user selects a magazine, that should start the process*/
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMagazine(@PathVariable("id") Long id){
        Magazine m = magazineService.findById(id);
        if(m == null)
            return ResponseEntity.notFound().build();

        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Map<String,Object> processVariables = new HashMap<>();
        processVariables.put("authorId",u.getId().toString());
        processVariables.put("magazineId",m.getId());
        processVariables.put("magazineName",m.getName());
        processVariables.put("mainEditorId",m.getMainEditor().getId().toString());

        runtimeService.startProcessInstanceByKey("publishJournalProcess",processVariables);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<?> allMagazines(){
        return ResponseEntity.ok(magazineService.getAll());
    }
}
