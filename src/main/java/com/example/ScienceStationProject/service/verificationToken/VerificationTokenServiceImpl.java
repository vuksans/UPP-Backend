package com.example.ScienceStationProject.service.verificationToken;

import com.example.ScienceStationProject.component.EmailSender;
import com.example.ScienceStationProject.model.User;
import com.example.ScienceStationProject.model.VerificationToken;
import com.example.ScienceStationProject.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private VerificationTokenRepository verificationTokenRepository;

    private EmailSender emailSender;

    @Autowired
    public VerificationTokenServiceImpl(VerificationTokenRepository verificationTokenRepository, EmailSender emailSender) {
        this.verificationTokenRepository = verificationTokenRepository;
        this.emailSender = emailSender;
    }

    @Override
    public void generateToken(User u) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setUser(u);
        verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1));
        verificationTokenRepository.save(verificationToken);

        emailSender.confirmRegistration(verificationToken.getToken(),u.getEmail());
    }

    @Override
    public Optional<User> confirmUser(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findOneByToken(token);
        Optional<User> userOptional;
        if(verificationToken.isPresent()){
            if(verificationToken.get().getExpiryDate().isAfter(LocalDateTime.now())){
                userOptional = Optional.of(verificationToken.get().getUser());
            }
            else
                userOptional = Optional.of(null);
        }
        else
            userOptional = Optional.of(null);
        return userOptional;
    }
}
