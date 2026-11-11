package com.aep6s.service;

import com.aep6s.dto.AnimalRequest;
import com.aep6s.dto.VacinaRequest;
import com.aep6s.enums.StatusAdocao;
import com.aep6s.exception.ResourceNotFoundException;
import com.aep6s.model.AnimalModel;
import com.aep6s.model.Vacina;
import com.aep6s.repository.AnimalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnimalService {

    private final AnimalRepository repository;

    public AnimalService(AnimalRepository repository) {
        this.repository = repository;
    }

    public AnimalModel cadastrar(AnimalRequest request) {
        AnimalModel animal = new AnimalModel(
                request.getNome(),
                request.getEspecie(),
                request.getRaca(),
                request.getIdade(),
                request.getPorte(),
                request.getSexo(),
                request.getDescricao()
        );
        animal.setCastrado(Boolean.TRUE.equals(request.getCastrado()));
        if (animal.isCastrado()) {
            animal.setDataCastracao(LocalDate.now());
        }
        animal.setVacinas(converterVacinas(request.getVacinas()));
        return repository.save(animal);
    }

    public List<AnimalModel> listarTodos() {
        return repository.findAll();
    }

    public AnimalModel buscarPorId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Animal não encontrado com id: " + id));
    }

    public List<AnimalModel> listarPorStatus(StatusAdocao status) {
        return repository.findByStatus(status);
    }

    public AnimalModel atualizar(String id, AnimalRequest request) {
        AnimalModel animal = buscarPorId(id);
        animal.setNome(request.getNome());
        animal.setEspecie(request.getEspecie());
        animal.setRaca(request.getRaca());
        animal.setIdade(request.getIdade());
        animal.setPorte(request.getPorte());
        animal.setSexo(request.getSexo());
        animal.setDescricao(request.getDescricao());
        if (request.getCastrado() != null && request.getCastrado() && !animal.isCastrado()) {
            animal.registrarCastracao();
        }
        if (request.getVacinas() != null) {
            animal.setVacinas(converterVacinas(request.getVacinas()));
        }
        return repository.save(animal);
    }

    public List<AnimalModel> listarPorCastrado(boolean castrado) {
        return repository.findByCastrado(castrado);
    }

    public AnimalModel registrarCastracao(String id) {
        AnimalModel animal = buscarPorId(id);
        if (animal.isCastrado()) {
            throw new IllegalStateException("Animal já está castrado");
        }
        animal.registrarCastracao();
        return repository.save(animal);
    }

    public void remover(String id) {
        AnimalModel animal = buscarPorId(id);
        if (animal.estaAdotado()) {
            throw new IllegalStateException(
                    "Não é possível remover um animal adotado. Cancele a adoção primeiro");
        }
        repository.delete(animal);
    }

    public AnimalModel adicionarVacina(String id, VacinaRequest request) {
        AnimalModel animal = buscarPorId(id);
        animal.adicionarVacina(converterVacina(request));
        return repository.save(animal);
    }

    public AnimalModel marcarComoAdotado(String id) {
        AnimalModel animal = buscarPorId(id);
        if (animal.estaAdotado()) {
            throw new IllegalStateException("Animal já está adotado");
        }
        animal.marcarComoAdotado();
        return repository.save(animal);
    }

    public AnimalModel marcarComoDisponivel(String id) {
        AnimalModel animal = buscarPorId(id);
        animal.marcarComoDisponivel();
        return repository.save(animal);
    }

    private List<Vacina> converterVacinas(List<VacinaRequest> requests) {
        List<Vacina> vacinas = new ArrayList<>();
        if (requests != null) {
            for (VacinaRequest request : requests) {
                vacinas.add(converterVacina(request));
            }
        }
        return vacinas;
    }

    private Vacina converterVacina(VacinaRequest request) {
        return new Vacina(request.getNome(), request.getDataAplicacao(), request.getObservacao());
    }
}
