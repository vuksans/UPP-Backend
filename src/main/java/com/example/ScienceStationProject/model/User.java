package com.example.ScienceStationProject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name="user")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements UserDetails{

    @Id
    @GeneratedValue
    private Long id;

    private String firstName;

    private String lastName;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    private String country;

    @NotBlank
    private String city;

    @NotNull
    private boolean emailVerified;

    @JsonIgnore
    @NotBlank
    private String hashedPassword;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private boolean deleted;

    @ManyToOne
    @JsonIgnore
    private ScienceBranch scienceBranch;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(userRole.name()));
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return hashedPassword;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return email;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return emailVerified;
    }
}
