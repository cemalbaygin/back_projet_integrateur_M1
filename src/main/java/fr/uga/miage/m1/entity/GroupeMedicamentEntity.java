package fr.uga.miage.m1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Getter
@Table(name="groupe_medicament")
@Setter
public class GroupeMedicamentEntity {
    @Id
    private Long id;

    @Column(length = 512)
    @NonNull
    private String libelle;
}
 