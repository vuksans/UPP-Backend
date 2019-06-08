package com.example.ScienceStationProject.component;

import com.example.ScienceStationProject.security.SecurityParams;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final String tokenHeader = SecurityParams.jwtHeader;


    public JwtAuthorizationTokenFilter(UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        logger.debug("processing authentication for '{}'", httpServletRequest.getRequestURL());

        String errorMessage = "";
        boolean flag = false;

        final String requestHeader = httpServletRequest.getHeader(this.tokenHeader);
        String username = null;
        String authToken = null;
        if(requestHeader!= null  && requestHeader.startsWith("Bearer ")){
            authToken = requestHeader.substring(7);
            try{
                username = jwtTokenUtil.getUserNameFromToken(authToken);
            }catch (IllegalArgumentException e) {
                flag = true;
                errorMessage = "an error occurred during getting username from token";
                logger.error(errorMessage, e);
            } catch (ExpiredJwtException e) {
                flag = true;
                errorMessage = "the token is expired and not valid anymore";
                logger.warn(errorMessage, e);
            }
            catch (SignatureException e){
                flag = true;
                errorMessage = "The signature doesn't match, token isn't trusted!";
                logger.warn(errorMessage);
            }
        }
        else {
            logger.warn("couldn't find bearer string, will ignore the header");
        }

        logger.debug("checking authentication for user '{}'", username);

        if(username !=null && SecurityContextHolder.getContext().getAuthentication() == null){
            logger.debug("security context was null, so authorizing user");

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if(jwtTokenUtil.validateToken(authToken,userDetails)){
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                logger.info("authorizated user '{}', setting security context", username);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        if(flag)
            httpServletResponse.sendError(401,errorMessage);
        else
            filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
