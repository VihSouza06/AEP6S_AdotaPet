package com.aep6s.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public class VacinaRequest {

    @NotBlank(message = "O nome da vacina é obrigatório")
    private String nome;

    @NotNull(message = "A data de aplicação é obrigatória")
    @PastOrPresent(message = "A data de aplicação não pode estar no futuro")
    private LocalDate dataAplicacao;

    private String observacao;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataAplicacao() {
        return dataAplicacao;
    }

    public void setDataAplicacao(LocalDate dataAplicacao) {
        this.dataAplicacao = dataAplicacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}

