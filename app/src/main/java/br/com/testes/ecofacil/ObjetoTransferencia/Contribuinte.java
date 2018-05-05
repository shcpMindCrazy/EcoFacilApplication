package br.com.testes.ecofacil.ObjetoTransferencia;

import java.io.Serializable;

/**
 * Created by samue on 12/03/2018.
 */

public class Contribuinte implements Serializable {

    private int idContribuinte;
    private String nomeContribuinte;
    private String sobrenomeContribuinte;
    private String celularContribuinte;
    private String generoContribuinte;
    private String estadoCivilContribuinte;
    private String cpfContribuinte;
    private String emailContribuinte;
    private String senhaContribuinte;

    //Construtor vazio;
    public Contribuinte () {}

    public Contribuinte(int idContribuinte, String nomeContribuinte, String sobrenomeContribuinte, String celularContribuinte, String generoContribuinte, String estadoCivilContribuinte, String cpfContribuinte, String emailContribuinte, String senhaContribuinte) {
        this.idContribuinte = idContribuinte;
        this.nomeContribuinte = nomeContribuinte;
        this.sobrenomeContribuinte = sobrenomeContribuinte;
        this.celularContribuinte = celularContribuinte;
        this.generoContribuinte = generoContribuinte;
        this.estadoCivilContribuinte = estadoCivilContribuinte;
        this.cpfContribuinte = cpfContribuinte;
        this.emailContribuinte = emailContribuinte;
        this.senhaContribuinte = senhaContribuinte;
    }

    public int getIdContribuinte() {
        return idContribuinte;
    }

    public void setIdContribuinte(int idContribuinte) {
        this.idContribuinte = idContribuinte;
    }

    public String getNomeContribuinte() {
        return nomeContribuinte;
    }

    public void setNomeContribuinte(String nomeContribuinte) {
        this.nomeContribuinte = nomeContribuinte;
    }

    public String getSobrenomeContribuinte() {
        return sobrenomeContribuinte;
    }

    public void setSobrenomeContribuinte(String sobrenomeContribuinte) {
        this.sobrenomeContribuinte = sobrenomeContribuinte;
    }

    public String getCelularContribuinte() {
        return celularContribuinte;
    }

    public void setCelularContribuinte(String celularContribuinte) {
        this.celularContribuinte = celularContribuinte;
    }

    public String getGeneroContribuinte() {
        return generoContribuinte;
    }

    public void setGeneroContribuinte(String generoContribuinte) {
        this.generoContribuinte = generoContribuinte;
    }

    public String getEstadoCivilContribuinte() {
        return estadoCivilContribuinte;
    }

    public void setEstadoCivilContribuinte(String estadoCivilContribuinte) {
        this.estadoCivilContribuinte = estadoCivilContribuinte;
    }

    public String getCpfContribuinte() {
        return cpfContribuinte;
    }

    public void setCpfContribuinte(String cpfContribuinte) {
        this.cpfContribuinte = cpfContribuinte;
    }

    public String getEmailContribuinte() {
        return emailContribuinte;
    }

    public void setEmailContribuinte(String emailContribuinte) {
        this.emailContribuinte = emailContribuinte;
    }

    public String getSenhaContribuinte() {
        return senhaContribuinte;
    }

    public void setSenhaContribuinte(String senhaContribuinte) {
        this.senhaContribuinte = senhaContribuinte;
    }
}
