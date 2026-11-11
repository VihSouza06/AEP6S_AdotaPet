package com.aep6s.controller;

import com.aep6s.dto.AdotanteRequest;
import com.aep6s.model.AdotanteModel;
import com.aep6s.service.AdotanteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adotantes")
public class AdotanteController {

    private final AdotanteService service;

    public AdotanteController(AdotanteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AdotanteModel> cadastrar(@Valid @RequestBody AdotanteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(request));
    }

    @GetMapping
    public ResponseEntity<List<AdotanteModel>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdotanteModel> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdotanteModel> atualizar(@PathVariable String id,
                                                   @Valid @RequestBody AdotanteRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable String id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
