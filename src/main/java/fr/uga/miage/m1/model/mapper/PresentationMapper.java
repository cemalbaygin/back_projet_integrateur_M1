package fr.uga.miage.m1.model.mapper;

import fr.uga.miage.m1.entity.GroupeMedicament;
import fr.uga.miage.m1.entity.Medicament;
import fr.uga.miage.m1.entity.Presentation;
import fr.uga.miage.m1.model.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PresentationMapper {
    AutoMapper autoMapper = Mappers.getMapper(AutoMapper.class);



    default PresentationMedicamentDTO presentationMedicamentDTO(Presentation entity) {
        PresentationMedicamentDTO dto = new PresentationMedicamentDTO();

        dto.setPresentation(autoMapper.entityToDto(entity));
        dto.setMedicament(autoMapper.entityToDto(entity.getMedicament()));

        return dto;
    }

    default PresentationCompleteDTO entityToDto(Presentation presentation) {
        Medicament medicament = presentation.getMedicament();
        PresentationCompleteDTO dto = new PresentationCompleteDTO();
        dto.setPresentations(
                medicament
                        .getPresentations()
                        .stream().map(pres -> autoMapper.entityToDto(pres))
                        .collect(Collectors.toList())
        );

        dto.setMedicament(autoMapper.entityToDto(medicament));

        dto.setExcipients(
                medicament.getMedicamentAssos().stream().map(medicamentExcipient -> {
                            ExcipientDTO dtoExcipient = new ExcipientDTO();
                            dtoExcipient.setDosage(medicamentExcipient.getDosage());
                            dtoExcipient.setLibelle(medicamentExcipient.getExcipient().getLibelle());
                            return dtoExcipient;
                        }
                ).collect(Collectors.toList())
        );

        GroupeMedicamentDTO groupeMedicamentDTO = new GroupeMedicamentDTO();
        GroupeMedicament groupe = medicament.getGroupeMedicament();
        groupeMedicamentDTO.setLibelle(groupe.getLibelle());

        groupeMedicamentDTO.setPrincipesActifs(
                groupe.getGroupeMedicamentAssoc().stream().map(assoc -> {
                    PrincipeActifDTO principeActifDTO = new PrincipeActifDTO();
                    principeActifDTO.setDosage(assoc.getDosage());
                    principeActifDTO.setLibelle(assoc.getPrincipeActif().getLibelle());
                    return principeActifDTO;
                }).collect(Collectors.toList()));


        List<Presentation> presMed = groupe.getMedicaments().stream().map(med -> med.getPresentations().stream().findFirst()).flatMap(Optional::stream).collect(Collectors.toList());

        groupeMedicamentDTO.setPresentationsMedicaments(
                presMed.stream()
                        .map(this::presentationMedicamentDTO).collect(Collectors.toList())
        );


        dto.setPrescriptions(
                medicament
                        .getPrescriptions()
                        .stream()
                        .map(prescription -> autoMapper.entityToDto(prescription))
                        .collect(Collectors.toList())
        );

        dto.setGroupeMedicament(groupeMedicamentDTO);

        return dto;
    }
}
