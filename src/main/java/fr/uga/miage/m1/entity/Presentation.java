package fr.uga.miage.m1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "presentation")
public class Presentation {
    @Id
    private Long codeCIP13;

    @NonNull
    @Column(length = 512)
    private String libelle;

    @NonNull
    @Column
    private Double prix;

    @NonNull
    private Integer tauxRemboursement;

    @ManyToOne
    private Medicament medicament;
}