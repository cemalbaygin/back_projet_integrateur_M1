package fr.uga.miage.m1.service;

import fr.uga.miage.m1.entity.*;
import fr.uga.miage.m1.model.EtatCommande;
import fr.uga.miage.m1.model.dto.PanierPresentationDTO;
import fr.uga.miage.m1.model.key.CommandePresentationKey;
import fr.uga.miage.m1.model.mapper.CommandeMapper;
import fr.uga.miage.m1.model.request.AjouterAuPanierDTO;
import fr.uga.miage.m1.repository.CommandesPresentationRepository;
import fr.uga.miage.m1.repository.CommandesRepository;
import fr.uga.miage.m1.repository.PresentationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PanierService {
    private final CommandesRepository commandeRepository;
    private final PresentationsRepository presentationsRepository;
    private final CommandesPresentationRepository commandesPresentationRepository;
    private final CommandeMapper commandeMapper;

    public Commande getOrCreatePanier(Utilisateur utilisateur) {
        Commande commande = commandeRepository.getPanier(utilisateur.getId()).orElse(null);
        if (commande == null) {
            commande = new Commande();
            commande.setEtat(EtatCommande.panier);
            commande.setUtilisateur(utilisateur);
            commande.setDateAchat(new Timestamp(System.currentTimeMillis()));
            commande = commandeRepository.save(commande);
        }
        return commande;
    }

    public List<PanierPresentationDTO> getPanier(Utilisateur utilisateur) {
        return commandeMapper.panierToDto(getOrCreatePanier(utilisateur).getCommandePresentations());
    }

    public Boolean addPresentationToPanier(Utilisateur utilisateur, AjouterAuPanierDTO dto) {
        Commande commande = this.getOrCreatePanier(utilisateur);
        Presentation pres = presentationsRepository.findById(Long.valueOf(dto.getCode_CIP13())).orElseThrow();

        CommandePresentationKey commandePresentationKey = new CommandePresentationKey(commande.getId(), pres.getCodeCIP13());
        CommandePresentation commandePresentation = commandesPresentationRepository.findById(commandePresentationKey).orElse(null);
        if (commandePresentation == null) {
            commandePresentation = new CommandePresentation();
            commandePresentation.setId(commandePresentationKey);
            commandePresentation.setQuantite(dto.getQuantite());
            commandePresentation.setEtat(EtatCommande.panier);
        } else {
            commandePresentation.setQuantite(commandePresentation.getQuantite() + dto.getQuantite());
        }
        commandesPresentationRepository.save(commandePresentation);

        return true;
    }

    public List<PanierPresentationDTO> updateFromPanier(Utilisateur utilisateur, AjouterAuPanierDTO dto) {
        Commande commande = this.getOrCreatePanier(utilisateur);
        Presentation pres = presentationsRepository.findById(Long.valueOf(dto.getCode_CIP13())).orElseThrow();

        CommandePresentationKey commandePresentationKey = new CommandePresentationKey(commande.getId(), pres.getCodeCIP13());
        CommandePresentation commandePresentation = commandesPresentationRepository.findById(commandePresentationKey).orElseThrow();

        commandePresentation.setQuantite(dto.getQuantite());

        commandesPresentationRepository.save(commandePresentation);

        return getPanier(utilisateur);
    }


    public List<PanierPresentationDTO> deleteFromPanier(Utilisateur utilisateur, String codeCIP13) {
        Commande commande = this.getOrCreatePanier(utilisateur);

        CommandePresentationKey commandePresentationKey = new CommandePresentationKey(commande.getId(), Long.valueOf(codeCIP13));
        CommandePresentation commandePresentation = commandesPresentationRepository.findById(commandePresentationKey).orElseThrow();
        commandesPresentationRepository.delete(commandePresentation);

        return getPanier(utilisateur);
    }

    public List<PanierPresentationDTO> substituerProduit(Utilisateur utilisateur, String codeCIP13, Integer quantite) {
        Commande commande = this.getOrCreatePanier(utilisateur);

        Presentation presentation = presentationsRepository.findById(Long.valueOf(codeCIP13)).orElseThrow();

        CommandePresentationKey commandePresentationKey = new CommandePresentationKey(commande.getId(), presentation.getCodeCIP13());
        CommandePresentation commandePresentation = commandesPresentationRepository.findById(commandePresentationKey).orElseThrow();

        List<Presentation> presentationMedicament = presentation.getMedicament().getPresentations();
        Optional<Presentation> presentationSubstitute = presentationMedicament.stream().filter(pres -> pres.getQuantiteStock() - quantite >= 0 && pres.getCodeCIP13() != Long.valueOf(codeCIP13)).findAny();

        if (!presentationSubstitute.isPresent()) {
            List<Medicament> med = presentation.getMedicament()
                    .getGroupeMedicament()
                    .getMedicaments();

            for (int i = 0; i < med.size(); i++) {
                presentationSubstitute = med.get(i).getPresentations().stream().filter(
                        p -> p.getCodeCIP13() != presentation.getCodeCIP13() && p.getQuantiteStock() - quantite >= 0
                ).findAny();

                if (presentationSubstitute.isPresent()) continue;
            }
        }

        commandePresentation.setPresentation(presentationSubstitute.get());
        commandesPresentationRepository.save(commandePresentation);

        return getPanier(utilisateur);
    }
}
