package com.aep6s.service;

import com.aep6s.dto.AnimalRequest;
import com.aep6s.enums.StatusAdocao;
import com.aep6s.exception.ResourceNotFoundException;
import com.aep6s.model.AnimalModel;
import com.aep6s.repository.AnimalRepository;
import org.springframework.stereotype.Service;

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
        return repository.save(animal);
    }

    public void remover(String id) {
        AnimalModel animal = buscarPorId(id);
        repository.delete(animal);
    }

    public AnimalModel registrarAdocao(String id, String adotante) {
        AnimalModel animal = buscarPorId(id);
        if (animal.getStatus() == StatusAdocao.ADOTADO) {
            throw new IllegalStateException("Animal já está adotado");
        }
        animal.marcarComoAdotado(adotante);
        return repository.save(animal);
    }
}

