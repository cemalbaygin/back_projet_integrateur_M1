package fr.uga.miage.m1.repository;
import fr.uga.miage.m1.entity.FabricantEntity;
import fr.uga.miage.m1.entity.TypeAdministrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypesAdministrationRepository extends JpaRepository<TypeAdministrationEntity, Long> {
    Boolean existsByLibelle(String libelle);
    TypeAdministrationEntity getByLibelle(String libelle);
}