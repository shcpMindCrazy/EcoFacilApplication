package br.com.testes.ecofacil.ObjetoTransferencia;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by samue on 22/04/2018.
 */

public class Solicitacao implements Serializable {

    //Encapsulamento de Variáveis;
    private int idSolicitacao;
    private int fkContribuinte;
    private int fkContribuinteEndereco;
    private int fkReciclador;
    private String estadoAtualSolicitacao;
    private String inicioSolicitacao;
    private String finalSolicitacao;
    private String descricaoSolicitacao;

    //Construtores:

    //Sem parâmetros;
    public Solicitacao() {
    }

    //Parâmetros (Sem id)
    public Solicitacao(int fkContribuinte, int fkContribuinteEndereco, int fkReciclador, String estadoAtualSolicitacao, String inicioSolicitacao, String finalSolicitacao) {
        this.fkContribuinte = fkContribuinte;
        this.fkContribuinteEndereco = fkContribuinteEndereco;
        this.fkReciclador = fkReciclador;
        this.estadoAtualSolicitacao = estadoAtualSolicitacao;
        this.inicioSolicitacao = inicioSolicitacao;
        this.finalSolicitacao = finalSolicitacao;
    }

    //Parâmetros (Completos);
    public Solicitacao(int idSolicitacao, int fkContribuinteEndereco, int fkContribuinte, int fkReciclador, String estadoAtualSolicitacao, String inicioSolicitacao, String finalSolicitacao, String descricaoSolicitacao) {
        this.idSolicitacao = idSolicitacao;
        this.fkContribuinte = fkContribuinte;
        this.fkContribuinteEndereco = fkContribuinteEndereco;
        this.fkReciclador = fkReciclador;
        this.estadoAtualSolicitacao = estadoAtualSolicitacao;
        this.inicioSolicitacao = inicioSolicitacao;
        this.finalSolicitacao = finalSolicitacao;
        this.descricaoSolicitacao = descricaoSolicitacao;
    }

    //Receber dados por extenso;
    @Override
    public String toString() {
        return "Solicitacao{" +
                "idSolicitacao=" + idSolicitacao +
                ", fkContribuinte=" + fkContribuinte +
                ", fkReciclador=" + fkReciclador +
                ", estadoAtualSolicitacao='" + estadoAtualSolicitacao + '\'' +
                ", inicioSolicitacao=" + inicioSolicitacao +
                ", finalSolicitacao=" + finalSolicitacao +
                '}';
    }

    public int getIdSolicitacao() {
        return idSolicitacao;
    }

    public void setIdSolicitacao(int idSolicitacao) {
        this.idSolicitacao = idSolicitacao;
    }

    public int getFkContribuinte() {
        return fkContribuinte;
    }

    public void setFkContribuinte(int fkContribuinte) {
        this.fkContribuinte = fkContribuinte;
    }

    public int getFkContribuinteEndereco() {
        return fkContribuinteEndereco;
    }

    public void setFkContribuinteEndereco(int fkContribuinteEndereco) {
        this.fkContribuinteEndereco = fkContribuinteEndereco;
    }

    public int getFkReciclador() {
        return fkReciclador;
    }

    public void setFkReciclador(int fkReciclador) {
        this.fkReciclador = fkReciclador;
    }

    public String getEstadoAtualSolicitacao() {
        return estadoAtualSolicitacao;
    }

    public void setEstadoAtualSolicitacao(String estadoAtualSolicitacao) {
        this.estadoAtualSolicitacao = estadoAtualSolicitacao;
    }

    public String getInicioSolicitacao() {
        return inicioSolicitacao;
    }

    public void setInicioSolicitacao(String inicioSolicitacao) {
        this.inicioSolicitacao = inicioSolicitacao;
    }

    public String getFinalSolicitacao() {
        return finalSolicitacao;
    }

    public void setFinalSolicitacao(String finalSolicitacao) {
        this.finalSolicitacao = finalSolicitacao;
    }

    public String getDescricaoSolicitacao() {
        return descricaoSolicitacao;
    }

    public void setDescricaoSolicitacao(String descricaoSolicitacao) {
        this.descricaoSolicitacao = descricaoSolicitacao;
    }

}
