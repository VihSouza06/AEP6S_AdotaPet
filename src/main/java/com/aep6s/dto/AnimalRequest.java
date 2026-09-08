package com.aep6s.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public class AnimalRequest {

    @NotBlank(message = "O nome do animal é obrigatório")
    private String nome;

    @NotBlank(message = "A espécie é obrigatória")
    private String especie;

    private String raca;

    @PositiveOrZero(message = "A idade não pode ser negativa")
    private int idade;

    @NotBlank(message = "O porte é obrigatório")
    private String porte;

    @NotBlank(message = "O sexo é obrigatório")
    private String sexo;

    private String descricao;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getPorte() {
        return porte;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}

