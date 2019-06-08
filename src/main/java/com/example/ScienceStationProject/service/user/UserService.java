package com.example.ScienceStationProject.service.user;

import com.example.ScienceStationProject.controller.user.dto.ChangePasswordDTO;
import com.example.ScienceStationProject.controller.user.dto.SignUpRequestDTO;
import com.example.ScienceStationProject.model.User;
import com.example.ScienceStationProject.util.exception.EmailAlreadyExistsException;
import com.example.ScienceStationProject.util.exception.PasswordNotMatchingException;

import java.util.List;

public interface UserService {

    User add(SignUpRequestDTO signUpRequestDTO) throws EmailAlreadyExistsException;
    boolean confirm(String token);
    User findById(Long id);
    void changePassword(User u, ChangePasswordDTO request) throws PasswordNotMatchingException;
}
