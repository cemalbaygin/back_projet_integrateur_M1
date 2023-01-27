package fr.uga.miage.m1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "principe_actif")
public class PrincipeActif {
    @Id
    private Long id;

    @NonNull
    @Column(length = 512)
    private String libelle;

    @OneToMany(mappedBy = "principeActif")
    private List<GroupeMedicamentPrincipeActif> principeActifAssos;
}