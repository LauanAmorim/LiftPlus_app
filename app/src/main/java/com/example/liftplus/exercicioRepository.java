package com.example.liftplus;

public class exercicioRepository{

    private String id;
    private String nome;
    private String grupoMuscular;

    private final String idStart = "Id: ";
    private final String nomeStart = "Nome: ";
    private final String  grupoMuscularStart = "Grupo muscular: ";


    public exercicioRepository(String id, String nome, String grupoMuscular) {
        this.id = id;
        this.nome = nome;
        this.grupoMuscular = grupoMuscular;
    }

    public String getId() {
        return this.id + idStart;
    }

    public String getNome() {
        return this.nome + nomeStart;
    }

    public String getGrupoMuscular() {
        return this.grupoMuscular + grupoMuscular;
    }
}