package com.aep6s.controller;

import com.aep6s.dto.AdotanteRequest;
import com.aep6s.dto.EnderecoRequest;
import com.aep6s.exception.ResourceNotFoundException;
import com.aep6s.model.AdotanteModel;
import com.aep6s.model.Endereco;
import com.aep6s.service.AdotanteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdotanteController.class)
class AdotanteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdotanteService service;

    @Test
    void deveCadastrarAdotanteComEnderecos() throws Exception {
        when(service.cadastrar(any(AdotanteRequest.class))).thenReturn(criarAdotante());

        mockMvc.perform(post("/adotantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criarRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Maria Silva"))
                .andExpect(jsonPath("$.enderecos[0].cidade").value("Maringá"));
    }

    @Test
    void naoDeveCadastrarAdotanteComEmailInvalido() throws Exception {
        AdotanteRequest request = criarRequest();
        request.setEmail("email-invalido");

        mockMvc.perform(post("/adotantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void naoDeveCadastrarAdotanteSemEndereco() throws Exception {
        AdotanteRequest request = criarRequest();
        request.setEnderecos(List.of());

        mockMvc.perform(post("/adotantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void naoDeveCadastrarAdotanteComEnderecoInvalido() throws Exception {
        AdotanteRequest request = criarRequest();
        request.getEnderecos().get(0).setEstado("Paraná");

        mockMvc.perform(post("/adotantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar409QuandoEmailJaCadastrado() throws Exception {
        when(service.cadastrar(any(AdotanteRequest.class)))
                .thenThrow(new IllegalStateException("Já existe um adotante cadastrado com o e-mail"));

        mockMvc.perform(post("/adotantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criarRequest())))
                .andExpect(status().isConflict());
    }

    @Test
    void deveListarTodosOsAdotantes() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(criarAdotante()));

        mockMvc.perform(get("/adotantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("maria@email.com"));
    }

    @Test
    void deveBuscarAdotantePorId() throws Exception {
        when(service.buscarPorId("10")).thenReturn(criarAdotante());

        mockMvc.perform(get("/adotantes/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("10"));
    }

    @Test
    void deveRetornar404QuandoAdotanteNaoExiste() throws Exception {
        when(service.buscarPorId("999"))
                .thenThrow(new ResourceNotFoundException("Adotante não encontrado com id: 999"));

        mockMvc.perform(get("/adotantes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveAtualizarAdotante() throws Exception {
        when(service.atualizar(eq("10"), any(AdotanteRequest.class))).thenReturn(criarAdotante());

        mockMvc.perform(put("/adotantes/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criarRequest())))
                .andExpect(status().isOk());
    }

    @Test
    void deveRemoverAdotante() throws Exception {
        mockMvc.perform(delete("/adotantes/10"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar409AoRemoverAdotanteComAdocoes() throws Exception {
        doThrow(new IllegalStateException("Adotante possui adoções registradas"))
                .when(service).remover("10");

        mockMvc.perform(delete("/adotantes/10"))
                .andExpect(status().isConflict());
    }

    private AdotanteRequest criarRequest() {
        EnderecoRequest endereco = new EnderecoRequest();
        endereco.setRua("Rua A");
        endereco.setNumero("100");
        endereco.setCidade("Maringá");
        endereco.setEstado("PR");

        AdotanteRequest request = new AdotanteRequest();
        request.setNome("Maria Silva");
        request.setEmail("maria@email.com");
        request.setTelefone("44999990000");
        request.setEnderecos(List.of(endereco));
        return request;
    }

    private AdotanteModel criarAdotante() {
        AdotanteModel adotante = new AdotanteModel("Maria Silva", "maria@email.com", "44999990000");
        adotante.setId("10");
        adotante.adicionarEndereco(new Endereco("Rua A", "100", "Maringá", "PR"));
        return adotante;
    }
}
