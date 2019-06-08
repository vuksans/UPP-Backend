package com.example.ScienceStationProject.service.scienceBranch;

import com.example.ScienceStationProject.model.ScienceBranch;

import java.util.List;

public interface ScienceBranchService {
    ScienceBranch findById(Long id);
    List<ScienceBranch> findAll();
}
