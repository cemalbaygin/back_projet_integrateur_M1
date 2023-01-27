package fr.uga.miage.m1.repository;

import fr.uga.miage.m1.entity.Commande;
import fr.uga.miage.m1.entity.CommandeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface  CommandeTypeRepository  extends JpaRepository<CommandeType, Integer> {


}
