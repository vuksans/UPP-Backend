package com.example.ScienceStationProject.service.coAuthor;

import com.example.ScienceStationProject.model.CoAuthor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public interface CoAuthorService {

    void add(CoAuthor coAuthor);
    List<CoAuthor> addCoAuthors(List<CoAuthor> coAuthorList);
}
