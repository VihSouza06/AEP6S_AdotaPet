package com.aep6s.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "adocoes")
public class AdocaoModel {

    @Id
    private String id;

    private String animalId;
    private String adotanteId;
    private LocalDate dataAdocao;
    private String observacoes;

    public AdocaoModel() {
        this.dataAdocao = LocalDate.now();
    }

    public AdocaoModel(String animalId, String adotanteId, String observacoes) {
        this();
        this.animalId = animalId;
        this.adotanteId = adotanteId;
        this.observacoes = observacoes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public LocalDate getDataAdocao() {
        return dataAdocao;
    }

    public void setDataAdocao(LocalDate dataAdocao) {
        this.dataAdocao = dataAdocao;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
