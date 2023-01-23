package fr.uga.miage.m1.model.mapper;

import fr.uga.miage.m1.entity.Fabricant;
import fr.uga.miage.m1.entity.Medicament;
import fr.uga.miage.m1.entity.Presentation;
import fr.uga.miage.m1.model.dto.FabricantDTO;
import fr.uga.miage.m1.model.dto.MedicamentDTO;
import fr.uga.miage.m1.model.dto.PresentationDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AutoMapper {
    PresentationDTO entityToDto(Presentation entity);

    FabricantDTO entityToDto(Fabricant entity);

    MedicamentDTO entityToDto(Medicament entity);
}
