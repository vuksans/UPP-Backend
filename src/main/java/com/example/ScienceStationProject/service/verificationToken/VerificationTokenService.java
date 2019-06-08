package com.example.ScienceStationProject.service.verificationToken;

import com.example.ScienceStationProject.model.User;

import java.util.Optional;

public interface VerificationTokenService {

    public void generateToken(User u);
    public Optional<User> confirmUser(String token);
}
