package com.example.ScienceStationProject.controller.user;

import com.example.ScienceStationProject.component.JwtTokenUtil;
import com.example.ScienceStationProject.controller.user.dto.SignInRequestDTO;
import com.example.ScienceStationProject.controller.user.dto.SignInResponseDTO;
import com.example.ScienceStationProject.model.User;
import com.example.ScienceStationProject.security.SecurityParams;
import com.example.ScienceStationProject.util.exception.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    @Qualifier("userDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = SecurityParams.jwtAuthenticationPath, method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody SignInRequestDTO signInRequestDTO){
        final UserDetails userDetails = userDetailsService.loadUserByUsername(signInRequestDTO.getUsername());

        authenticate(signInRequestDTO.getUsername(),signInRequestDTO.getPassword());

        final SignInResponseDTO token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(token);
    }
    //TODO:Refresh namesti
    @RequestMapping(value = SecurityParams.jwtAuthenticationRefresh, method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request){
        String authToken = request.getHeader(SecurityParams.jwtHeader);
        final String token = authToken.substring(7);
        String username = jwtTokenUtil.getUserNameFromToken(token);
        User user = (User)userDetailsService.loadUserByUsername(username);

        if(jwtTokenUtil.canTokenBeRefreshed(token)){
            SignInResponseDTO refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(refreshedToken);
        }
        return ResponseEntity.badRequest().build();
    }

    public void authenticate(String username,String password){
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        } catch (DisabledException e) {
            throw new AuthenticationException("User is disabled!", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Bad credentials!", e);
        }
    }
}
