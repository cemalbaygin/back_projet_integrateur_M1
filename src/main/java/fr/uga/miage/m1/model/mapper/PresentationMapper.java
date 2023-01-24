package fr.uga.miage.m1.model.mapper;

import fr.uga.miage.m1.entity.GroupeMedicament;
import fr.uga.miage.m1.entity.Presentation;
import fr.uga.miage.m1.model.dto.GroupeMedicamentDTO;
import fr.uga.miage.m1.model.dto.MedicamentDTO;
import fr.uga.miage.m1.model.dto.PresentationCompleteDTO;
import fr.uga.miage.m1.model.dto.PrincipeActifDTO;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PresentationMapper {
    AutoMapper autoMapper = Mappers.getMapper(AutoMapper.class);

    default PresentationCompleteDTO entityToDto(Presentation presentation) {
        PresentationCompleteDTO dto = new PresentationCompleteDTO();
        dto.setPresentation(autoMapper.entityToDto(presentation));

        GroupeMedicamentDTO groupeMedicamentDTO = new GroupeMedicamentDTO();
        GroupeMedicament groupe = presentation.getMedicament().getGroupeMedicament();
        groupeMedicamentDTO.setLibelle(groupe.getLibelle());

        groupeMedicamentDTO.setPrincipesActifs(
                groupe.getGroupeMedicamentAssoc().stream().map(assoc -> {
                    PrincipeActifDTO principeActifDTO = new PrincipeActifDTO();
                    principeActifDTO.setDosage(assoc.getDosage());
                    principeActifDTO.setLibelle(assoc.getPrincipeActif().getLibelle());
                    return principeActifDTO;
                }).collect(Collectors.toList()));

        groupeMedicamentDTO.setMedicaments(
                groupe.getMedicaments().stream()
                        .map(med -> autoMapper.entityToDto(med)).collect(Collectors.toList())
        );


        dto.setPrescriptions(
                presentation.getMedicament()
                        .getPrescriptions()
                        .stream()
                        .map(prescription -> autoMapper.entityToDto(prescription))
                        .collect(Collectors.toList())
        );

        dto.setGroupeMedicament(groupeMedicamentDTO);

        return dto;
    }
}
