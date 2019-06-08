package com.example.ScienceStationProject.service.magazine;

import com.example.ScienceStationProject.model.Magazine;
import com.example.ScienceStationProject.model.ScienceBranch;
import com.example.ScienceStationProject.model.User;
import com.example.ScienceStationProject.repository.MagazineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MagazineServiceImpl implements MagazineService {

    private MagazineRepository magazineRepository;

    @Autowired
    public MagazineServiceImpl(MagazineRepository magazineRepository) {
        this.magazineRepository = magazineRepository;
    }


    @Override
    public Magazine findById(Long id) {
        Optional<Magazine> magazineOptional = magazineRepository.findById(id);
        return magazineOptional.get();
    }

    @Override
    public List<Magazine> getAll() {
        return magazineRepository.findAll();
    }

    @Override
    public void subscribeUser(User user, Long magazineId) {
        Magazine m = findById(magazineId);
        m.getSubscribers().add(user);
        magazineRepository.save(m);
    }

    @Override
    public User checkForBranchEditor(Long branchId,Long magazineId) {
        Magazine m = findById(magazineId);
        for(User u: m.getEditors()){
            if(u.getScienceBranch().getId() == branchId)
                return u;
        }
        return null;
    }


}
