package fr.uga.miage.m1.command;

import fr.uga.miage.m1.entity.FabricantEntity;
import fr.uga.miage.m1.entity.GroupeMedicamentEntity;
import fr.uga.miage.m1.entity.MedicamentEntity;
import fr.uga.miage.m1.entity.TypeAdministrationEntity;
import fr.uga.miage.m1.repository.FabricantsRepository;
import fr.uga.miage.m1.repository.GroupeMedicamentsRepository;
import fr.uga.miage.m1.repository.MedicamentsRepository;
import fr.uga.miage.m1.repository.TypesAdministrationRepository;
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
public class SeederCommand  implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(SeederCommand.class);

    @Autowired
    GroupeMedicamentsRepository groupeMedicamentsRepository;
    @Autowired
    FabricantsRepository fabricantsRepository;
    @Autowired
    MedicamentsRepository medicamentsRepository;
    @Autowired
    TypesAdministrationRepository typesAdministrationRepository;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Import start");

        importMedicament();
        importGroupeGenerique();

        logger.info("Import end");
    }

    private void importMedicament() {
        InputStream is =  SeederCommand.class.getClassLoader().getResourceAsStream("dataBrut/CIS_bdpm.csv");

        logger.info("Import medoc");

        int colCodeCis = 0;
        int colLibelle = 1;
        int colFormePharmaceutique = 2;
        int colTitulaires = 10;
        int colSurveillanceRenforcee = 11;
        int colVoiesAdmin = 3;

        try (InputStreamReader streamReader =
                     new InputStreamReader(is, StandardCharsets.ISO_8859_1);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split("\t");

                String[] titulaires = columns[colTitulaires].split(";");
                List<FabricantEntity> fabricants = new ArrayList<>();
                for (String titulaire: titulaires) {
                    if(!fabricantsRepository.existsByLibelle(titulaire)){
                        FabricantEntity fabricantEntity = new FabricantEntity();
                        fabricantEntity.setLibelle(titulaire);
                        fabricantsRepository.save(fabricantEntity);
                    }

                    fabricants.add(fabricantsRepository.getByLibelle(titulaire));
                }
                String[] voiesAdministration = columns[colVoiesAdmin].split(";");

                List<TypeAdministrationEntity> voies = new ArrayList<>();
                for (String voieAdministration: voiesAdministration) {
                    if(!typesAdministrationRepository.existsByLibelle(voieAdministration)){
                        TypeAdministrationEntity typeAdministration = new TypeAdministrationEntity();
                        typeAdministration.setLibelle(voieAdministration);
                        typesAdministrationRepository.save(typeAdministration);
                    }

                    voies.add(typesAdministrationRepository.getByLibelle(voieAdministration));
                }


                if(!medicamentsRepository.existsById(Long.parseLong(columns[colCodeCis]))){
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importGroupeGenerique(){
        logger.info("Import groupe generique");

        InputStream is =  SeederCommand.class.getClassLoader().getResourceAsStream("dataBrut/CIS_GENER_bdpm.csv");

        try (InputStreamReader streamReader =
                     new InputStreamReader(is, StandardCharsets.ISO_8859_1);
             BufferedReader reader = new BufferedReader(streamReader)) {


            int colIdGroupeGenerique = 0;
            int colLabelGroupeGenerique = 1;
            int colCodeCis = 2;
            int colTypeGenerique = 3;

            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split("\t");
                Long idGroupeMedicament = Long.parseLong(columns[colIdGroupeGenerique]);

                if(!groupeMedicamentsRepository.existsById(idGroupeMedicament) ){
                    GroupeMedicamentEntity groupeMedicamentEntity = new GroupeMedicamentEntity();
                    groupeMedicamentEntity.setId(idGroupeMedicament);
                    groupeMedicamentEntity.setLibelle(columns[colLabelGroupeGenerique]);
                    groupeMedicamentsRepository.save(groupeMedicamentEntity);
                }

                Optional<MedicamentEntity> medicament = medicamentsRepository.findById(Long.parseLong(columns[colCodeCis]));
                if(medicament.isPresent()){
                    MedicamentEntity med = medicament.get();
                    med.setGroupeGenerique(groupeMedicamentsRepository.findById(idGroupeMedicament).get());
                    med.setEstReference(columns[colTypeGenerique].equals("0"));
                    medicamentsRepository.save(med);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
