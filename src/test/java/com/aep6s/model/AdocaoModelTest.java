package com.aep6s.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AdocaoModelTest {

    @Test
    void deveCriarAdocaoReferenciandoAnimalEAdotante() {
        AdocaoModel adocao = new AdocaoModel("animal-1", "adotante-1", "Visita feita");

        assertEquals("animal-1", adocao.getAnimalId());
        assertEquals("adotante-1", adocao.getAdotanteId());
        assertEquals("Visita feita", adocao.getObservacoes());
        assertEquals(LocalDate.now(), adocao.getDataAdocao());
    }

    @Test
    void devePermitirAlterarTodosOsCampos() {
        AdocaoModel adocao = new AdocaoModel();

        adocao.setId("adocao-1");
        adocao.setAnimalId("animal-2");
        adocao.setAdotanteId("adotante-2");
        adocao.setObservacoes("Observação");
        adocao.setDataAdocao(LocalDate.of(2026, 10, 1));

        assertEquals("adocao-1", adocao.getId());
        assertEquals("animal-2", adocao.getAnimalId());
        assertEquals("adotante-2", adocao.getAdotanteId());
        assertEquals("Observação", adocao.getObservacoes());
        assertEquals(LocalDate.of(2026, 10, 1), adocao.getDataAdocao());
    }
}
