package com.example.ScienceStationProject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name="co_author")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CoAuthor {

    @Id
    @GeneratedValue
    private Long id;

    @Email
    private String email;

    @NotBlank
    private String country;

    @NotBlank
    private String city;

}
