package com.aep6s.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubdocumentosTest {

    @Test
    void vacinaDevePermitirLerEAlterarOsCampos() {
        Vacina vacina = new Vacina();

        vacina.setNome("V10");
        vacina.setDataAplicacao(LocalDate.of(2026, 3, 15));
        vacina.setObservacao("Reforço em 21 dias");

        assertEquals("V10", vacina.getNome());
        assertEquals(LocalDate.of(2026, 3, 15), vacina.getDataAplicacao());
        assertEquals("Reforço em 21 dias", vacina.getObservacao());
    }

    @Test
    void enderecoDevePermitirLerEAlterarOsCampos() {
        Endereco endereco = new Endereco();

        endereco.setRua("Rua A");
        endereco.setNumero("100");
        endereco.setCidade("Maringá");
        endereco.setEstado("PR");

        assertEquals("Rua A", endereco.getRua());
        assertEquals("100", endereco.getNumero());
        assertEquals("Maringá", endereco.getCidade());
        assertEquals("PR", endereco.getEstado());
    }
}
