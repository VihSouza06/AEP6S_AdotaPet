package com.aep6s.controller;

import com.aep6s.dto.AnimalRequest;
import com.aep6s.dto.VacinaRequest;
import com.aep6s.enums.StatusAdocao;
import com.aep6s.exception.ResourceNotFoundException;
import com.aep6s.model.AnimalModel;
import com.aep6s.model.Vacina;
import com.aep6s.service.AnimalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnimalController.class)
class AnimalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AnimalService service;

    @Test
    void deveCadastrarAnimalComSucesso() throws Exception {
        when(service.cadastrar(any(AnimalRequest.class))).thenReturn(criarAnimal());

        mockMvc.perform(post("/animais")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criarRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Thor"))
                .andExpect(jsonPath("$.status").value("DISPONIVEL"));
    }

    @Test
    void naoDeveCadastrarAnimalSemNome() throws Exception {
        AnimalRequest request = criarRequest();
        request.setNome("");

        mockMvc.perform(post("/animais")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").exists());
    }

    @Test
    void naoDeveCadastrarAnimalComVacinaInvalida() throws Exception {
        AnimalRequest request = criarRequest();
        VacinaRequest vacina = criarVacinaRequest();
        vacina.setNome("");
        request.setVacinas(List.of(vacina));

        mockMvc.perform(post("/animais")
                        .contentType(MediaType.APPLICATION_JSON)
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
        when(service.buscarPorId("999"))
                .thenThrow(new ResourceNotFoundException("Animal não encontrado com id: 999"));

        mockMvc.perform(get("/animais/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Animal não encontrado com id: 999"));
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
        when(service.atualizar(eq("1"), any(AnimalRequest.class))).thenReturn(criarAnimal());

        mockMvc.perform(put("/animais/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criarRequest())))
                .andExpect(status().isOk());
    }

    @Test
    void deveAdicionarVacinaAoAnimal() throws Exception {
        AnimalModel animal = criarAnimal();
        animal.adicionarVacina(new Vacina("V10", LocalDate.now().minusDays(10), "Dose única"));
        when(service.adicionarVacina(eq("1"), any(VacinaRequest.class))).thenReturn(animal);

        mockMvc.perform(post("/animais/1/vacinas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criarVacinaRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.vacinas[0].nome").value("V10"));
    }

    @Test
    void naoDeveAdicionarVacinaComDataNoFuturo() throws Exception {
        VacinaRequest vacina = criarVacinaRequest();
        vacina.setDataAplicacao(LocalDate.now().plusDays(5));

        mockMvc.perform(post("/animais/1/vacinas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vacina)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRemoverAnimal() throws Exception {
        mockMvc.perform(delete("/animais/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar409AoRemoverAnimalAdotado() throws Exception {
        doThrow(new IllegalStateException("Não é possível remover um animal adotado"))
                .when(service).remover("1");

        mockMvc.perform(delete("/animais/1"))
                .andExpect(status().isConflict());
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

    private VacinaRequest criarVacinaRequest() {
        VacinaRequest vacina = new VacinaRequest();
        vacina.setNome("V10");
        vacina.setDataAplicacao(LocalDate.now().minusDays(10));
        vacina.setObservacao("Dose única");
        return vacina;
    }

    private AnimalModel criarAnimal() {
        AnimalModel animal = new AnimalModel("Thor", "cachorro", "SRD", 3, "medio", "macho", "Dócil");
        animal.setId("1");
        return animal;
    }

    @Test
    void deveCadastrarAnimalCastrado() throws Exception {
        AnimalModel animal = criarAnimal();
        animal.registrarCastracao();
        when(service.cadastrar(any(AnimalRequest.class))).thenReturn(animal);
        AnimalRequest request = criarRequest();
        request.setCastrado(true);

        mockMvc.perform(post("/animais")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.castrado").value(true));
    }

    @Test
    void deveListarAnimaisPorCastracao() throws Exception {
        when(service.listarPorCastrado(false)).thenReturn(List.of(criarAnimal()));

        mockMvc.perform(get("/animais/castrado/false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].castrado").value(false));
    }

    @Test
    void deveRegistrarCastracao() throws Exception {
        AnimalModel animal = criarAnimal();
        animal.registrarCastracao();
        when(service.registrarCastracao("1")).thenReturn(animal);

        mockMvc.perform(patch("/animais/1/castracao"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.castrado").value(true))
                .andExpect(jsonPath("$.dataCastracao").exists());
    }

    @Test
    void deveRetornar409AoCastrarAnimalJaCastrado() throws Exception {
        when(service.registrarCastracao("1")).thenThrow(new IllegalStateException("Animal já está castrado"));

        mockMvc.perform(patch("/animais/1/castracao"))
                .andExpect(status().isConflict());
    }
}
