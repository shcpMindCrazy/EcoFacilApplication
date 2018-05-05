package br.com.testes.ecofacil.ObjetoTransferencia;

import java.io.Serializable;

/**
 * Created by samue on 16/04/2018.
 */

public class SolicitacaoMateriais implements Serializable {

    //Encapsulamento de Atributos;
    private int idSolicitacaoMaterial;
    private int fkSolicitacao;
    private double quantidadeSolicitacaoMaterial;
    private String medidaSolicitacaoMaterial;
    private String tipoSolicitacaoMaterial;
    private String descricaoSolicitacaoMaterial;

    public SolicitacaoMateriais() {
    }

    public SolicitacaoMateriais(double quantidadeSolicitacaoMaterial, String medidaSolicitacaoMaterial, String tipoSolicitacaoMaterial) {
        this.quantidadeSolicitacaoMaterial = quantidadeSolicitacaoMaterial;
        this.medidaSolicitacaoMaterial = medidaSolicitacaoMaterial;
        this.tipoSolicitacaoMaterial = tipoSolicitacaoMaterial;
    }

    public SolicitacaoMateriais(int fkSolicitacao, double quantidadeSolicitacaoMaterial, String medidaSolicitacaoMaterial, String tipoSolicitacaoMaterial, String descricaoSolicitacaoMaterial) {
        this.fkSolicitacao = fkSolicitacao;
        this.quantidadeSolicitacaoMaterial = quantidadeSolicitacaoMaterial;
        this.medidaSolicitacaoMaterial = medidaSolicitacaoMaterial;
        this.tipoSolicitacaoMaterial = tipoSolicitacaoMaterial;
        this.descricaoSolicitacaoMaterial = descricaoSolicitacaoMaterial;
    }

    public SolicitacaoMateriais(int idSolicitacaoMaterial, int fkSolicitacao, double quantidadeSolicitacaoMaterial, String medidaSolicitacaoMaterial, String tipoSolicitacaoMaterial, String descricaoSolicitacaoMaterial) {
        this.idSolicitacaoMaterial = idSolicitacaoMaterial;
        this.fkSolicitacao = fkSolicitacao;
        this.quantidadeSolicitacaoMaterial = quantidadeSolicitacaoMaterial;
        this.medidaSolicitacaoMaterial = medidaSolicitacaoMaterial;
        this.tipoSolicitacaoMaterial = tipoSolicitacaoMaterial;
        this.descricaoSolicitacaoMaterial = descricaoSolicitacaoMaterial;
    }

    public int getIdSolicitacaoMaterial() {
        return idSolicitacaoMaterial;
    }

    public void setIdSolicitacaoMaterial(int idSolicitacaoMaterial) {
        this.idSolicitacaoMaterial = idSolicitacaoMaterial;
    }

    public int getFkSolicitacao() {
        return fkSolicitacao;
    }

    public void setFkSolicitacao(int fkSolicitacao) {
        this.fkSolicitacao = fkSolicitacao;
    }

    public double getQuantidadeSolicitacaoMaterial() {
        return quantidadeSolicitacaoMaterial;
    }

    public void setQuantidadeSolicitacaoMaterial(double quantidadeSolicitacaoMaterial) {
        this.quantidadeSolicitacaoMaterial = quantidadeSolicitacaoMaterial;
    }

    public String getMedidaSolicitacaoMaterial() {
        return medidaSolicitacaoMaterial;
    }

    public void setMedidaSolicitacaoMaterial(String medidaSolicitacaoMaterial) {
        this.medidaSolicitacaoMaterial = medidaSolicitacaoMaterial;
    }

    public String getTipoSolicitacaoMaterial() {
        return tipoSolicitacaoMaterial;
    }

    public void setTipoSolicitacaoMaterial(String tipoSolicitacaoMaterial) {
        this.tipoSolicitacaoMaterial = tipoSolicitacaoMaterial;
    }

    public String getDescricaoSolicitacaoMaterial() {
        return descricaoSolicitacaoMaterial;
    }

    public void setDescricaoSolicitacaoMaterial(String descricaoSolicitacaoMaterial) {
        this.descricaoSolicitacaoMaterial = descricaoSolicitacaoMaterial;
    }
}
