package br.com.testes.ecofacil.ObjetoTransferencia;

import java.io.Serializable;

/**
 * Created by samue on 13/03/2018.
 */

public class ContribuinteEndereco implements Serializable {

    private int idContribuinteEndereco;
    private int fkContribuinte;
    private String nomeEndereco;
    private String numeroEndereco;
    private String bairroEndereco;
    private String cidadeEndereco;
    private String estadoEndereco;
    private String cepEndereco;
    private String complementoEndereco;
    
    public ContribuinteEndereco() {
    }

    public ContribuinteEndereco(int fkContribuinte, String nomeEndereco, String numeroEndereco, String bairroEndereco, String cidadeEndereco, String estadoEndereco, String cepEndereco, String complementoEndereco) {
        this.fkContribuinte = fkContribuinte;
        this.nomeEndereco = nomeEndereco;
        this.numeroEndereco = numeroEndereco;
        this.bairroEndereco = bairroEndereco;
        this.cidadeEndereco = cidadeEndereco;
        this.estadoEndereco = estadoEndereco;
        this.cepEndereco = cepEndereco;
        this.complementoEndereco = complementoEndereco;
    }

    @Override
    public String toString() {
        return nomeEndereco + "," + numeroEndereco + " - " + bairroEndereco + ", " + cidadeEndereco + " - " + estadoEndereco + ", " + cepEndereco;
    }

    public int getidContribuinteEndereco() {
        return idContribuinteEndereco;
    }

    public void setidContribuinteEndereco(int idContribuinteEndereco) {
        this.idContribuinteEndereco = idContribuinteEndereco;
    }

    public int getfkContribuinte() {
        return fkContribuinte;
    }

    public void setfkContribuinte(int fkContribuinte) {
        this.fkContribuinte = fkContribuinte;
    }

    public String getNomeEndereco() {
        return nomeEndereco;
    }

    public void setNomeEndereco(String nomeEndereco) {
        this.nomeEndereco = nomeEndereco;
    }

    public String getNumeroEndereco() {
        return numeroEndereco;
    }

    public void setNumeroEndereco(String numeroEndereco) {
        this.numeroEndereco = numeroEndereco;
    }

    public String getBairroEndereco() {
        return bairroEndereco;
    }

    public void setBairroEndereco(String bairroEndereco) {
        this.bairroEndereco = bairroEndereco;
    }

    public String getCidadeEndereco() {
        return cidadeEndereco;
    }

    public void setCidadeEndereco(String cidadeEndereco) {
        this.cidadeEndereco = cidadeEndereco;
    }

    public String getEstadoEndereco() {
        return estadoEndereco;
    }

    public void setEstadoEndereco(String estadoEndereco) {
        this.estadoEndereco = estadoEndereco;
    }

    public String getCepEndereco() {
        return cepEndereco;
    }

    public void setCepEndereco(String cepEndereco) {
        this.cepEndereco = cepEndereco;
    }

    public String getComplementoEndereco() {
        return complementoEndereco;
    }

    public void setComplementoEndereco(String complementoEndereco) {
        this.complementoEndereco = complementoEndereco;
    }
}
