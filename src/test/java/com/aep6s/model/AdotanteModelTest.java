package com.aep6s.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdotanteModelTest {

    @Test
    void deveCriarAdotanteComDadosBasicosEListaVaziaDeEnderecos() {
        AdotanteModel adotante = new AdotanteModel("Maria Silva", "maria@email.com", "44999990000");

        assertEquals("Maria Silva", adotante.getNome());
        assertEquals("maria@email.com", adotante.getEmail());
        assertEquals("44999990000", adotante.getTelefone());
        assertNotNull(adotante.getDataCadastro());
        assertTrue(adotante.getEnderecos().isEmpty());
    }

    @Test
    void deveAdicionarEnderecoNaListaDeSubdocumentos() {
        AdotanteModel adotante = new AdotanteModel("Maria Silva", "maria@email.com", null);

        adotante.adicionarEndereco(new Endereco("Rua A", "10", "Maringá", "PR"));
        adotante.adicionarEndereco(new Endereco("Rua B", "20", "Londrina", "PR"));

        assertEquals(2, adotante.getEnderecos().size());
        assertEquals("Maringá", adotante.getEnderecos().get(0).getCidade());
    }

    @Test
    void deveTratarListaNulaDeEnderecosComoListaVazia() {
        AdotanteModel adotante = new AdotanteModel();

        adotante.setEnderecos(null);

        assertNotNull(adotante.getEnderecos());
        assertTrue(adotante.getEnderecos().isEmpty());
    }

    @Test
    void devePermitirAlterarTodosOsCampos() {
        AdotanteModel adotante = new AdotanteModel();
        List<Endereco> enderecos = List.of(new Endereco("Rua A", "1", "Maringá", "PR"));

        adotante.setId("id-1");
        adotante.setNome("João");
        adotante.setEmail("joao@email.com");
        adotante.setTelefone("44988880000");
        adotante.setEnderecos(enderecos);
        adotante.setDataCadastro(LocalDate.of(2026, 9, 10));

        assertEquals("id-1", adotante.getId());
        assertEquals("João", adotante.getNome());
        assertEquals("joao@email.com", adotante.getEmail());
        assertEquals("44988880000", adotante.getTelefone());
        assertEquals(enderecos, adotante.getEnderecos());
        assertEquals(LocalDate.of(2026, 9, 10), adotante.getDataCadastro());
    }
}
