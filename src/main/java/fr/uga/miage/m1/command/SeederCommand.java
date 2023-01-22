package fr.uga.miage.m1.command;

import fr.uga.miage.m1.entity.*;
import fr.uga.miage.m1.model.GroupeMedicamentPrincipeActifKey;
import fr.uga.miage.m1.model.MedicamentExcipientKey;
import fr.uga.miage.m1.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class SeederCommand implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(SeederCommand.class);

    @Autowired
    GroupeMedicamentsRepository groupeMedicamentsRepository;
    @Autowired
    FabricantsRepository fabricantsRepository;
    @Autowired
    MedicamentsRepository medicamentsRepository;
    @Autowired
    TypesAdministrationRepository typesAdministrationRepository;
    @Autowired
    GroupeMedicamentsPrincipeActifRepository groupeMedicamentsPrincipeActifRepository;
    @Autowired
    PrincipesActifsRepository principesActifsRepository;
    @Autowired
    MedicamentsExcipientsRepository medicamentsExcipientsRepository;
    @Autowired
    ExcipientsRepository excipientsRepository;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Import start");

        //importMedicament();
        //importGroupeMedicament();

        //importComposition();

        logger.info("Import end");
    }

    private void importComposition() throws IOException {
        InputStream is = SeederCommand.class.getClassLoader().getResourceAsStream("dataBrut/CIS_COMPO_bdpm.txt");

        logger.info("Import compositions");

        int colCodeCis = 0;
        int colLibelleSubstance = 3;
        int colCodeSubstance = 2;
        int colDosageSubstance = 4;
        int colNature = 6;

        String naturePrincipeActif = "SA";
        String natureExcipient = "ST";

        InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.ISO_8859_1);
        BufferedReader reader = new BufferedReader(streamReader);

        String line;
        int counter = 0;

        while ((line = reader.readLine()) != null) {
            counter++;
            String[] columns = line.split("\t");

            MedicamentEntity med = medicamentsRepository.findById(Long.parseLong(columns[colCodeCis])).orElse(null);
            Long substanceId = Long.parseLong(columns[colCodeSubstance]);

            if (med == null) continue;

            if (counter % 1000 == 0) logger.info((counter * 100 / 32000) + "%");

            if (columns[colNature].equals(naturePrincipeActif)) {
                GroupeMedicamentEntity groupeMedicament = med.getGroupeMedicament();

                GroupeMedicamentPrincipeActifKey id = new GroupeMedicamentPrincipeActifKey(groupeMedicament.getId(), substanceId);
                if (!groupeMedicamentsPrincipeActifRepository.existsById(id)) {
                    PrincipeActifEntity principeActif = new PrincipeActifEntity();
                    principeActif.setId(Long.valueOf(columns[colCodeSubstance]));
                    principeActif.setLibelle(columns[colLibelleSubstance]);
                    principesActifsRepository.save(principeActif);

                    GroupeMedicamentPrincipeActifEntity asso = new GroupeMedicamentPrincipeActifEntity();
                    asso.setId(id);
                    asso.setDosage(columns[colDosageSubstance]);
                    groupeMedicamentsPrincipeActifRepository.save(asso);
                }
            } else {
                MedicamentExcipientKey id = new MedicamentExcipientKey(med.getCodeCIS(), substanceId);

                if (!medicamentsExcipientsRepository.existsById(id)) {
                    ExcipientEntity excipient = new ExcipientEntity();
                    excipient.setId(Long.valueOf(columns[colCodeSubstance]));
                    excipient.setLibelle(columns[colLibelleSubstance]);
                    excipientsRepository.save(excipient);

                    MedicamentExcipientEntity asso = new MedicamentExcipientEntity();
                    asso.setId(id);
                    asso.setDosage(columns[colDosageSubstance]);
                    medicamentsExcipientsRepository.save(asso);
                }
            }

        }

        logger.info("Import end");

    }

    private void importMedicament() throws IOException {
        InputStream is = SeederCommand.class.getClassLoader().getResourceAsStream("dataBrut/CIS_bdpm.csv");

        logger.info("Import medoc");

        int colCodeCis = 0;
        int colLibelle = 1;
        int colFormePharmaceutique = 2;
        int colTitulaires = 10;
        int colSurveillanceRenforcee = 11;
        int colVoiesAdmin = 3;

        InputStreamReader streamReader =
                new InputStreamReader(is, StandardCharsets.ISO_8859_1);
        BufferedReader reader = new BufferedReader(streamReader);

        String line;
        int counter = 0;
        while ((line = reader.readLine()) != null) {
            counter++;
            if (counter % 1000 == 0) logger.info((counter * 100 / 15000) + "%");

            String[] columns = line.split("\t");

            String[] titulaires = columns[colTitulaires].split(";");
            List<FabricantEntity> fabricants = new ArrayList<>();
            for (String titulaire : titulaires) {
                if (!fabricantsRepository.existsByLibelle(titulaire)) {
                    FabricantEntity fabricantEntity = new FabricantEntity();
                    fabricantEntity.setLibelle(titulaire);
                    fabricantsRepository.save(fabricantEntity);
                }

                fabricants.add(fabricantsRepository.getByLibelle(titulaire));
            }
            String[] voiesAdministration = columns[colVoiesAdmin].split(";");

            List<TypeAdministrationEntity> voies = new ArrayList<>();
            for (String voieAdministration : voiesAdministration) {
                if (!typesAdministrationRepository.existsByLibelle(voieAdministration)) {
                    TypeAdministrationEntity typeAdministration = new TypeAdministrationEntity();
                    typeAdministration.setLibelle(voieAdministration);
                    typesAdministrationRepository.save(typeAdministration);
                }

                voies.add(typesAdministrationRepository.getByLibelle(voieAdministration));
            }


            if (!medicamentsRepository.existsById(Long.parseLong(columns[colCodeCis]))) {
                MedicamentEntity medicament = new MedicamentEntity();
                medicament.setCodeCIS(Long.parseLong(columns[colCodeCis]));
                medicament.setLibelle(columns[colLibelle]);
                medicament.setFormePharmaceutique(columns[colFormePharmaceutique]);
                medicament.setFabricants(fabricants);
                medicament.setASurveillanceRenforce(columns[colSurveillanceRenforcee].equals("Oui"));
                medicament.setTypesAdministration(voies);
                medicamentsRepository.save(medicament);
            }
        }

    }

    public void importGroupeMedicament() throws IOException {
        logger.info("Import groupe medicament");

        InputStream is = SeederCommand.class.getClassLoader().getResourceAsStream("dataBrut/CIS_GENER_bdpm.csv");

        InputStreamReader streamReader =
                new InputStreamReader(is, StandardCharsets.ISO_8859_1);
        BufferedReader reader = new BufferedReader(streamReader);

        int colIdGroupeMedicament = 0;
        int colLabelGroupeMedicament = 1;
        int colCodeCis = 2;
        int colTypeMedicament = 3;

        String line;
        int counter = 0;
        while ((line = reader.readLine()) != null) {
            counter++;
            if (counter % 1000 == 0) logger.info((counter * 100 / 15000) + "%");

            String[] columns = line.split("\t");
            Long idGroupeMedicament = Long.parseLong(columns[colIdGroupeMedicament]);

            if (!groupeMedicamentsRepository.existsById(idGroupeMedicament)) {
                GroupeMedicamentEntity groupeMedicamentEntity = new GroupeMedicamentEntity();
                groupeMedicamentEntity.setLibelle(columns[colLabelGroupeMedicament]);
                groupeMedicamentsRepository.save(groupeMedicamentEntity);
            }

            Optional<MedicamentEntity> medicament = medicamentsRepository.findById(Long.parseLong(columns[colCodeCis]));
            if (medicament.isPresent()) {
                MedicamentEntity med = medicament.get();
                med.setGroupeMedicament(groupeMedicamentsRepository.findById(idGroupeMedicament).get());
                med.setEstReference(columns[colTypeMedicament].equals("0"));
                medicamentsRepository.save(med);
            }
        }

        medicamentsRepository.findByGroupeMedicamentIsNull().forEach((med) -> {
            med.setEstReference(true);
            GroupeMedicamentEntity groupeMedicament = new GroupeMedicamentEntity();
            groupeMedicament.setLibelle(med.getLibelle());
            med.setGroupeMedicament(groupeMedicament);
            groupeMedicamentsRepository.save(groupeMedicament);
            medicamentsRepository.save(med);
        });
    }
}
