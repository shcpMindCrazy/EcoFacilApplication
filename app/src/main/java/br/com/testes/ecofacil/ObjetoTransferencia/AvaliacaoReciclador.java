package br.com.testes.ecofacil.ObjetoTransferencia;

/**
 * Created by samue on 30/04/2018.
 */

public class AvaliacaoReciclador {

    //Encapsulamento das variáveis;
    private int idAvaliacaoReciclador;
    private int fkContribuinte;
    private int fkSolicitacao;
    private double quantStarsAvaliacaoReciclador;
    private String descricaoAvaliacaoReciclador;

    public AvaliacaoReciclador() {
    }

    public AvaliacaoReciclador(int idAvaliacaoReciclador, int fkContribuinte, int fkSolicitacao, double quantStarsAvaliacaoReciclador, String descricaoAvaliacaoReciclador) {
        this.idAvaliacaoReciclador = idAvaliacaoReciclador;
        this.fkContribuinte = fkContribuinte;
        this.fkSolicitacao = fkSolicitacao;
        this.quantStarsAvaliacaoReciclador = quantStarsAvaliacaoReciclador;
        this.descricaoAvaliacaoReciclador = descricaoAvaliacaoReciclador;
    }

    public AvaliacaoReciclador(int fkContribuinte, int fkSolicitacao, double quantStarsAvaliacaoReciclador, String descricaoAvaliacaoReciclador) {
        this.fkContribuinte = fkContribuinte;
        this.fkSolicitacao = fkSolicitacao;
        this.quantStarsAvaliacaoReciclador = quantStarsAvaliacaoReciclador;
        this.descricaoAvaliacaoReciclador = descricaoAvaliacaoReciclador;
    }

    public int getIdAvaliacaoReciclador() {
        return idAvaliacaoReciclador;
    }

    public void setIdAvaliacaoReciclador(int idAvaliacaoReciclador) {
        this.idAvaliacaoReciclador = idAvaliacaoReciclador;
    }

    public int getFkContribuinte() {
        return fkContribuinte;
    }

    public void setFkContribuinte(int fkContribuinte) {
        this.fkContribuinte = fkContribuinte;
    }

    public int getFkSolicitacao() {
        return fkSolicitacao;
    }

    public void setFkSolicitacao(int fkSolicitacao) {
        this.fkSolicitacao = fkSolicitacao;
    }

    public double getQuantStarsAvaliacaoReciclador() {
        return quantStarsAvaliacaoReciclador;
    }

    public void setQuantStarsAvaliacaoReciclador(double quantStarsAvaliacaoReciclador) {
        this.quantStarsAvaliacaoReciclador = quantStarsAvaliacaoReciclador;
    }

    public String getDescricaoAvaliacaoReciclador() {
        return descricaoAvaliacaoReciclador;
    }

    public void setDescricaoAvaliacaoReciclador(String descricaoAvaliacaoReciclador) {
        this.descricaoAvaliacaoReciclador = descricaoAvaliacaoReciclador;
    }
}
