package fr.uga.miage.m1.entity;

import fr.uga.miage.m1.model.dto.MedicamentDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Table(name = "groupe_medicament")
@Setter
public class GroupeMedicament {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 512)
    @NonNull
    private String libelle;

    @OneToMany(mappedBy = "groupeMedicament")
    private List<GroupeMedicamentPrincipeActif> groupeMedicamentAssoc;

    @OneToMany(mappedBy = "groupeMedicament")
    private List<Medicament> medicaments;
}
 