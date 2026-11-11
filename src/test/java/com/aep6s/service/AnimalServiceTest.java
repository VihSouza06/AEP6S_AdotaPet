package com.aep6s.service;

import com.aep6s.dto.AnimalRequest;
import com.aep6s.dto.VacinaRequest;
import com.aep6s.enums.StatusAdocao;
import com.aep6s.exception.ResourceNotFoundException;
import com.aep6s.model.AnimalModel;
import com.aep6s.repository.AnimalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {

    @Mock
    private AnimalRepository repository;

    @InjectMocks
    private AnimalService service;

    private AnimalModel animalExistente;

    @BeforeEach
    void setUp() {
        animalExistente = new AnimalModel("Thor", "cachorro", "SRD", 3, "medio", "macho", "Dócil");
        animalExistente.setId("1");
    }

    @Test
    void deveCadastrarAnimalSemVacinas() {
        when(repository.save(any(AnimalModel.class))).thenAnswer(inv -> inv.getArgument(0));

        AnimalModel resultado = service.cadastrar(criarRequest());

        assertEquals("Mia", resultado.getNome());
        assertEquals(StatusAdocao.DISPONIVEL, resultado.getStatus());
        assertTrue(resultado.getVacinas().isEmpty());
        verify(repository, times(1)).save(any(AnimalModel.class));
    }

    @Test
    void deveCadastrarAnimalComVacinasEmbutidas() {
        when(repository.save(any(AnimalModel.class))).thenAnswer(inv -> inv.getArgument(0));
        AnimalRequest request = criarRequest();
        request.setVacinas(List.of(criarVacinaRequest("V10")));

        AnimalModel resultado = service.cadastrar(request);

        assertEquals(1, resultado.getVacinas().size());
        assertEquals("V10", resultado.getVacinas().get(0).getNome());
        assertEquals(LocalDate.of(2026, 5, 10), resultado.getVacinas().get(0).getDataAplicacao());
    }

    @Test
    void deveListarTodosOsAnimais() {
        when(repository.findAll()).thenReturn(List.of(animalExistente));

        assertEquals(1, service.listarTodos().size());
    }

    @Test
    void deveBuscarPorIdComSucesso() {
        when(repository.findById("1")).thenReturn(Optional.of(animalExistente));

        assertEquals("Thor", service.buscarPorId("1").getNome());
    }

    @Test
    void deveLancarExcecaoQuandoIdNaoExiste() {
        when(repository.findById("999")).thenReturn(Optional.empty());

        ResourceNotFoundException erro =
                assertThrows(ResourceNotFoundException.class, () -> service.buscarPorId("999"));

        assertTrue(erro.getMessage().contains("999"));
    }

    @Test
    void deveListarPorStatus() {
        when(repository.findByStatus(StatusAdocao.DISPONIVEL)).thenReturn(List.of(animalExistente));

        assertEquals(1, service.listarPorStatus(StatusAdocao.DISPONIVEL).size());
    }

    @Test
    void deveAtualizarAnimalMantendoVacinasQuandoNaoInformadas() {
        animalExistente.adicionarVacina(new com.aep6s.model.Vacina("Antirrábica", LocalDate.of(2026, 1, 1), null));
        when(repository.findById("1")).thenReturn(Optional.of(animalExistente));
        when(repository.save(any(AnimalModel.class))).thenAnswer(inv -> inv.getArgument(0));

        AnimalModel resultado = service.atualizar("1", criarRequest());

        assertEquals("Mia", resultado.getNome());
        assertEquals("pequeno", resultado.getPorte());
        assertEquals(1, resultado.getVacinas().size());
    }

    @Test
    void deveSubstituirVacinasQuandoInformadasNaAtualizacao() {
        animalExistente.adicionarVacina(new com.aep6s.model.Vacina("Antirrábica", LocalDate.of(2026, 1, 1), null));
        when(repository.findById("1")).thenReturn(Optional.of(animalExistente));
        when(repository.save(any(AnimalModel.class))).thenAnswer(inv -> inv.getArgument(0));
        AnimalRequest request = criarRequest();
        request.setVacinas(List.of(criarVacinaRequest("V10"), criarVacinaRequest("Gripe")));

        AnimalModel resultado = service.atualizar("1", request);

        assertEquals(2, resultado.getVacinas().size());
        assertEquals("V10", resultado.getVacinas().get(0).getNome());
    }

    @Test
    void deveAdicionarVacinaAUmAnimalExistente() {
        when(repository.findById("1")).thenReturn(Optional.of(animalExistente));
        when(repository.save(any(AnimalModel.class))).thenAnswer(inv -> inv.getArgument(0));

        AnimalModel resultado = service.adicionarVacina("1", criarVacinaRequest("V10"));

        assertEquals(1, resultado.getVacinas().size());
        assertEquals("V10", resultado.getVacinas().get(0).getNome());
    }

    @Test
    void deveRemoverAnimalDisponivel() {
        when(repository.findById("1")).thenReturn(Optional.of(animalExistente));

        service.remover("1");

        verify(repository, times(1)).delete(animalExistente);
    }

    @Test
    void naoDeveRemoverAnimalAdotado() {
        animalExistente.marcarComoAdotado();
        when(repository.findById("1")).thenReturn(Optional.of(animalExistente));

        assertThrows(IllegalStateException.class, () -> service.remover("1"));

        verify(repository, never()).delete(any(AnimalModel.class));
    }

    @Test
    void deveMarcarAnimalComoAdotado() {
        when(repository.findById("1")).thenReturn(Optional.of(animalExistente));
        when(repository.save(any(AnimalModel.class))).thenAnswer(inv -> inv.getArgument(0));

        AnimalModel resultado = service.marcarComoAdotado("1");

        assertEquals(StatusAdocao.ADOTADO, resultado.getStatus());
    }

    @Test
    void naoDeveAdotarAnimalJaAdotado() {
        animalExistente.marcarComoAdotado();
        when(repository.findById("1")).thenReturn(Optional.of(animalExistente));

        assertThrows(IllegalStateException.class, () -> service.marcarComoAdotado("1"));

        verify(repository, never()).save(any(AnimalModel.class));
    }

    @Test
    void deveMarcarAnimalComoDisponivelNovamente() {
        animalExistente.marcarComoAdotado();
        when(repository.findById("1")).thenReturn(Optional.of(animalExistente));
        when(repository.save(any(AnimalModel.class))).thenAnswer(inv -> inv.getArgument(0));

        AnimalModel resultado = service.marcarComoDisponivel("1");

        assertEquals(StatusAdocao.DISPONIVEL, resultado.getStatus());
    }

    private AnimalRequest criarRequest() {
        AnimalRequest request = new AnimalRequest();
        request.setNome("Mia");
        request.setEspecie("gato");
        request.setRaca("SRD");
        request.setIdade(1);
        request.setPorte("pequeno");
        request.setSexo("femea");
        request.setDescricao("Brincalhona");
        return request;
    }

    private VacinaRequest criarVacinaRequest(String nome) {
        VacinaRequest vacina = new VacinaRequest();
        vacina.setNome(nome);
        vacina.setDataAplicacao(LocalDate.of(2026, 5, 10));
        vacina.setObservacao("Dose única");
        return vacina;
    }

    @Test
    void deveCadastrarAnimalJaCastrado() {
        when(repository.save(any(AnimalModel.class))).thenAnswer(inv -> inv.getArgument(0));
        AnimalRequest request = criarRequest();
        request.setCastrado(true);

        AnimalModel resultado = service.cadastrar(request);

        assertTrue(resultado.isCastrado());
        assertEquals(LocalDate.now(), resultado.getDataCastracao());
    }

    @Test
    void deveCadastrarAnimalNaoCastradoQuandoCampoNaoInformado() {
        when(repository.save(any(AnimalModel.class))).thenAnswer(inv -> inv.getArgument(0));

        AnimalModel resultado = service.cadastrar(criarRequest());

        assertFalse(resultado.isCastrado());
        assertNull(resultado.getDataCastracao());
    }

    @Test
    void deveRegistrarCastracaoDeAnimalNaoCastrado() {
        when(repository.findById("1")).thenReturn(Optional.of(animalExistente));
        when(repository.save(any(AnimalModel.class))).thenAnswer(inv -> inv.getArgument(0));

        AnimalModel resultado = service.registrarCastracao("1");

        assertTrue(resultado.isCastrado());
        assertEquals(LocalDate.now(), resultado.getDataCastracao());
    }

    @Test
    void naoDeveRegistrarCastracaoDeAnimalJaCastrado() {
        animalExistente.registrarCastracao();
        when(repository.findById("1")).thenReturn(Optional.of(animalExistente));

        assertThrows(IllegalStateException.class, () -> service.registrarCastracao("1"));

        verify(repository, never()).save(any(AnimalModel.class));
    }

    @Test
    void deveListarAnimaisPorCastracao() {
        when(repository.findByCastrado(true)).thenReturn(List.of(animalExistente));

        assertEquals(1, service.listarPorCastrado(true).size());
    }

    @Test
    void atualizacaoSemCampoCastradoDeveManterOStatusDeCastracao() {
        animalExistente.registrarCastracao();
        when(repository.findById("1")).thenReturn(Optional.of(animalExistente));
        when(repository.save(any(AnimalModel.class))).thenAnswer(inv -> inv.getArgument(0));

        AnimalModel resultado = service.atualizar("1", criarRequest());

        assertTrue(resultado.isCastrado());
    }

    @Test
    void atualizacaoComCastradoVerdadeiroDeveRegistrarCastracao() {
        when(repository.findById("1")).thenReturn(Optional.of(animalExistente));
        when(repository.save(any(AnimalModel.class))).thenAnswer(inv -> inv.getArgument(0));
        AnimalRequest request = criarRequest();
        request.setCastrado(true);

        AnimalModel resultado = service.atualizar("1", request);

        assertTrue(resultado.isCastrado());
        assertNotNull(resultado.getDataCastracao());
    }
}
