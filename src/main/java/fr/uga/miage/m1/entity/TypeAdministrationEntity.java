package fr.uga.miage.m1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Table(name="type_administration")
@Setter
public class TypeAdministrationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column( length = 255)
    @NonNull
    private String libelle;

    @ManyToMany(mappedBy = "typesAdministration")
    List<MedicamentEntity> medicaments;
}