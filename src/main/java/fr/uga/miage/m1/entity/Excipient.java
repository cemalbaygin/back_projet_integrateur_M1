package fr.uga.miage.m1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "excipient")
public class Excipient {
    @Id
    private Long id;

    @NonNull
    @Column(length = 512)
    private String libelle;

    @OneToMany(mappedBy = "excipient")
    private List<MedicamentExcipient> excipientAssos;
}