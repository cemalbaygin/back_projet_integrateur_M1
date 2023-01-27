package fr.uga.miage.m1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Table(name = "fabricant")
@Setter
public class Fabricant {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 512)
    @NonNull
    private String libelle;

    @ManyToMany(mappedBy = "fabricants")
    List<Medicament> medicaments;
}