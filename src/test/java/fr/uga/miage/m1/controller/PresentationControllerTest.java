package fr.uga.miage.m1.controller;

import fr.uga.miage.m1.config.SecurityConfiguration;
import fr.uga.miage.m1.entity.Presentation;
import fr.uga.miage.m1.model.dto.Normalizer;
import fr.uga.miage.m1.model.dto.PresentationDTO;
import fr.uga.miage.m1.service.PresentationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PresentationController.class, excludeAutoConfiguration = SecurityConfiguration.class)
public class PresentationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PresentationService presentationService;

    private PresentationDTO dto1;
    private PresentationDTO dto2;
    private PresentationDTO dto3;
    private Page<PresentationDTO> presentationDtos;



    /**
     * Test que la route /api/public/presentations renvoie la liste des présentations présents dans la bdd
     * @see PresentationController#index(Optional, Pageable) 
     */

    @BeforeEach
    void before(){

        this.dto1 = new PresentationDTO();
        dto1.setCodeCIP13(1234443L);
        dto1.setLibelle("plaquette(s) aluminium de 28 comprimé(s)");
        dto1.setPrix(13d);
        dto1.setTauxRemboursement(65);
        dto1.setQuantiteStock(34);

        this.dto2 = new PresentationDTO();
        dto2.setCodeCIP13(4534443L);
        dto2.setLibelle("plaquette(s) PVC PVDC aluminium de 14 comprimé(s)");
        dto2.setPrix(11d);
        dto2.setTauxRemboursement(65);
        dto2.setQuantiteStock(14);

        this.dto3 = new PresentationDTO();
        dto3.setCodeCIP13(6784443L);
        dto3.setLibelle("1 plaquette(s) PVC PVDC aluminium de 1 comprimé(s)");
        dto3.setPrix(17d);
        dto3.setTauxRemboursement(65);
        dto3.setQuantiteStock(20);
    }

    @Test
    void getAllPresentations(){
        final Pageable pageable = PageRequest.of(0,3);
        List<PresentationDTO> list = Arrays.asList(dto1, dto2, dto3);
        //Page<Presentation> presentations = new PageImpl<Presentation>();
    }

}
