package com.example.ScienceStationProject.service.magazine;

import com.example.ScienceStationProject.model.Magazine;
import com.example.ScienceStationProject.model.User;

import java.util.List;

public interface MagazineService {
    Magazine findById(Long id);
    List<Magazine> getAll();
    void subscribeUser(User user,Long magazineId);
    User checkForBranchEditor(Long branchId,Long magazineId);
}
