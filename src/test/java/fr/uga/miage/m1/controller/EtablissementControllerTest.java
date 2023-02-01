package fr.uga.miage.m1.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import fr.uga.miage.m1.config.JwtAuthenticationFilter;
import fr.uga.miage.m1.config.SecurityConfiguration;
import fr.uga.miage.m1.model.dto.EtablissementDto;
import fr.uga.miage.m1.service.EtablissementService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = EtablissementController.class, excludeAutoConfiguration = SecurityConfiguration.class)
@WithMockUser
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EtablissementControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private EtablissementService etablissementService;
    private EtablissementDto etablissement1DTO;
    private EtablissementDto etablissement2DTO;
    private List<EtablissementDto> etablissementsDTO;

    @BeforeAll
    void before() {
        etablissement1DTO = new EtablissementDto();
        etablissement1DTO.setId(1);
        etablissement1DTO.setLibelle("Etablissement1");
        etablissement1DTO.setAdresse("adresse1");

        etablissement2DTO = new EtablissementDto();
        etablissement2DTO.setId(1);
        etablissement2DTO.setLibelle("Etablissement1");
        etablissement2DTO.setAdresse("adresse1");

        etablissementsDTO = Arrays.asList(etablissement1DTO,etablissement2DTO);
    }
    @Test
    public void getEtablissements() throws Exception {
        Mockito.when(etablissementService.getEtablissements()).thenReturn(etablissementsDTO);
        mockMvc.perform(get("/public/etablissements"))
                .andExpect(status().isOk());
    }
}