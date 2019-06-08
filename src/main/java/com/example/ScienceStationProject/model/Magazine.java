package com.example.ScienceStationProject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Magazine {

    @GeneratedValue
    @Id
    private Long id;

    @NotBlank
    private String issn;

    @NotBlank
    private String name;

    private boolean openAccess;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="main_editor_id",nullable = false)
    private User mainEditor;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name="magazine_editors")
    private List<User> editors;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name="magazine_reviewers")
    private List<User> reviewers;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "magazine_subscriptions")
    private List<User> subscribers;
}
