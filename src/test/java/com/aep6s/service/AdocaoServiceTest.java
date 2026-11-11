package com.aep6s.service;

import com.aep6s.dto.AdocaoRequest;
import com.aep6s.exception.ResourceNotFoundException;
import com.aep6s.model.AdocaoModel;
import com.aep6s.model.AdotanteModel;
import com.aep6s.model.AnimalModel;
import com.aep6s.repository.AdocaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdocaoServiceTest {

    @Mock
    private AdocaoRepository repository;

    @Mock
    private AnimalService animalService;

    @Mock
    private AdotanteService adotanteService;

    @InjectMocks
    private AdocaoService service;

    @Test
    void deveRegistrarAdocaoLigandoAnimalEAdotante() {
        when(adotanteService.buscarPorId("10")).thenReturn(new AdotanteModel());
        when(animalService.marcarComoAdotado("1")).thenReturn(new AnimalModel());
        when(repository.save(any(AdocaoModel.class))).thenAnswer(inv -> inv.getArgument(0));

        AdocaoModel resultado = service.registrar(criarRequest("1", "10"));

        assertEquals("1", resultado.getAnimalId());
        assertEquals("10", resultado.getAdotanteId());
        assertEquals("Visita realizada", resultado.getObservacoes());
        InOrder ordem = inOrder(adotanteService, animalService, repository);
        ordem.verify(adotanteService).buscarPorId("10");
        ordem.verify(animalService).marcarComoAdotado("1");
        ordem.verify(repository).save(any(AdocaoModel.class));
    }

    @Test
    void naoDeveRegistrarAdocaoQuandoAdotanteNaoExiste() {
        when(adotanteService.buscarPorId("999")).thenThrow(new ResourceNotFoundException("Adotante não encontrado"));

        assertThrows(ResourceNotFoundException.class, () -> service.registrar(criarRequest("1", "999")));

        verify(animalService, never()).marcarComoAdotado(any());
        verify(repository, never()).save(any(AdocaoModel.class));
    }

    @Test
    void naoDeveRegistrarAdocaoQuandoAnimalJaEstaAdotado() {
        when(adotanteService.buscarPorId("10")).thenReturn(new AdotanteModel());
        when(animalService.marcarComoAdotado("1")).thenThrow(new IllegalStateException("Animal já está adotado"));

        assertThrows(IllegalStateException.class, () -> service.registrar(criarRequest("1", "10")));

        verify(repository, never()).save(any(AdocaoModel.class));
    }

    @Test
    void deveListarTodasAsAdocoes() {
        when(repository.findAll()).thenReturn(List.of(new AdocaoModel("1", "10", null)));

        assertEquals(1, service.listarTodas().size());
    }

    @Test
    void deveBuscarAdocaoPorId() {
        AdocaoModel adocao = new AdocaoModel("1", "10", null);
        when(repository.findById("a1")).thenReturn(Optional.of(adocao));

        assertSame(adocao, service.buscarPorId("a1"));
    }

    @Test
    void deveLancarExcecaoQuandoAdocaoNaoExiste() {
        when(repository.findById("x")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorId("x"));
    }

    @Test
    void deveListarAdocoesDeUmAdotante() {
        when(adotanteService.buscarPorId("10")).thenReturn(new AdotanteModel());
        when(repository.findByAdotanteId("10")).thenReturn(List.of(new AdocaoModel("1", "10", null)));

        List<AdocaoModel> resultado = service.listarPorAdotante("10");

        assertEquals(1, resultado.size());
    }

    @Test
    void deveCancelarAdocaoELiberarOAnimal() {
        AdocaoModel adocao = new AdocaoModel("1", "10", null);
        when(repository.findById("a1")).thenReturn(Optional.of(adocao));

        service.cancelar("a1");

        verify(animalService).marcarComoDisponivel("1");
        verify(repository).delete(adocao);
    }

    @Test
    void naoDeveCancelarAdocaoInexistente() {
        when(repository.findById("x")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.cancelar("x"));

        verify(animalService, never()).marcarComoDisponivel(any());
    }

    private AdocaoRequest criarRequest(String animalId, String adotanteId) {
        AdocaoRequest request = new AdocaoRequest();
        request.setAnimalId(animalId);
        request.setAdotanteId(adotanteId);
        request.setObservacoes("Visita realizada");
        return request;
    }
}
