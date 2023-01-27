package fr.uga.miage.m1.model.mapper;

import fr.uga.miage.m1.entity.CommandePresentation;
import fr.uga.miage.m1.model.dto.PanierPresentationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommandeMapper {
    AutoMapper autoMapper = Mappers.getMapper(AutoMapper.class);
    PresentationMapper presentationMapper = Mappers.getMapper(PresentationMapper.class);


    default List<PanierPresentationDTO> panierToDto(List<CommandePresentation> panier) {
        return panier.stream().map(commandePresentation -> {
            PanierPresentationDTO dto = new PanierPresentationDTO();
            dto.setPresentation(autoMapper.entityToDto(commandePresentation.getPresentation()));
            dto.setMedicament(autoMapper.entityToDto(commandePresentation.getPresentation().getMedicament()));
            dto.setQuantite(commandePresentation.getQuantite());
            dto.setPrescriptions(commandePresentation.getPresentation().getMedicament().getPrescriptions());

            return dto;
        }).toList();
    }

}
