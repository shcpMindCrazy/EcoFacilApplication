package br.com.testes.ecofacil.ObjetoTransferencia;

/**
 * Created by samue on 30/04/2018.
 */

public class AvaliacaoContribuinte {

    //Encapsulamento das variáveis;
    private int idAvaliacaoContribuinte;
    private int fkContribuinte;
    private int fkSolicitacao;
    private double quantStarsAvaliacaoContribuinte;
    private String descricaoAvaliacaoContribuinte;

    public AvaliacaoContribuinte() {
    }

    public AvaliacaoContribuinte(int idAvaliacaoContribuinte, int fkContribuinte, int fkSolicitacao, double quantStarsAvaliacaoContribuinte, String descricaoAvaliacaoContribuinte) {
        this.idAvaliacaoContribuinte = idAvaliacaoContribuinte;
        this.fkContribuinte = fkContribuinte;
        this.fkSolicitacao = fkSolicitacao;
        this.quantStarsAvaliacaoContribuinte = quantStarsAvaliacaoContribuinte;
        this.descricaoAvaliacaoContribuinte = descricaoAvaliacaoContribuinte;
    }

    public AvaliacaoContribuinte(int fkContribuinte, int fkSolicitacao, double quantStarsAvaliacaoContribuinte, String descricaoAvaliacaoContribuinte) {
        this.fkContribuinte = fkContribuinte;
        this.fkSolicitacao = fkSolicitacao;
        this.quantStarsAvaliacaoContribuinte = quantStarsAvaliacaoContribuinte;
        this.descricaoAvaliacaoContribuinte = descricaoAvaliacaoContribuinte;
    }

    public int getIdAvaliacaoContribuinte() {
        return idAvaliacaoContribuinte;
    }

    public void setIdAvaliacaoContribuinte(int idAvaliacaoContribuinte) {
        this.idAvaliacaoContribuinte = idAvaliacaoContribuinte;
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

    public double getQuantStarsAvaliacaoContribuinte() {
        return quantStarsAvaliacaoContribuinte;
    }

    public void setQuantStarsAvaliacaoContribuinte(double quantStarsAvaliacaoContribuinte) {
        this.quantStarsAvaliacaoContribuinte = quantStarsAvaliacaoContribuinte;
    }

    public String getDescricaoAvaliacaoContribuinte() {
        return descricaoAvaliacaoContribuinte;
    }

    public void setDescricaoAvaliacaoContribuinte(String descricaoAvaliacaoContribuinte) {
        this.descricaoAvaliacaoContribuinte = descricaoAvaliacaoContribuinte;
    }
}
