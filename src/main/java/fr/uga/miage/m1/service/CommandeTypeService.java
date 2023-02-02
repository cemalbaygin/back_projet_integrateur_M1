package fr.uga.miage.m1.service;

import fr.uga.miage.m1.entity.CommandeType;
import fr.uga.miage.m1.entity.Etablissement;
import fr.uga.miage.m1.entity.PresentationCommandeType;
import fr.uga.miage.m1.entity.Utilisateur;
import fr.uga.miage.m1.model.dto.CommandeTypeDTO;
import fr.uga.miage.m1.model.dto.PresentationForCommandeTypeDTO;
import fr.uga.miage.m1.repository.EtablissementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
@Log
public class CommandeTypeService {

    private final EtablissementRepository etablissementRepository;

    public List<CommandeTypeDTO> getListCommandeType(Utilisateur utilisateur) throws NoSuchElementException{
        int idEtablissement;
        List<CommandeType> commandeTypes = new ArrayList<>();
        List<CommandeTypeDTO> commandeTypeDTOS = new ArrayList<>();
        List<PresentationForCommandeTypeDTO> presentationForCommandeTypeDTOS;

        if(utilisateur.getEtablissement()==null){
            throw new NoSuchElementException("Aucun etablissement liée a l'utilisateur :"+ utilisateur.getEmail());
        }

        idEtablissement = utilisateur.getEtablissement().getId();
        Etablissement etablissement = etablissementRepository.findById(idEtablissement).orElseThrow(() -> new NoSuchElementException("Aucun etablissement trouvé avec l'id :"+ idEtablissement));

        List<Utilisateur> utilisateurs=  etablissement.getUtilisateurs();
        for (Utilisateur u: utilisateurs){
            u.getEmail();
            commandeTypes.addAll(u.getCommandeTypes());
        }

        for (CommandeType c : commandeTypes) {
            presentationForCommandeTypeDTOS = new ArrayList<>();

            for (PresentationCommandeType ctp: c.getPresentationCommandeTypes()) {

                var presForCom = PresentationForCommandeTypeDTO.builder()
                        .libelleMedicament(ctp.getPresentation().getMedicament().getLibelle())
                        .libelle(ctp.getPresentation().getLibelle())
                        .quantite(ctp.getQuantite())
                        .codeCIP13(ctp.getPresentation().getCodeCIP13()).build();

                presentationForCommandeTypeDTOS.add(presForCom);
            }

            var commandeTypeDTO = CommandeTypeDTO.builder()
                    .presentations(presentationForCommandeTypeDTOS)
                    .libelle(c.getLibelle()).build();

            commandeTypeDTOS.add(commandeTypeDTO);
        }

        return commandeTypeDTOS;

    }
}
