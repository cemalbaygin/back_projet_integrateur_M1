package fr.uga.miage.m1.service;

import fr.uga.miage.m1.entity.CommandeType;
import fr.uga.miage.m1.entity.Etablissement;
import fr.uga.miage.m1.entity.Utilisateur;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommandeTypeService {

    public List<CommandeType> getListCommandeType(Utilisateur utilisateur){

        List<CommandeType> commandeTypes = new ArrayList<>();

        Etablissement etablissement = utilisateur.getEtablissement();
        List<Utilisateur> utilisateurs= etablissement.getUtilisateurs();

        for (Utilisateur u: utilisateurs){
            commandeTypes.addAll(u.getCommandeTypes());
        }
        return commandeTypes;

    }
}
