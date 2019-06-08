package com.example.ScienceStationProject.controller.task.dto.processInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProcessInfoDTO {

    public String journalAbstract;
    public String keyPoints;
    public String headline;
    public String authorName;
    public String branchName;
    public List<ReviewCommentDTO> reviewCommentDTOList = new ArrayList<>();
}

