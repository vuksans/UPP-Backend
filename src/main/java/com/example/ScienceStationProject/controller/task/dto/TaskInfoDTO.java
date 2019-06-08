package com.example.ScienceStationProject.controller.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskInfoDTO {

    public String taskId;
    public String taskName;
    public String magazineId;
    public String magazineName;
}
