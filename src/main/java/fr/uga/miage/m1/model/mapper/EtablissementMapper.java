package fr.uga.miage.m1.model.mapper;

import fr.uga.miage.m1.entity.Etablissement;
import fr.uga.miage.m1.model.dto.EtablissementDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EtablissementMapper {

    EtablissementDto entityToDto(Etablissement etablissement);

}
