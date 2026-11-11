package com.aep6s.model;

import com.aep6s.enums.StatusAdocao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnimalModelTest {

    private AnimalModel criarAnimal() {
        return new AnimalModel("Thor", "cachorro", "SRD", 3, "medio", "macho", "Dócil");
    }

    @Test
    @DisplayName("Animal novo começa DISPONIVEL, sem vacinas e com data de cadastro")
    void deveIniciarComoDisponivelAoCriar() {
        AnimalModel animal = criarAnimal();

        assertEquals(StatusAdocao.DISPONIVEL, animal.getStatus());
        assertFalse(animal.estaAdotado());
        assertNotNull(animal.getDataCadastro());
        assertNotNull(animal.getVacinas());
        assertTrue(animal.getVacinas().isEmpty());
    }

    @Test
    void deveGuardarOsDadosInformadosNoConstrutor() {
        AnimalModel animal = criarAnimal();

        assertEquals("Thor", animal.getNome());
        assertEquals("cachorro", animal.getEspecie());
        assertEquals("SRD", animal.getRaca());
        assertEquals(3, animal.getIdade());
        assertEquals("medio", animal.getPorte());
        assertEquals("macho", animal.getSexo());
        assertEquals("Dócil", animal.getDescricao());
    }

    @Test
    void deveMarcarComoAdotado() {
        AnimalModel animal = criarAnimal();

        animal.marcarComoAdotado();

        assertEquals(StatusAdocao.ADOTADO, animal.getStatus());
        assertTrue(animal.estaAdotado());
    }

    @Test
    void deveVoltarAFicarDisponivel() {
        AnimalModel animal = criarAnimal();
        animal.marcarComoAdotado();

        animal.marcarComoDisponivel();

        assertEquals(StatusAdocao.DISPONIVEL, animal.getStatus());
        assertFalse(animal.estaAdotado());
    }

    @Test
    void deveAdicionarVacinaNaListaDeSubdocumentos() {
        AnimalModel animal = criarAnimal();

        animal.adicionarVacina(new Vacina("V10", LocalDate.of(2026, 5, 10), "Primeira dose"));

        assertEquals(1, animal.getVacinas().size());
        assertEquals("V10", animal.getVacinas().get(0).getNome());
    }

    @Test
    void deveTratarListaNulaDeVacinasComoListaVazia() {
        AnimalModel animal = criarAnimal();

        animal.setVacinas(null);

        assertNotNull(animal.getVacinas());
        assertTrue(animal.getVacinas().isEmpty());
    }

    @Test
    void devePermitirAlterarTodosOsCampos() {
        AnimalModel animal = new AnimalModel();
        List<Vacina> vacinas = List.of(new Vacina("Antirrábica", LocalDate.of(2026, 1, 1), null));

        animal.setId("abc");
        animal.setNome("Mia");
        animal.setEspecie("gato");
        animal.setRaca("Siamês");
        animal.setIdade(1);
        animal.setPorte("pequeno");
        animal.setSexo("femea");
        animal.setDescricao("Brincalhona");
        animal.setStatus(StatusAdocao.ADOTADO);
        animal.setDataCadastro(LocalDate.of(2026, 9, 1));
        animal.setVacinas(vacinas);

        assertEquals("abc", animal.getId());
        assertEquals("Mia", animal.getNome());
        assertEquals("gato", animal.getEspecie());
        assertEquals("Siamês", animal.getRaca());
        assertEquals(1, animal.getIdade());
        assertEquals("pequeno", animal.getPorte());
        assertEquals("femea", animal.getSexo());
        assertEquals("Brincalhona", animal.getDescricao());
        assertEquals(StatusAdocao.ADOTADO, animal.getStatus());
        assertEquals(LocalDate.of(2026, 9, 1), animal.getDataCadastro());
        assertEquals(vacinas, animal.getVacinas());
    }

    @Test
    void animalNovoNaoDeveEstarCastrado() {
        AnimalModel animal = criarAnimal();

        assertFalse(animal.isCastrado());
        assertNull(animal.getDataCastracao());
    }

    @Test
    void deveRegistrarCastracaoComData() {
        AnimalModel animal = criarAnimal();

        animal.registrarCastracao();

        assertTrue(animal.isCastrado());
        assertEquals(LocalDate.now(), animal.getDataCastracao());
    }

    @Test
    void devePermitirAlterarCamposDeCastracao() {
        AnimalModel animal = criarAnimal();

        animal.setCastrado(true);
        animal.setDataCastracao(LocalDate.of(2026, 6, 1));

        assertTrue(animal.isCastrado());
        assertEquals(LocalDate.of(2026, 6, 1), animal.getDataCastracao());
    }
}
