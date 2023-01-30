package fr.uga.miage.m1.service;

import fr.uga.miage.m1.entity.*;
import fr.uga.miage.m1.model.EtatCommande;
import fr.uga.miage.m1.model.dto.PanierPresentationDTO;
import fr.uga.miage.m1.model.dto.PresentationMedicamentDTO;
import fr.uga.miage.m1.model.key.CommandePresentationKey;
import fr.uga.miage.m1.model.mapper.CommandeMapper;
import fr.uga.miage.m1.model.mapper.PresentationMapper;
import fr.uga.miage.m1.model.request.AjouterAuPanierDTO;
import fr.uga.miage.m1.repository.CommandesPresentationRepository;
import fr.uga.miage.m1.repository.CommandesRepository;
import fr.uga.miage.m1.repository.PresentationsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log
public class PanierService {
    private final CommandesRepository commandeRepository;
    private final PresentationsRepository presentationsRepository;
    private final CommandesPresentationRepository commandesPresentationRepository;
    private final CommandeMapper commandeMapper;
    private final PresentationMapper presentationMapper;
    private final CommandeService commandeService;

    public Commande getOrCreatePanier(Utilisateur utilisateur) {
        Commande commande = commandeRepository.getPanier(utilisateur.getId()).orElse(null);
        if (commande == null) {
            commande = new Commande();
            commande.setEtat(EtatCommande.panier);
            commande.setUtilisateur(utilisateur);
            commande.setDateAchat(new Timestamp(System.currentTimeMillis()));
            commande = commandeRepository.saveAndFlush(commande);
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

    public List<PresentationMedicamentDTO> getSimilaires(String codeCIP13) {
        Presentation presentation = presentationsRepository.findById(Long.valueOf(codeCIP13)).orElseThrow();

        List<Presentation> presentationMedicament = presentation.getMedicament().getPresentations();

        List<Medicament> meds = presentation.getMedicament()
                .getGroupeMedicament()
                .getMedicaments();

        for (Medicament m : meds) {
            presentationMedicament.addAll(m.getPresentations());
        }

        return presentationMedicament.stream().map(m -> presentationMapper.presentationMedicamentDTO(m)).collect(Collectors.toList());
    }

    public Boolean substituerProduit(Utilisateur utilisateur, String sourceCodeCIP13, String destinationCodeCIP13) {
        Commande panier = this.getOrCreatePanier(utilisateur);

        Presentation presentation = presentationsRepository.findById(Long.valueOf(sourceCodeCIP13)).orElseThrow();

        CommandePresentationKey commandePresentationKey = new CommandePresentationKey(panier.getId(), presentation.getCodeCIP13());
        CommandePresentation commandePresentation = commandesPresentationRepository.findById(commandePresentationKey).orElseThrow();

        Integer quantite = commandePresentation.getQuantite();

        commandesPresentationRepository.delete(commandePresentation);

        commandePresentationKey = new CommandePresentationKey(panier.getId(), Long.valueOf(destinationCodeCIP13));
        commandePresentation = new CommandePresentation();
        commandePresentation.setId(commandePresentationKey);
        commandePresentation.setQuantite(quantite);

        commandesPresentationRepository.save(commandePresentation);

        return true;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean passerCommande(Utilisateur user) {

        Commande panier = commandeRepository.getPanier(user.getId()).orElseThrow();
        List<CommandePresentation> commandePresentations = panier.getCommandePresentations();
        if (commandePresentations.size() == 0) throw new NoSuchElementException();

        panier.setEtat(EtatCommande.attente_paiement);
        panier.setDateAchat(new Timestamp(System.currentTimeMillis()));

        for (int i = 0; i < commandePresentations.size(); i++) {

            CommandePresentation comPres = commandePresentations.get(i);
            Presentation pres = comPres.getPresentation();

            comPres.setEtat(EtatCommande.attente_paiement);
            comPres.setPrixAchat(pres.getPrix());

            if (pres.getQuantiteStock() >= comPres.getQuantite()) {
                pres.setQuantiteStock(pres.getQuantiteStock() - comPres.getQuantite());
                comPres.setEtat(EtatCommande.attente_paiement_reserver);
                presentationsRepository.save(pres);
            }

            commandesPresentationRepository.save(comPres);
        }

        commandeRepository.save(panier);

        return true;
    }
}
