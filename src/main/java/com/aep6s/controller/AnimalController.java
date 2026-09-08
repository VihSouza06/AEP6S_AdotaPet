package com.aep6s.controller;

import com.aep6s.dto.AdocaoRequest;
import com.aep6s.dto.AnimalRequest;
import com.aep6s.enums.StatusAdocao;
import com.aep6s.model.AnimalModel;
import com.aep6s.service.AnimalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/animais")
public class AnimalController {

    private final AnimalService service;

    public AnimalController(AnimalService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AnimalModel> cadastrar(@Valid @RequestBody AnimalRequest request) {
        AnimalModel criado = service.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @GetMapping
    public ResponseEntity<List<AnimalModel>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalModel> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AnimalModel>> listarPorStatus(@PathVariable StatusAdocao status) {
        return ResponseEntity.ok(service.listarPorStatus(status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnimalModel> atualizar(@PathVariable String id,
                                            @Valid @RequestBody AnimalRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @PutMapping("/{id}/adotar")
    public ResponseEntity<AnimalModel> adotar(@PathVariable String id,
                                         @Valid @RequestBody AdocaoRequest request) {
        return ResponseEntity.ok(service.registrarAdocao(id, request.getAdotante()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable String id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}

