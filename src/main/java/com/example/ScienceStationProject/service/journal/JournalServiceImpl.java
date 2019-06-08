package com.example.ScienceStationProject.service.journal;

import com.example.ScienceStationProject.model.Journal;
import com.example.ScienceStationProject.repository.JournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JournalServiceImpl implements JournalService {

    private JournalRepository journalRepository;

    @Autowired
    public JournalServiceImpl(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    @Override
    public void addJournal(Journal j) {
        journalRepository.save(j);
    }
}
