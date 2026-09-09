package com.aep6s.controller;

import com.aep6s.dto.AdocaoRequest;
import com.aep6s.dto.AnimalRequest;
import com.aep6s.enums.StatusAdocao;
import com.aep6s.exception.ResourceNotFoundException;
import com.aep6s.model.AnimalModel;
import com.aep6s.service.AnimalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnimalController.class)
public class AnimalControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AnimalService service;

    @Test
    void deveCadastrarAnimalComSucesso() throws Exception {
        AnimalRequest request = criarRequest();
        AnimalModel salvo = criarAnimal();
        when(service.cadastrar(any(AnimalRequest.class))).thenReturn(salvo);

        mockMvc.perform(post("/animais")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Thor"));
    }

    @Test
    void naoDeveCadastrarAnimalSemNome() throws Exception {
        AnimalRequest request = criarRequest();
        request.setNome("");

        mockMvc.perform(post("/animais")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveListarTodosOsAnimais() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(criarAnimal()));

        mockMvc.perform(get("/animais"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Thor"));
    }

    @Test
    void deveBuscarAnimalPorId() throws Exception {
        when(service.buscarPorId("1")).thenReturn(criarAnimal());

        mockMvc.perform(get("/animais/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Thor"));
    }

    @Test
    void deveRetornar404QuandoAnimalNaoExiste() throws Exception {
        when(service.buscarPorId("999")).thenThrow(new ResourceNotFoundException("Animal não encontrado com id: 999"));

        mockMvc.perform(get("/animais/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveListarPorStatus() throws Exception {
        when(service.listarPorStatus(StatusAdocao.DISPONIVEL)).thenReturn(List.of(criarAnimal()));

        mockMvc.perform(get("/animais/status/DISPONIVEL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("DISPONIVEL"));
    }

    @Test
    void deveAtualizarAnimal() throws Exception {
        AnimalRequest request = criarRequest();
        when(service.atualizar(eq("1"), any(AnimalRequest.class))).thenReturn(criarAnimal());

        mockMvc.perform(put("/animais/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void deveRemoverAnimal() throws Exception {
        mockMvc.perform(delete("/animais/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRegistrarAdocao() throws Exception {
        AdocaoRequest request = new AdocaoRequest();
        request.setAdotante("Maria Silva");
        AnimalModel adotado = criarAnimal();
        adotado.marcarComoAdotado("Maria Silva");
        when(service.registrarAdocao(eq("1"), eq("Maria Silva"))).thenReturn(adotado);

        mockMvc.perform(put("/animais/1/adotar")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ADOTADO"))
                .andExpect(jsonPath("$.adotante").value("Maria Silva"));
    }

    private AnimalRequest criarRequest() {
        AnimalRequest request = new AnimalRequest();
        request.setNome("Thor");
        request.setEspecie("cachorro");
        request.setRaca("SRD");
        request.setIdade(3);
        request.setPorte("medio");
        request.setSexo("macho");
        request.setDescricao("Dócil");
        return request;
    }

    private AnimalModel criarAnimal() {
        AnimalModel animal = new AnimalModel("Thor", "cachorro", "SRD", 3, "medio", "macho", "Dócil");
        animal.setId("1");
        return animal;
    }
}
