package br.com.testes.ecofacil.Domain;

import br.com.testes.ecofacil.AdaptersList.RecyclerView.Chamado;
import br.com.testes.ecofacil.ObjetoTransferencia.AvaliacaoContribuinte;
import br.com.testes.ecofacil.ObjetoTransferencia.AvaliacaoReciclador;
import br.com.testes.ecofacil.ObjetoTransferencia.Contribuinte;
import br.com.testes.ecofacil.ObjetoTransferencia.ContribuinteEndereco;
import br.com.testes.ecofacil.ObjetoTransferencia.Reciclador;
import br.com.testes.ecofacil.ObjetoTransferencia.RecicladorEndereco;
import br.com.testes.ecofacil.ObjetoTransferencia.Solicitacao;
import br.com.testes.ecofacil.ObjetoTransferencia.SolicitacaoMateriais;

/**
 * Created by samue on 05/04/2018.
 */

public class WrapObjToNetwork {

    private Contribuinte contribuinte;
    private ContribuinteEndereco contribuinteEndereco;
    private Reciclador reciclador;
    private RecicladorEndereco recicladorEndereco;
    private Solicitacao solicitacao;
    private SolicitacaoMateriais solicitacaoMateriais;
    private AvaliacaoContribuinte avaliacaoContribuinte;
    private AvaliacaoReciclador avaliacaoReciclador;
    private Chamado solicitacaoChamado;
    private String method;
    private String url;

    public WrapObjToNetwork(String method, String url) {
        this.method = method;
        this.url = url;
    }

    public WrapObjToNetwork(Contribuinte contribuinte, String method, String url) {
        this.contribuinte = contribuinte;
        this.method = method;
        this.url = url;
    }

    public WrapObjToNetwork(Reciclador reciclador, String method, String url) {
        this.reciclador = reciclador;
        this.method = method;
        this.url = url;
    }

    public WrapObjToNetwork(ContribuinteEndereco contribuinteEndereco, String method, String url) {
        this.contribuinteEndereco = contribuinteEndereco;
        this.method = method;
        this.url = url;
    }

    public WrapObjToNetwork(RecicladorEndereco recicladorEndereco, String method, String url) {
        this.recicladorEndereco = recicladorEndereco;
        this.method = method;
        this.url = url;
    }

    public WrapObjToNetwork(Solicitacao solicitacao, String method, String url) {
        this.solicitacao = solicitacao;
        this.method = method;
        this.url = url;
    }

    public WrapObjToNetwork(SolicitacaoMateriais solicitacaoMateriais, String method, String url) {
        this.solicitacaoMateriais = solicitacaoMateriais;
        this.method = method;
        this.url = url;
    }

    public WrapObjToNetwork(Chamado solicitacaoChamado, String method, String url) {
        this.solicitacaoChamado = solicitacaoChamado;
        this.method = method;
        this.url = url;
    }

    public WrapObjToNetwork(AvaliacaoContribuinte avaliacaoContribuinte, String method, String url) {
        this.avaliacaoContribuinte = avaliacaoContribuinte;
        this.method = method;
        this.url = url;
    }

    public WrapObjToNetwork(AvaliacaoReciclador avaliacaoReciclador, String method, String url) {
        this.avaliacaoReciclador = avaliacaoReciclador;
        this.method = method;
        this.url = url;
    }

    public Contribuinte getContribuinte() {
        return contribuinte;
    }

    public void setContribuinte(Contribuinte contribuinte) {
        this.contribuinte = contribuinte;
    }

    public Reciclador getReciclador() {
        return reciclador;
    }

    public void setReciclador(Reciclador reciclador) {
        this.reciclador = reciclador;
    }

    public ContribuinteEndereco getContribuinteEndereco() {
        return contribuinteEndereco;
    }

    public void setContribuinteEndereco(ContribuinteEndereco contribuinteEndereco) {
        this.contribuinteEndereco = contribuinteEndereco;
    }

    public Solicitacao getSolicitacao() {
        return solicitacao;
    }

    public void setSolicitacao(Solicitacao solicitacao) {
        this.solicitacao = solicitacao;
    }

    public SolicitacaoMateriais getSolicitacaoMateriais() {
        return solicitacaoMateriais;
    }

    public void setSolicitacaoMateriais(SolicitacaoMateriais solicitacaoMateriais) {
        this.solicitacaoMateriais = solicitacaoMateriais;
    }

    public Chamado getSolicitacaoChamado() {
        return solicitacaoChamado;
    }

    public void setSolicitacaoChamado(Chamado solicitacaoChamado) {
        this.solicitacaoChamado = solicitacaoChamado;
    }

    public AvaliacaoContribuinte getAvaliacaoContribuinte() {
        return avaliacaoContribuinte;
    }

    public void setAvaliacaoContribuinte(AvaliacaoContribuinte avaliacaoContribuinte) {
        this.avaliacaoContribuinte = avaliacaoContribuinte;
    }

    public AvaliacaoReciclador getAvaliacaoReciclador() {
        return avaliacaoReciclador;
    }

    public void setAvaliacaoReciclador(AvaliacaoReciclador avaliacaoReciclador) {
        this.avaliacaoReciclador = avaliacaoReciclador;
    }

    public RecicladorEndereco getRecicladorEndereco() {
        return recicladorEndereco;
    }

    public void setRecicladorEndereco(RecicladorEndereco recicladorEndereco) {
        this.recicladorEndereco = recicladorEndereco;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}