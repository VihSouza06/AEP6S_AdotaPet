package com.aep6s.service;

import com.aep6s.dto.AdocaoRequest;
import com.aep6s.exception.ResourceNotFoundException;
import com.aep6s.model.AdocaoModel;
import com.aep6s.repository.AdocaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Regras do relacionamento entre as coleções "animais" e "adotantes".
@Service
public class AdocaoService {

    private final AdocaoRepository repository;
    private final AnimalService animalService;
    private final AdotanteService adotanteService;

    public AdocaoService(AdocaoRepository repository,
                         AnimalService animalService,
                         AdotanteService adotanteService) {
        this.repository = repository;
        this.animalService = animalService;
        this.adotanteService = adotanteService;
    }

    public AdocaoModel registrar(AdocaoRequest request) {
        // Lança 404 se o adotante ou o animal não existirem e 409 se o animal já estiver adotado
        adotanteService.buscarPorId(request.getAdotanteId());
        animalService.marcarComoAdotado(request.getAnimalId());

        AdocaoModel adocao = new AdocaoModel(
                request.getAnimalId(),
                request.getAdotanteId(),
                request.getObservacoes()
        );
        return repository.save(adocao);
    }

    public List<AdocaoModel> listarTodas() {
        return repository.findAll();
    }

    public AdocaoModel buscarPorId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Adoção não encontrada com id: " + id));
    }

    public List<AdocaoModel> listarPorAdotante(String adotanteId) {
        adotanteService.buscarPorId(adotanteId);
        return repository.findByAdotanteId(adotanteId);
    }

    public void cancelar(String id) {
        AdocaoModel adocao = buscarPorId(id);
        animalService.marcarComoDisponivel(adocao.getAnimalId());
        repository.delete(adocao);
    }
}
