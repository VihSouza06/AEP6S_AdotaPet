package com.aep6s.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

public class AdotanteRequest {

    @NotBlank(message = "O nome do adotante é obrigatório")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    private String telefone;

    @Valid
    @NotEmpty(message = "Informe ao menos um endereço")
    private List<EnderecoRequest> enderecos = new ArrayList<>();

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public List<EnderecoRequest> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<EnderecoRequest> enderecos) {
        this.enderecos = enderecos;
    }
}
