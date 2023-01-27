package fr.uga.miage.m1.controller;

/*
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
*/