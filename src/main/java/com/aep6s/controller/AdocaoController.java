package com.aep6s.controller;

import com.aep6s.dto.AdocaoRequest;
import com.aep6s.model.AdocaoModel;
import com.aep6s.service.AdocaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adocoes")
public class AdocaoController {

    private final AdocaoService service;

    public AdocaoController(AdocaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AdocaoModel> registrar(@Valid @RequestBody AdocaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(request));
    }

    @GetMapping
    public ResponseEntity<List<AdocaoModel>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdocaoModel> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/adotante/{adotanteId}")
    public ResponseEntity<List<AdocaoModel>> listarPorAdotante(@PathVariable String adotanteId) {
        return ResponseEntity.ok(service.listarPorAdotante(adotanteId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable String id) {
        service.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}
