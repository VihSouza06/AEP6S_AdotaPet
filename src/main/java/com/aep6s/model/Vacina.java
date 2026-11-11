package com.aep6s.model;

import java.time.LocalDate;

//Subdocumento embutido na lista "vacinas" do documento de animal.
public class Vacina {

    private String nome;
    private LocalDate dataAplicacao;
    private String observacao;

    public Vacina() {
    }

    public Vacina(String nome, LocalDate dataAplicacao, String observacao) {
        this.nome = nome;
        this.dataAplicacao = dataAplicacao;
        this.observacao = observacao;
    }

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
