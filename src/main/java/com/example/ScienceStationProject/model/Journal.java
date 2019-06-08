package com.example.ScienceStationProject.model;

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
public class Journal {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String headline;

    @NotBlank
    private String keyPoints;

    @Column(columnDefinition = "TEXT")
    private String journalAbstract;

    @ManyToOne
    private ScienceBranch branch;

    @ManyToOne
    private Magazine magazine;

    @ManyToOne
    private User author;

    @OneToMany
    private List<CoAuthor> coAuthors;

}
