package com.example.ScienceStationProject.service.scienceBranch;

import com.example.ScienceStationProject.model.ScienceBranch;
import com.example.ScienceStationProject.repository.ScienceBranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScienceBranchServiceImpl implements ScienceBranchService {

    @Autowired
    private ScienceBranchRepository scienceBranchRepository;

    @Override
    public ScienceBranch findById(Long id) {
        return scienceBranchRepository.findById(id).get();
}

    @Override
    public List<ScienceBranch> findAll() {
        return scienceBranchRepository.findAll();
    }
}
