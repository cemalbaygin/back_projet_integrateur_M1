package fr.uga.miage.m1.entity;

import fr.uga.miage.m1.model.EtatCommande;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "commande")
@Getter
@Setter
public class Commande {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private Timestamp dateAchat;

    @Column
    private EtatCommande etat;

    @OneToMany(mappedBy = "commande")
    private List<CommandePresentation> commande;

}
