package com.aep.project.controller;

import com.aep.project.exception.EspecieNotFoundException;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @Test
    @DisplayName("Deve listar todas espécies em /GET")
    public void listarTodasEspecies() throws Exception {
        when(especieService.buscar(null)).thenReturn(List.of(especie));

        mockMvc.perform(get("/especies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"));

    }

    @Test
    @DisplayName("Deve listar espécies por nome popular em /GET")
    public void listarEspeciesPorNomePopular() throws Exception {
        when(especieService.buscar("Onça-Pintada")).thenReturn(List.of(especie));

        mockMvc.perform(get("/especies")
                .param("nomePopular", "Onça-Pintada"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"));

    }

    @Test
    @DisplayName("Deve listar espécie por Id em /GET")
    public void listarEspeciePorId() throws Exception {
        when(especieService.buscarPorId("1")).thenReturn(especie);

        mockMvc.perform(get("/especies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.nomePopular").value("Ararinha-Azul"));
    }

    @Test
    @DisplayName("Deve atualizar espécie por Id em /PUT")
    public void atualizarEspeciePorId() throws Exception {
        when(especieService.atualizar(any(String.class), any(Especie.class))).thenReturn(especie);

        String json = """
        {
            "nomePopular": "Ararinha-Azul",
            "nomeCientifico": "Cyanopsitta spixii",
            "grupo": "AVE",
            "bioma": "AMAZONIA",
            "nivelRisco": "CRITICO",
            "populacaoEstimada": 250
        }
        """;

        mockMvc.perform(put("/especies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.nomePopular").value("Ararinha-Azul"));
    }

    @Test
    @DisplayName("Deve excluir espécie por Id")
    public void excluirEspeciePorId() throws Exception {
        mockMvc.perform(delete("/especies/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar 400 ao criar espécie com dados inválidos")
    public void criarEspecieComDadosInvalidos() throws Exception {
        String json = """
        {
            "nomePopular": "",
            "nomeCientifico": "",
            "grupo": null,
            "bioma": null,
            "nivelRisco": null,
            "populacaoEstimada": -1
        }
        """;

        mockMvc.perform(post("/especies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar espécie inexistente")
    public void buscarEspecieInexistente() throws Exception {
        when(especieService.buscarPorId("999"))
                .thenThrow(new EspecieNotFoundException("Espécie não encontrada com id 999"));

        mockMvc.perform(get("/especies/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Espécie não encontrada"))
                .andExpect(jsonPath("$.mensagem")
                        .value("Espécie não encontrada com id 999"));
    }

    @Test
    @DisplayName("Deve retornar 400 ao atualizar espécie com dados inválidos")
    public void atualizarEspecieComDadosInvalidos() throws Exception {
        String json = """
        {
            "nomePopular": "",
            "nomeCientifico": "",
            "grupo": null,
            "bioma": null,
            "nivelRisco": null,
            "populacaoEstimada": -1
        }
        """;

        mockMvc.perform(put("/especies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

}
