package com.example.ScienceStationProject.service.coAuthor;

import com.example.ScienceStationProject.model.CoAuthor;
import com.example.ScienceStationProject.repository.CoAuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoAuthorServiceImpl implements CoAuthorService {

    private CoAuthorRepository coAuthorRepository;

    @Autowired
    public CoAuthorServiceImpl(CoAuthorRepository coAuthorRepository) {
        this.coAuthorRepository = coAuthorRepository;
    }

    @Override
    public void add(CoAuthor coAuthor) {
      coAuthorRepository.save(coAuthor);
    }

    @Override
    public List<CoAuthor> addCoAuthors(List<CoAuthor> coAuthorList) {
       return coAuthorRepository.saveAll(coAuthorList);
    }

}
