package br.com.testes.ecofacil.ObjetoTransferencia;

import java.io.Serializable;

/**
 * Created by samue on 12/03/2018.
 */

public class Reciclador implements Serializable {

    private int idReciclador;
    private String razaoSocialReciclador;
    private String nomeFantasiaReciclador;
    private String segmentoReciclador;
    private String emailReciclador;
    private String cnpjReciclador;
    private String senhaReciclador;

    public Reciclador() {
    }

    public Reciclador(int idReciclador, String razaoSocialReciclador, String nomeFantasiaReciclador, String segmentoReciclador, String emailReciclador, String cnpjReciclador, String senhaReciclador) {
        this.idReciclador = idReciclador;
        this.razaoSocialReciclador = razaoSocialReciclador;
        this.nomeFantasiaReciclador = nomeFantasiaReciclador;
        this.segmentoReciclador = segmentoReciclador;
        this.emailReciclador = emailReciclador;
        this.cnpjReciclador = cnpjReciclador;
        this.senhaReciclador = senhaReciclador;
    }

    public int getIdReciclador() {
        return idReciclador;
    }

    public void setIdReciclador(int idReciclador) {
        this.idReciclador = idReciclador;
    }

    public String getRazaoSocialReciclador() {
        return razaoSocialReciclador;
    }

    public void setRazaoSocialReciclador(String razaoSocialReciclador) {
        this.razaoSocialReciclador = razaoSocialReciclador;
    }

    public String getNomeFantasiaReciclador() {
        return nomeFantasiaReciclador;
    }

    public void setNomeFantasiaReciclador(String nomeFantasiaReciclador) {
        this.nomeFantasiaReciclador = nomeFantasiaReciclador;
    }

    public String getSegmentoReciclador() {
        return segmentoReciclador;
    }

    public void setSegmentoReciclador(String segmentoReciclador) {
        this.segmentoReciclador = segmentoReciclador;
    }

    public String getEmailReciclador() {
        return emailReciclador;
    }

    public void setEmailReciclador(String emailReciclador) {
        this.emailReciclador = emailReciclador;
    }

    public String getCnpjReciclador() {
        return cnpjReciclador;
    }

    public void setCnpjReciclador(String cnpjReciclador) {
        this.cnpjReciclador = cnpjReciclador;
    }

    public String getSenhaReciclador() {
        return senhaReciclador;
    }

    public void setSenhaReciclador(String senhaReciclador) {
        this.senhaReciclador = senhaReciclador;
    }

    @Override
    public String toString() {
        return "Reciclador{" +
                "idPontoReciclador=" + idReciclador +
                ", razaoSocialReciclador='" + razaoSocialReciclador + '\'' +
                ", nomeFantasiaReciclador='" + nomeFantasiaReciclador + '\'' +
                ", segmentoReciclador='" + segmentoReciclador + '\'' +
                ", emailReciclador='" + emailReciclador + '\'' +
                ", cnpjReciclador='" + cnpjReciclador + '\'' +
                ", senhaReciclador='" + senhaReciclador + '\'' +
                '}';
    }
}
