package com.example.ScienceStationProject.service.journal;

import com.example.ScienceStationProject.model.Journal;
import org.springframework.stereotype.Service;

@Service
public interface JournalService {

    void addJournal(Journal j);
}
