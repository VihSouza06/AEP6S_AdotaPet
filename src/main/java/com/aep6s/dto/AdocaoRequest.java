package com.aep6s.dto;

import jakarta.validation.constraints.NotBlank;

public class AdocaoRequest {

    @NotBlank(message = "O id do animal é obrigatório")
    private String animalId;

    @NotBlank(message = "O id do adotante é obrigatório")
    private String adotanteId;

    private String observacoes;

    public String getAnimalId() {
        return animalId;
    }

    public void setAnimalId(String animalId) {
        this.animalId = animalId;
    }

    public String getAdotanteId() {
        return adotanteId;
    }

    public void setAdotanteId(String adotanteId) {
        this.adotanteId = adotanteId;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
