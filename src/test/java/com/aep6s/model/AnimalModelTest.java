package com.aep6s.model;

import com.aep6s.enums.StatusAdocao;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AnimalModelTest {
    @Test
    void deveIniciarComoDisponivelAoCriar() {
        AnimalModel animal = new AnimalModel("Thor", "cachorro", "SRD", 3, "medio", "macho", "Dócil");

        assertThat(animal.getStatus()).isEqualTo(StatusAdocao.DISPONIVEL);
        assertThat(animal.getAdotante()).isNull();
        assertThat(animal.getDataCadastro()).isNotNull();
    }

    @Test
    void deveMarcarComoAdotado() {
        AnimalModel animal = new AnimalModel("Mia", "gato", "SRD", 1, "pequeno", "femea", "Brincalhona");

        animal.marcarComoAdotado("Maria Silva");

        assertThat(animal.getStatus()).isEqualTo(StatusAdocao.ADOTADO);
        assertThat(animal.getAdotante()).isEqualTo("Maria Silva");
    }

    @Test
    void deveVoltarAFicarDisponivel() {
        AnimalModel animal = new AnimalModel("Rex", "cachorro", "Labrador", 2, "grande", "macho", "Ativo");
        animal.marcarComoAdotado("João Pereira");

        animal.marcarComoDisponivel();

        assertThat(animal.getStatus()).isEqualTo(StatusAdocao.DISPONIVEL);
        assertThat(animal.getAdotante()).isNull();
    }
}
