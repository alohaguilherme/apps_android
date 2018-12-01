package com.example.gteod.recyclerview.Activity.model;

public class Filme {

    private String ano;
    private String genero;
    private String tituloFilme;

    public Filme(){

    }

    public Filme(String ano, String genero, String tituloFilme) {
        this.ano = ano;
        this.genero = genero;
        this.tituloFilme = tituloFilme;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getTituloFilme() {
        return tituloFilme;
    }

    public void setTituloFilme(String tituloFilme) {
        this.tituloFilme = tituloFilme;
    }
}
