package com.example.ScienceStationProject.controller.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FormFieldData {
    public String value;
    public String key;
    public String label;
    public boolean required;
    public List<OptionDTO> options = new ArrayList<>();
    public List<PropertyDTO> properties = new ArrayList<>();

}
