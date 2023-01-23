package fr.uga.miage.m1.model.mapper;

import fr.uga.miage.m1.entity.Presentation;
import fr.uga.miage.m1.model.dto.PresentationDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PresentationMapper {
    PresentationDTO entityToDto(Presentation entity);
}
