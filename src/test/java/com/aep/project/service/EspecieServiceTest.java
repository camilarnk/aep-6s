package com.aep.project.service;

import com.aep.project.exception.EspecieNotFoundException;
import com.aep.project.model.Bioma;
import com.aep.project.model.Especie;
import com.aep.project.model.Grupo;
import com.aep.project.model.NivelRisco;
import com.aep.project.repository.EspecieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EspecieServiceTest {

    @Mock
    private EspecieRepository especieRepository;

    @InjectMocks
    private EspecieService especieService;

    private Especie especie;

    @BeforeEach
    public void configurarEspecie() {
        especie = new Especie();

        especie.setNomePopular("Ararinha-Azul");
        especie.setNomeCientifico("Cyanopsitta spixii");
        especie.setGrupo(Grupo.AVE);
        especie.setBioma(Bioma.AMAZONIA);
        especie.setNivelRisco(NivelRisco.CRITICO);
        especie.setPopulacaoEstimada(200);
    }

    @Test
    @DisplayName("Deve criar uma espécie")
    public void criarEspecie() {
        when(especieRepository.save(especie)).thenReturn(especie);

        Especie especieCriada = especieService.criar(especie);

        assertEquals(especie, especieCriada);
        verify(especieRepository).save(especie);
    }

    @Test
    @DisplayName("Deve buscar todas as espécies quando filtro não for aplicado")
    public void buscarTodasEspecies() {
        List<Especie> especies = List.of(especie);

        when(especieRepository.findAll()).thenReturn(especies);

        List<Especie> resultado = especieService.buscar(null);

        assertEquals(especies, resultado);
        verify(especieRepository).findAll();
    }

    @Test
    @DisplayName("Deve buscar espécies por nome popular")
    public void buscarPorNomePopular() {
        List<Especie> especies = List.of(especie);

        when(especieRepository.findByNomePopularContainingIgnoreCase("Ararinha"))
                .thenReturn(especies);

        List<Especie> resultado = especieService.buscar("Ararinha");

        assertEquals(especies, resultado);
        verify(especieRepository).findByNomePopularContainingIgnoreCase("Ararinha");
    }

    @Test
    @DisplayName("Deve buscar todas espécies quando nome estiver vazio")
    public void buscarComNomeVazio() {
        List<Especie> especies = List.of();

        when(especieRepository.findAll()).thenReturn(especies);

        List<Especie> resultado = especieService.buscar("");

        assertEquals(especies, resultado);
        verify(especieRepository).findAll();
    }

    @Test
    @DisplayName("Deve buscar uma espécie por id")
    public void buscarPorId() {
        especie.setId("1");
        when(especieRepository.findById("1")).thenReturn(Optional.of(especie));

        Especie especieBuscada = especieService.buscarPorId("1");

        assertEquals(especie, especieBuscada);
        verify(especieRepository).findById("1");
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar por id inexistente")
    public void buscarPorIdInexistente() {
        when(especieRepository.findById("100")).thenReturn(Optional.empty());

        assertThrows(EspecieNotFoundException.class,() -> especieService.buscarPorId("100"));
        verify(especieRepository).findById("100");
    }

    @Test
    @DisplayName("Deve atualizar uma espécie por id")
    public void deveAtualizarUmaEspeciePorId() {
        especie.setId("1");

        Especie especieAtualizada = new Especie();
        especieAtualizada.setNomePopular("Arara-Azul");
        especieAtualizada.setNomeCientifico("Anodorhynchus hyacinthinus");
        especieAtualizada.setGrupo(Grupo.AVE);
        especieAtualizada.setBioma(Bioma.CERRADO);
        especieAtualizada.setNivelRisco(NivelRisco.ALTO);
        especieAtualizada.setPopulacaoEstimada(500);

        when(especieRepository.findById("1")).thenReturn(Optional.of(especie));
        when(especieRepository.save(especie)).thenReturn(especie);

        Especie resultado = especieService.atualizar("1", especieAtualizada);

        assertEquals("Arara-Azul", resultado.getNomePopular());
        assertEquals("Anodorhynchus hyacinthinus", resultado.getNomeCientifico());
        assertEquals(Bioma.CERRADO, resultado.getBioma());
        assertEquals(NivelRisco.ALTO, resultado.getNivelRisco());
        assertEquals(500, resultado.getPopulacaoEstimada());

        verify(especieRepository).findById("1");
        verify(especieRepository).save(especie);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar por id inexistente")
    public void atualizarPorIdInexistente() {
        when(especieRepository.findById("100")).thenReturn(Optional.empty());

        assertThrows(EspecieNotFoundException.class, () -> especieService.atualizar("100", especie));

        verify(especieRepository).findById("100");
        verify(especieRepository, never()).save(especie);
    }

    @Test
    @DisplayName("Deve deletar uma espécie por id")
    public void deletarEspecie() {
        especie.setId("10");

        when(especieRepository.findById("10")).thenReturn(Optional.of(especie));

        especieService.deletar("10");

        verify(especieRepository).findById("10");
        verify(especieRepository).deleteById("10");
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar uma espécie por id inexistente")
    public void deletarPorIdInexistente() {
        when(especieRepository.findById("10")).thenReturn(Optional.empty());

        assertThrows(EspecieNotFoundException.class, () -> especieService.deletar("10"));

        verify(especieRepository).findById("10");
        verify(especieRepository, never()).deleteById("10");
    }

}
