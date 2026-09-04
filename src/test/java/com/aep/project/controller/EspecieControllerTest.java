package com.aep.project.controller;

import com.aep.project.model.Bioma;
import com.aep.project.model.Especie;
import com.aep.project.model.Grupo;
import com.aep.project.model.NivelRisco;
import com.aep.project.service.EspecieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EspecieController.class)
public class EspecieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EspecieService especieService;

    private Especie especie;

    @BeforeEach
    public void configurarEspecie() {
        especie = new Especie();

        especie.setId("1");
        especie.setNomePopular("Ararinha-Azul");
        especie.setNomeCientifico("Cyanopsitta spixii");
        especie.setGrupo(Grupo.AVE);
        especie.setBioma(Bioma.AMAZONIA);
        especie.setNivelRisco(NivelRisco.CRITICO);
        especie.setPopulacaoEstimada(200);
    }

    @Test
    @DisplayName("Deve criar uma espécie em /POST")
    public void criarUmaEspecie() throws Exception {
        when(especieService.criar(any(Especie.class))).thenReturn(especie);

        String json = """
        {
            "nomePopular": "Ararinha-Azul",
            "nomeCientifico": "Cyanopsitta spixii",
            "grupo": "AVE",
            "bioma": "AMAZONIA",
            "nivelRisco": "CRITICO",
            "populacaoEstimada": 200
        }
        """;

        mockMvc.perform(post("/especies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated());
    }

}
