package com.example.ScienceStationProject.controller.user;

import com.example.ScienceStationProject.controller.user.dto.ChangePasswordDTO;
import com.example.ScienceStationProject.controller.user.dto.SignUpRequestDTO;
import com.example.ScienceStationProject.model.User;
import com.example.ScienceStationProject.service.user.UserService;
import com.example.ScienceStationProject.util.exception.EmailAlreadyExistsException;
import com.example.ScienceStationProject.util.exception.PasswordNotMatchingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("stationApi/users")
public class UserController {

    private UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @RequestMapping(value="/create", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody SignUpRequestDTO signUpRequestDTO){
        try{
            return ResponseEntity.ok(userService.add(signUpRequestDTO));
        }catch (EmailAlreadyExistsException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @RequestMapping(value="/confirm/{token}", method = RequestMethod.GET)
    public ResponseEntity<?> confirmAccount(@PathVariable("token") String token){
        if(userService.confirm(token))
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }
    @RequestMapping(value="/user", method = RequestMethod.GET)
    public ResponseEntity<?> getUserFromToken(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(user);
    }
    @RequestMapping(value="/changePassword", method = RequestMethod.POST)
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO request){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try{
            userService.changePassword(user, request);
            return ResponseEntity.ok().build();
        } catch (PasswordNotMatchingException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
