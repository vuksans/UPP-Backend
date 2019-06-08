package com.example.ScienceStationProject.service.user;

import com.example.ScienceStationProject.controller.user.dto.ChangePasswordDTO;
import com.example.ScienceStationProject.controller.user.dto.SignUpRequestDTO;
import com.example.ScienceStationProject.model.User;
import com.example.ScienceStationProject.model.UserRole;
import com.example.ScienceStationProject.repository.UserRepository;
import com.example.ScienceStationProject.service.verificationToken.VerificationTokenService;
import com.example.ScienceStationProject.util.exception.EmailAlreadyExistsException;
import com.example.ScienceStationProject.util.exception.PasswordNotMatchingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private PasswordEncoder passwordEncoder;

    private UserRepository userRepository;

    private VerificationTokenService verificationTokenService;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, VerificationTokenService verificationTokenService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.verificationTokenService = verificationTokenService;
    }

    @Override
    public User add(SignUpRequestDTO signUpRequestDTO) throws EmailAlreadyExistsException {

        Optional<User> userOptional = userRepository.findUserByEmail(signUpRequestDTO.getEmail());
        if(userOptional.isPresent())
            throw new EmailAlreadyExistsException();

        User u = new User();
        u.setFirstName(signUpRequestDTO.getFirstName());
        u.setLastName(signUpRequestDTO.getLastName());
        u.setCity(signUpRequestDTO.getCity());
        u.setCountry(signUpRequestDTO.getCountry());
        u.setEmail(signUpRequestDTO.getEmail());
        String password = signUpRequestDTO.getPassword();
        u.setHashedPassword(passwordEncoder.encode(password));
        u.setUserRole(UserRole.ROLE_AUTHOR);
        userRepository.save(u);

        verificationTokenService.generateToken(u);

        return u;
    }

    @Override
    public boolean confirm(String token) {
        Optional<User> user = verificationTokenService.confirmUser(token);
        if(user.isPresent()){
            user.get().setEmailVerified(true);
            userRepository.save(user.get());
            return true;
        }
        return false;
    }

    @Override
    public User findById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.get();
    }

    @Override
    public void changePassword(User u, ChangePasswordDTO request) throws PasswordNotMatchingException{
        User dbUser = userRepository.findUserById(u.getId()).get();
        if(!passwordEncoder.matches(request.getOldPassword(),dbUser.getHashedPassword()))
            throw new PasswordNotMatchingException();
        dbUser.setHashedPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(dbUser);
    }

}
