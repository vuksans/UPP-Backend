package com.example.ScienceStationProject.util;

import com.example.ScienceStationProject.model.Magazine;
import com.example.ScienceStationProject.model.ScienceBranch;
import com.example.ScienceStationProject.model.User;
import com.example.ScienceStationProject.model.UserRole;
import com.example.ScienceStationProject.repository.MagazineRepository;
import com.example.ScienceStationProject.repository.ScienceBranchRepository;
import com.example.ScienceStationProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TestData {

    private PasswordEncoder passwordEncoder;

    private UserRepository userRepository;

    private MagazineRepository magazineRepository;

    private ScienceBranchRepository scienceBranchRepository;

    @Autowired
    public TestData(PasswordEncoder passwordEncoder, UserRepository userRepository, MagazineRepository magazineRepository, ScienceBranchRepository scienceBranchRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.magazineRepository = magazineRepository;
        this.scienceBranchRepository = scienceBranchRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void insertTestData(){

        Optional<User> userOptional = userRepository.findUserByEmail("nikola.vukasinovic@smart.edu.rs");
        if(userOptional.isPresent())
            return;

        ScienceBranch scienceBranch1 = new ScienceBranch();
        scienceBranch1.setName("Economics");

        ScienceBranch scienceBranch2 = new ScienceBranch();
        scienceBranch2.setName("Sport");

        ScienceBranch scienceBranch3 = new ScienceBranch();
        scienceBranch3.setName("Science");

        User u1 = new User();
        u1.setFirstName("GlavniEditorEkonomista");
        u1.setLastName("Ekonomistic");
        u1.setEmail("nikola.vukasinovic@smart.edu.rs");
        u1.setCountry("Serbia");
        u1.setCity("Novi Sad");
        u1.setUserRole(UserRole.ROLE_EDITOR);
        u1.setHashedPassword(passwordEncoder.encode("123"));
        u1.setEmailVerified(true);
        u1.setDeleted(false);

        User u2 = new User();
        u2.setFirstName("Author");
        u2.setLastName("Authorovic");
        u2.setEmail("vuksawow@gmail.com");
        u2.setCountry("Serbia");
        u2.setCity("Novi Sad");
        u2.setUserRole(UserRole.ROLE_AUTHOR);
        u2.setHashedPassword(passwordEncoder.encode("123"));
        u2.setEmailVerified(true);
        u2.setDeleted(false);

        User u3 = new User();
        u3.setFirstName("Editor");
        u3.setLastName("Editorovic");
        u3.setEmail("vuksa12@gmail.com");
        u3.setCountry("Serbia");
        u3.setCity("Novi Sad");
        u3.setUserRole(UserRole.ROLE_EDITOR);
        u3.setHashedPassword(passwordEncoder.encode("123"));
        u3.setEmailVerified(true);
        u3.setDeleted(false);
        u3.setScienceBranch(scienceBranch1);

        User u4 = new User();
        u4.setFirstName("Reviewer1");
        u4.setLastName("Reviewerovic1");
        u4.setEmail("randomemail@gmail.com");
        u4.setCountry("Serbia");
        u4.setCity("Novi Sad");
        u4.setUserRole(UserRole.ROLE_REVIEWER);
        u4.setHashedPassword(passwordEncoder.encode("123"));
        u4.setEmailVerified(true);
        u4.setDeleted(false);
        u4.setScienceBranch(scienceBranch1);

        User u5 = new User();
        u5.setFirstName("Reviewer2");
        u5.setLastName("Reviewerovic2");
        u5.setEmail("randomemail2@gmail.com");
        u5.setCountry("Serbia");
        u5.setCity("Novi Sad");
        u5.setUserRole(UserRole.ROLE_REVIEWER);
        u5.setHashedPassword(passwordEncoder.encode("123"));
        u5.setEmailVerified(true);
        u5.setDeleted(false);
        u5.setScienceBranch(scienceBranch1);

        User u6 = new User();
        u6.setFirstName("Reviewer3");
        u6.setLastName("Reviewerovic3");
        u6.setEmail("randomemail23@gmail.com");
        u6.setCountry("Serbia");
        u6.setCity("Novi Sad");
        u6.setUserRole(UserRole.ROLE_REVIEWER);
        u6.setHashedPassword(passwordEncoder.encode("123"));
        u6.setEmailVerified(true);
        u6.setDeleted(false);
        u6.setScienceBranch(scienceBranch2);

        List<User> m1editors = new ArrayList<>();
        m1editors.add(u3);

        List<User> m1reviewers = new ArrayList<>();
        m1reviewers.add(u4);
        m1reviewers.add(u5);
        m1reviewers.add(u6);


        Magazine m1 = new Magazine();
        m1.setIssn("12313131-23131");
        m1.setMainEditor(u1);
        m1.setEditors(m1editors);
        m1.setReviewers(m1reviewers);
        m1.setName("Forbes");
        m1.setOpenAccess(false);


        scienceBranchRepository.save(scienceBranch1);
        scienceBranchRepository.save(scienceBranch2);
        scienceBranchRepository.save(scienceBranch3);

        userRepository.save(u1);
        userRepository.save(u2);
        userRepository.save(u3);
        userRepository.save(u4);
        userRepository.save(u5);
        userRepository.save(u6);

        magazineRepository.save(m1);
    }
}
