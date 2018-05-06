package br.com.narcielitonlopes.cadastropessoas.model;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class Pessoa extends SugarRecord {

    @Unique
    private Long id;
    private Integer idade;
    private String nome;
    private String sexo;

    public Pessoa(){}

    public Pessoa(Integer idade, String nome, String sexo) {
        this.idade = idade;
        this.nome = nome;
        this.sexo = sexo;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
}
