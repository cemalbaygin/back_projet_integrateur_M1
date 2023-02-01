package fr.uga.miage.m1.model.mapper;

import fr.uga.miage.m1.entity.*;
import fr.uga.miage.m1.model.dto.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AutoMapper {
    PresentationDTO entityToDto(Presentation entity);

    FabricantDTO entityToDto(Fabricant entity);

    MedicamentDTO entityToDto(Medicament entity);

    PrescriptionDTO entityToDto(Prescription prescription);

    CommandeDTO entityToDto(Commande entity);

    CommandePresentationDTO entityToDto(CommandePresentation entity);

    UtilisateurDTO entityToDto(Utilisateur entity);

    CommandeTypeDTO entityTdto(CommandeType entity);

    EtablissementDto entityToDto(Etablissement entity);
}