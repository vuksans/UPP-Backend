package com.example.ScienceStationProject.controller.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequestDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
