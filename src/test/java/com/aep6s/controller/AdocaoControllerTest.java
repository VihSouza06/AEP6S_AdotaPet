package com.aep6s.controller;

import com.aep6s.dto.AdocaoRequest;
import com.aep6s.exception.ResourceNotFoundException;
import com.aep6s.model.AdocaoModel;
import com.aep6s.service.AdocaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdocaoController.class)
class AdocaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdocaoService service;

    @Test
    void deveRegistrarAdocao() throws Exception {
        when(service.registrar(any(AdocaoRequest.class))).thenReturn(criarAdocao());

        mockMvc.perform(post("/adocoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criarRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.animalId").value("1"))
                .andExpect(jsonPath("$.adotanteId").value("10"));
    }

    @Test
    void naoDeveRegistrarAdocaoSemIds() throws Exception {
        mockMvc.perform(post("/adocoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar404QuandoAnimalOuAdotanteNaoExistem() throws Exception {
        when(service.registrar(any(AdocaoRequest.class)))
                .thenThrow(new ResourceNotFoundException("Animal não encontrado com id: 1"));

        mockMvc.perform(post("/adocoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criarRequest())))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar409QuandoAnimalJaFoiAdotado() throws Exception {
        when(service.registrar(any(AdocaoRequest.class)))
                .thenThrow(new IllegalStateException("Animal já está adotado"));

        mockMvc.perform(post("/adocoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criarRequest())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensagem").value("Animal já está adotado"));
    }

    @Test
    void deveListarTodasAsAdocoes() throws Exception {
        when(service.listarTodas()).thenReturn(List.of(criarAdocao()));

        mockMvc.perform(get("/adocoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("a1"));
    }

    @Test
    void deveBuscarAdocaoPorId() throws Exception {
        when(service.buscarPorId("a1")).thenReturn(criarAdocao());

        mockMvc.perform(get("/adocoes/a1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.observacoes").value("Visita realizada"));
    }

    @Test
    void deveListarAdocoesPorAdotante() throws Exception {
        when(service.listarPorAdotante("10")).thenReturn(List.of(criarAdocao()));

        mockMvc.perform(get("/adocoes/adotante/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].adotanteId").value("10"));
    }

    @Test
    void deveCancelarAdocao() throws Exception {
        mockMvc.perform(delete("/adocoes/a1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar404AoCancelarAdocaoInexistente() throws Exception {
        doThrow(new ResourceNotFoundException("Adoção não encontrada com id: x"))
                .when(service).cancelar("x");

        mockMvc.perform(delete("/adocoes/x"))
                .andExpect(status().isNotFound());
    }

    private AdocaoRequest criarRequest() {
        AdocaoRequest request = new AdocaoRequest();
        request.setAnimalId("1");
        request.setAdotanteId("10");
        request.setObservacoes("Visita realizada");
        return request;
    }

    private AdocaoModel criarAdocao() {
        AdocaoModel adocao = new AdocaoModel("1", "10", "Visita realizada");
        adocao.setId("a1");
        return adocao;
    }
}
