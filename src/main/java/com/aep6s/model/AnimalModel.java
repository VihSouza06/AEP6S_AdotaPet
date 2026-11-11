package com.aep6s.model;

import com.aep6s.enums.StatusAdocao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "animais")
public class AnimalModel {

    @Id
    private String id;

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

    @NotNull(message = "O status de adoção é obrigatório")
    private StatusAdocao status;

    private boolean castrado;
    private LocalDate dataCastracao;

    private LocalDate dataCadastro;

    private List<Vacina> vacinas = new ArrayList<>();

    public AnimalModel() {
        this.status = StatusAdocao.DISPONIVEL;
        this.dataCadastro = LocalDate.now();
    }

    public AnimalModel(String nome, String especie, String raca, int idade, String porte,
                  String sexo, String descricao) {
        this();
        this.nome = nome;
        this.especie = especie;
        this.raca = raca;
        this.idade = idade;
        this.porte = porte;
        this.sexo = sexo;
        this.descricao = descricao;
    }

    public void marcarComoAdotado() {
        this.status = StatusAdocao.ADOTADO;
    }

    public void marcarComoDisponivel() {
        this.status = StatusAdocao.DISPONIVEL;
    }

    public void registrarCastracao() {
        this.castrado = true;
        this.dataCastracao = LocalDate.now();
    }

    public boolean estaAdotado() {
        return this.status == StatusAdocao.ADOTADO;
    }

    public void adicionarVacina(Vacina vacina) {
        this.vacinas.add(vacina);
    }

    // Getters e setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public StatusAdocao getStatus() {
        return status;
    }

    public void setStatus(StatusAdocao status) {
        this.status = status;
    }

    public boolean isCastrado() {
        return castrado;
    }

    public void setCastrado(boolean castrado) {
        this.castrado = castrado;
    }

    public LocalDate getDataCastracao() {
        return dataCastracao;
    }

    public void setDataCastracao(LocalDate dataCastracao) {
        this.dataCastracao = dataCastracao;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public List<Vacina> getVacinas() {
        return vacinas;
    }

    public void setVacinas(List<Vacina> vacinas) {
        this.vacinas = vacinas == null ? new ArrayList<>() : vacinas;
    }
}
