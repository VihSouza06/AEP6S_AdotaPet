package com.aep6s.service;

import com.aep6s.dto.AdotanteRequest;
import com.aep6s.dto.EnderecoRequest;
import com.aep6s.exception.ResourceNotFoundException;
import com.aep6s.model.AdotanteModel;
import com.aep6s.repository.AdocaoRepository;
import com.aep6s.repository.AdotanteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdotanteServiceTest {

    @Mock
    private AdotanteRepository repository;

    @Mock
    private AdocaoRepository adocaoRepository;

    @InjectMocks
    private AdotanteService service;

    private AdotanteModel adotanteExistente;

    @BeforeEach
    void setUp() {
        adotanteExistente = new AdotanteModel("Maria Silva", "maria@email.com", "44999990000");
        adotanteExistente.setId("10");
    }

    @Test
    void deveCadastrarAdotanteComEnderecosEmbutidos() {
        when(repository.existsByEmailIgnoreCase("joao@email.com")).thenReturn(false);
        when(repository.save(any(AdotanteModel.class))).thenAnswer(inv -> inv.getArgument(0));

        AdotanteModel resultado = service.cadastrar(criarRequest("João", "joao@email.com"));

        assertEquals("João", resultado.getNome());
        assertEquals(1, resultado.getEnderecos().size());
        assertEquals("Maringá", resultado.getEnderecos().get(0).getCidade());
        assertEquals("PR", resultado.getEnderecos().get(0).getEstado());
    }

    @Test
    void naoDeveCadastrarAdotanteComEmailDuplicado() {
        when(repository.existsByEmailIgnoreCase("maria@email.com")).thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> service.cadastrar(criarRequest("Outra Maria", "maria@email.com")));

        verify(repository, never()).save(any(AdotanteModel.class));
    }

    @Test
    void deveListarTodosOsAdotantes() {
        when(repository.findAll()).thenReturn(List.of(adotanteExistente));

        assertEquals(1, service.listarTodos().size());
    }

    @Test
    void deveBuscarAdotantePorId() {
        when(repository.findById("10")).thenReturn(Optional.of(adotanteExistente));

        assertEquals("Maria Silva", service.buscarPorId("10").getNome());
    }

    @Test
    void deveLancarExcecaoQuandoAdotanteNaoExiste() {
        when(repository.findById("999")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorId("999"));
    }

    @Test
    void deveAtualizarAdotanteMantendoOMesmoEmail() {
        when(repository.findById("10")).thenReturn(Optional.of(adotanteExistente));
        when(repository.save(any(AdotanteModel.class))).thenAnswer(inv -> inv.getArgument(0));

        AdotanteModel resultado = service.atualizar("10", criarRequest("Maria Souza", "MARIA@email.com"));

        assertEquals("Maria Souza", resultado.getNome());
        assertEquals(1, resultado.getEnderecos().size());
        verify(repository, never()).existsByEmailIgnoreCase(any());
    }

    @Test
    void deveAtualizarAdotanteComNovoEmailDisponivel() {
        when(repository.findById("10")).thenReturn(Optional.of(adotanteExistente));
        when(repository.existsByEmailIgnoreCase("novo@email.com")).thenReturn(false);
        when(repository.save(any(AdotanteModel.class))).thenAnswer(inv -> inv.getArgument(0));

        AdotanteModel resultado = service.atualizar("10", criarRequest("Maria Silva", "novo@email.com"));

        assertEquals("novo@email.com", resultado.getEmail());
    }

    @Test
    void naoDeveAtualizarParaEmailDeOutroAdotante() {
        when(repository.findById("10")).thenReturn(Optional.of(adotanteExistente));
        when(repository.existsByEmailIgnoreCase("outro@email.com")).thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> service.atualizar("10", criarRequest("Maria Silva", "outro@email.com")));

        verify(repository, never()).save(any(AdotanteModel.class));
    }

    @Test
    void deveRemoverAdotanteSemAdocoes() {
        when(repository.findById("10")).thenReturn(Optional.of(adotanteExistente));
        when(adocaoRepository.existsByAdotanteId("10")).thenReturn(false);

        service.remover("10");

        verify(repository, times(1)).delete(adotanteExistente);
    }

    @Test
    void naoDeveRemoverAdotanteComAdocoesRegistradas() {
        when(repository.findById("10")).thenReturn(Optional.of(adotanteExistente));
        when(adocaoRepository.existsByAdotanteId("10")).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> service.remover("10"));

        verify(repository, never()).delete(any(AdotanteModel.class));
    }

    private AdotanteRequest criarRequest(String nome, String email) {
        EnderecoRequest endereco = new EnderecoRequest();
        endereco.setRua("Rua A");
        endereco.setNumero("100");
        endereco.setCidade("Maringá");
        endereco.setEstado("PR");

        AdotanteRequest request = new AdotanteRequest();
        request.setNome(nome);
        request.setEmail(email);
        request.setTelefone("44999990000");
        request.setEnderecos(List.of(endereco));
        return request;
    }
}
