package com.aep6s.service;

import com.aep6s.dto.AdotanteRequest;
import com.aep6s.dto.EnderecoRequest;
import com.aep6s.exception.ResourceNotFoundException;
import com.aep6s.model.AdotanteModel;
import com.aep6s.model.Endereco;
import com.aep6s.repository.AdocaoRepository;
import com.aep6s.repository.AdotanteRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdotanteService {

    private final AdotanteRepository repository;
    private final AdocaoRepository adocaoRepository;

    public AdotanteService(AdotanteRepository repository, AdocaoRepository adocaoRepository) {
        this.repository = repository;
        this.adocaoRepository = adocaoRepository;
    }

    public AdotanteModel cadastrar(AdotanteRequest request) {
        garantirEmailDisponivel(request.getEmail());
        AdotanteModel adotante = new AdotanteModel(
                request.getNome(),
                request.getEmail(),
                request.getTelefone()
        );
        adotante.setEnderecos(converterEnderecos(request.getEnderecos()));
        return repository.save(adotante);
    }

    public List<AdotanteModel> listarTodos() {
        return repository.findAll();
    }

    public AdotanteModel buscarPorId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Adotante não encontrado com id: " + id));
    }

    public AdotanteModel atualizar(String id, AdotanteRequest request) {
        AdotanteModel adotante = buscarPorId(id);
        if (!request.getEmail().equalsIgnoreCase(adotante.getEmail())) {
            garantirEmailDisponivel(request.getEmail());
        }
        adotante.setNome(request.getNome());
        adotante.setEmail(request.getEmail());
        adotante.setTelefone(request.getTelefone());
        adotante.setEnderecos(converterEnderecos(request.getEnderecos()));
        return repository.save(adotante);
    }

    public void remover(String id) {
        AdotanteModel adotante = buscarPorId(id);
        if (adocaoRepository.existsByAdotanteId(id)) {
            throw new IllegalStateException(
                    "Não é possível remover um adotante que possui adoções registradas");
        }
        repository.delete(adotante);
    }

    private void garantirEmailDisponivel(String email) {
        if (repository.existsByEmailIgnoreCase(email)) {
            throw new IllegalStateException("Já existe um adotante cadastrado com o e-mail: " + email);
        }
    }

    private List<Endereco> converterEnderecos(List<EnderecoRequest> requests) {
        List<Endereco> enderecos = new ArrayList<>();
        if (requests != null) {
            for (EnderecoRequest request : requests) {
                enderecos.add(new Endereco(
                        request.getRua(),
                        request.getNumero(),
                        request.getCidade(),
                        request.getEstado()
                ));
            }
        }
        return enderecos;
    }
}
