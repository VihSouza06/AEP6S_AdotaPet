package com.aep6s.service;

import com.aep6s.dto.AnimalRequest;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnimalServiceTest {
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
    void deveCadastrarAnimal() {
        AnimalRequest request = criarRequest();
        when(repository.save(any(AnimalModel.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        AnimalModel resultado = service.cadastrar(request);

        assertThat(resultado.getNome()).isEqualTo("Mia");
        assertThat(resultado.getStatus()).isEqualTo(StatusAdocao.DISPONIVEL);
        verify(repository, times(1)).save(any(AnimalModel.class));
    }

    @Test
    void deveListarTodosOsAnimais() {
        when(repository.findAll()).thenReturn(List.of(animalExistente));

        List<AnimalModel> resultado = service.listarTodos();

        assertThat(resultado).hasSize(1);
    }

    @Test
    void deveBuscarPorIdComSucesso() {
        when(repository.findById("1")).thenReturn(Optional.of(animalExistente));

        AnimalModel resultado = service.buscarPorId("1");

        assertThat(resultado.getNome()).isEqualTo("Thor");
    }

    @Test
    void deveLancarExcecaoQuandoIdNaoExiste() {
        when(repository.findById("999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId("999"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void deveListarPorStatus() {
        when(repository.findByStatus(StatusAdocao.DISPONIVEL)).thenReturn(List.of(animalExistente));

        List<AnimalModel> resultado = service.listarPorStatus(StatusAdocao.DISPONIVEL);

        assertThat(resultado).hasSize(1);
    }

    @Test
    void deveAtualizarAnimal() {
        AnimalRequest request = criarRequest();
        when(repository.findById("1")).thenReturn(Optional.of(animalExistente));
        when(repository.save(any(AnimalModel.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        AnimalModel resultado = service.atualizar("1", request);

        assertThat(resultado.getNome()).isEqualTo("Mia");
        assertThat(resultado.getPorte()).isEqualTo("pequeno");
    }

    @Test
    void deveRemoverAnimal() {
        when(repository.findById("1")).thenReturn(Optional.of(animalExistente));
        doNothing().when(repository).delete(animalExistente);

        service.remover("1");

        verify(repository, times(1)).delete(animalExistente);
    }

    @Test
    void deveRegistrarAdocaoComSucesso() {
        when(repository.findById("1")).thenReturn(Optional.of(animalExistente));
        when(repository.save(any(AnimalModel.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        AnimalModel resultado = service.registrarAdocao("1", "Maria Silva");

        assertThat(resultado.getStatus()).isEqualTo(StatusAdocao.ADOTADO);
        assertThat(resultado.getAdotante()).isEqualTo("Maria Silva");
    }

    @Test
    void naoDeveAdotarAnimalJaAdotado() {
        animalExistente.marcarComoAdotado("João Pereira");
        when(repository.findById("1")).thenReturn(Optional.of(animalExistente));

        assertThatThrownBy(() -> service.registrarAdocao("1", "Outra Pessoa"))
                .isInstanceOf(IllegalStateException.class);
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
}
