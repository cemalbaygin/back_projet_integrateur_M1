package fr.uga.miage.m1.service;

import fr.uga.miage.m1.model.dto.EtablissementDto;
import fr.uga.miage.m1.model.mapper.EtablissementMapper;
import fr.uga.miage.m1.repository.EtablissementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EtablissementService {
    private final EtablissementRepository etablissementRepository;

    private final EtablissementMapper etablissementMapper;

    public List<EtablissementDto> getEtablissements(){
        return etablissementRepository.findAll()
                .stream()
                .map(etablissementMapper::entityToDto)
                .toList();
    }
}
