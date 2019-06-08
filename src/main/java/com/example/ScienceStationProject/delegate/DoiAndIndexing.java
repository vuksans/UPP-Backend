package com.example.ScienceStationProject.delegate;

import com.example.ScienceStationProject.model.*;
import com.example.ScienceStationProject.service.coAuthor.CoAuthorService;
import com.example.ScienceStationProject.service.journal.JournalService;
import com.example.ScienceStationProject.service.magazine.MagazineService;
import com.example.ScienceStationProject.service.scienceBranch.ScienceBranchService;
import com.example.ScienceStationProject.service.user.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DoiAndIndexing implements JavaDelegate {

    @Autowired
    private UserService userService;

    @Autowired
    private ScienceBranchService scienceBranchService;

    @Autowired
    private CoAuthorService coAuthorService;

    @Autowired
    private MagazineService magazineService;

    @Autowired
    private JournalService journalService;
    //TODO: THIS AND DOWNLOAD BUTTON + HERE SET THE REAL PDF NAME
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Journal journal = new Journal();
        User author = userService.findById(Long.valueOf((String)delegateExecution.getVariable("authorId")));
        Long branchId = Long.valueOf((String)delegateExecution.getVariable("branch"));
        ScienceBranch scienceBranch = scienceBranchService.findById(branchId);
        Magazine magazine = magazineService.findById((Long)delegateExecution.getVariable("magazineId"));

        List<Map<String,Object>> coAuthors = (List<Map<String,Object>>)delegateExecution.getVariable("coAuthors");
        List<CoAuthor> coAuthorList = new ArrayList<>();
        for(Map<String,Object> coAuthor: coAuthors){
            CoAuthor temp = new CoAuthor();
            temp.setCity((String)coAuthor.get("city"));
            temp.setCountry((String)coAuthor.get("country"));
            temp.setEmail((String)coAuthor.get("email"));
            coAuthorList.add(temp);
        }

        journal.setCoAuthors(coAuthorService.addCoAuthors(coAuthorList));
        journal.setAuthor(author);
        journal.setBranch(scienceBranch);
        journal.setCoAuthors(coAuthorList);
        journal.setHeadline((String)delegateExecution.getVariable("headline"));
        journal.setMagazine(magazine);
        journal.setJournalAbstract((String)delegateExecution.getVariable("journalAbstract"));
        journal.setKeyPoints((String)delegateExecution.getVariable("keyPoints"));

        journalService.addJournal(journal);

        String uploadsPath = System.getProperty("user.dir") + "\\src\\main\\resources\\uploads\\"+delegateExecution.getProcessInstanceId()+".pdf";
        String journalsPath = System.getProperty("user.dir") + "\\src\\main\\resources\\journals";

        Path path = Paths.get(uploadsPath);
        File newPdf = new File(journalsPath+"\\"+journal.getId()+".pdf");
        newPdf.createNewFile();
        FileOutputStream fout = new FileOutputStream(newPdf);
        fout.write(Files.readAllBytes(path));
    }
}
