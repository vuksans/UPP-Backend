package com.example.ScienceStationProject.controller.task.dto.processInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReviewCommentDTO {

    public String hiddenComment;
    public String publicComment;
    public String reviewer;
}
