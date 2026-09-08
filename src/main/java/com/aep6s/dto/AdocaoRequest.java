package com.aep6s.dto;

import jakarta.validation.constraints.NotBlank;

public class AdocaoRequest {

    @NotBlank(message = "O nome do adotante é obrigatório")
    private String adotante;

    public String getAdotante() {
        return adotante;
    }

    public void setAdotante(String adotante) {
        this.adotante = adotante;
    }
}

