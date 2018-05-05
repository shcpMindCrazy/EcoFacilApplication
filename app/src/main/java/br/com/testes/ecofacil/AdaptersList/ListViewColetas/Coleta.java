package br.com.testes.ecofacil.AdaptersList.ListViewColetas;

import br.com.testes.ecofacil.ObjetoTransferencia.Contribuinte;
import br.com.testes.ecofacil.ObjetoTransferencia.ContribuinteEndereco;
import br.com.testes.ecofacil.ObjetoTransferencia.Solicitacao;
import br.com.testes.ecofacil.ObjetoTransferencia.SolicitacaoMateriais;

/**
 * Created by samue on 28/02/2018.
 */

public class Coleta {

    /*//Encapsulamento de objetos;
    private Contribuinte contribuinteColeta;
    private ContribuinteEndereco contribuinteEnderecoColeta;
    private Solicitacao solicitacaoColeta;
    private SolicitacaoMateriais materiaisColeta;

    //Método construtor para os parâmetros de objetos;
    public Coleta(Contribuinte contribuinteColeta, ContribuinteEndereco contribuinteEnderecoColeta, Solicitacao solicitacaoColeta, SolicitacaoMateriais materiaisColeta) {
        this.contribuinteColeta = contribuinteColeta;
        this.contribuinteEnderecoColeta = contribuinteEnderecoColeta;
        this.solicitacaoColeta = solicitacaoColeta;
        this.materiaisColeta = materiaisColeta;
    }

    //Get e Set dos objetos;
    public Contribuinte getContribuinteColeta() {
        return contribuinteColeta;
    }

    public void setContribuinteColeta(Contribuinte contribuinteColeta) {
        this.contribuinteColeta = contribuinteColeta;
    }

    public ContribuinteEndereco getContribuinteEnderecoColeta() {
        return contribuinteEnderecoColeta;
    }

    public void setContribuinteEnderecoColeta(ContribuinteEndereco contribuinteEnderecoColeta) {
        this.contribuinteEnderecoColeta = contribuinteEnderecoColeta;
    }

    public Solicitacao getSolicitacaoColeta() {
        return solicitacaoColeta;
    }

    public void setSolicitacaoColeta(Solicitacao solicitacaoColeta) {
        this.solicitacaoColeta = solicitacaoColeta;
    }

    public SolicitacaoMateriais getMateriaisColeta() {
        return materiaisColeta;
    }

    public void setMateriaisColeta(SolicitacaoMateriais materiaisColeta) {
        this.materiaisColeta = materiaisColeta;
    }

    //Encapsulamento das variáveis de informações exibidas;
    //Objeto - Solicitação;
    private int idSolicitacaoColeta;
    private String estadoAtualColeta;
    private String inicioSolicitacaoColeta;
    //Objeto - Contribuinte;
    private int idContribuinteColeta;
    private String nomeContribuinteColeta;
    private String sobrenomeContribuinteColeta;
    //Objeto - Contribuinte Endereco;
    private int idContribuinteEnderecoColeta;
    private String enderecoContribuinteColeta;
    //Objeto - Solicitação Material;
    private int idSolicitacaoMaterialColeta;
    private int quantidadeMaterialColeta;
    private String medidaMaterialColeta;
    private String tipoMaterialColeta;
    private String descricaoMaterialColeta;


    //Método construtor das variáveis individuais;


    public Coleta(Contribuinte contribuinteColeta, ContribuinteEndereco contribuinteEnderecoColeta,
                  Solicitacao solicitacaoColeta, SolicitacaoMateriais materiaisColeta, int idSolicitacaoColeta,
                  String estadoAtualColeta, String inicioSolicitacaoColeta, int idContribuinteColeta,
                  String nomeContribuinteColeta, String sobrenomeContribuinteColeta, int idContribuinteEnderecoColeta,
                  String enderecoContribuinteColeta, int idSolicitacaoMaterialColeta, int quantidadeMaterialColeta,
                  String medidaMaterialColeta, String tipoMaterialColeta, String descricaoMaterialColeta) {
        this.contribuinteColeta = contribuinteColeta;
        this.contribuinteEnderecoColeta = contribuinteEnderecoColeta;
        this.solicitacaoColeta = solicitacaoColeta;
        this.materiaisColeta = materiaisColeta;
        this.idSolicitacaoColeta = idSolicitacaoColeta;
        this.estadoAtualColeta = estadoAtualColeta;
        this.inicioSolicitacaoColeta = inicioSolicitacaoColeta;
        this.idContribuinteColeta = idContribuinteColeta;
        this.nomeContribuinteColeta = nomeContribuinteColeta;
        this.sobrenomeContribuinteColeta = sobrenomeContribuinteColeta;
        this.idContribuinteEnderecoColeta = idContribuinteEnderecoColeta;
        this.enderecoContribuinteColeta = enderecoContribuinteColeta;
        this.idSolicitacaoMaterialColeta = idSolicitacaoMaterialColeta;
        this.quantidadeMaterialColeta = quantidadeMaterialColeta;
        this.medidaMaterialColeta = medidaMaterialColeta;
        this.tipoMaterialColeta = tipoMaterialColeta;
        this.descricaoMaterialColeta = descricaoMaterialColeta;
    }

    //Get e Set das variáveis;
    public int getIdSolicitacaoColeta() {
        return idSolicitacaoColeta;
    }

    public void setIdSolicitacaoColeta(int idSolicitacaoColeta) {
        this.idSolicitacaoColeta = idSolicitacaoColeta;
    }

    public String getEstadoAtualColeta() {
        return estadoAtualColeta;
    }

    public void setEstadoAtualColeta(String estadoAtualColeta) {
        this.estadoAtualColeta = estadoAtualColeta;
    }

    public String getInicioSolicitacaoColeta() {
        return inicioSolicitacaoColeta;
    }

    public void setInicioSolicitacaoColeta(String inicioSolicitacaoColeta) {
        this.inicioSolicitacaoColeta = inicioSolicitacaoColeta;
    }

    public int getIdContribuinteColeta() {
        return idContribuinteColeta;
    }

    public void setIdContribuinteColeta(int idContribuinteColeta) {
        this.idContribuinteColeta = idContribuinteColeta;
    }

    public String getNomeContribuinteColeta() {
        return nomeContribuinteColeta;
    }

    public void setNomeContribuinteColeta(String nomeContribuinteColeta) {
        this.nomeContribuinteColeta = nomeContribuinteColeta;
    }

    public String getSobrenomeContribuinteColeta() {
        return sobrenomeContribuinteColeta;
    }

    public void setSobrenomeContribuinteColeta(String sobrenomeContribuinteColeta) {
        this.sobrenomeContribuinteColeta = sobrenomeContribuinteColeta;
    }

    public int getIdContribuinteEnderecoColeta() {
        return idContribuinteEnderecoColeta;
    }

    public void setIdContribuinteEnderecoColeta(int idContribuinteEnderecoColeta) {
        this.idContribuinteEnderecoColeta = idContribuinteEnderecoColeta;
    }

    public String getEnderecoContribuinteColeta() {
        return enderecoContribuinteColeta;
    }

    public void setEnderecoContribuinteColeta(String enderecoContribuinteColeta) {
        this.enderecoContribuinteColeta = enderecoContribuinteColeta;
    }

    public int getIdSolicitacaoMaterialColeta() {
        return idSolicitacaoMaterialColeta;
    }

    public void setIdSolicitacaoMaterialColeta(int idSolicitacaoMaterialColeta) {
        this.idSolicitacaoMaterialColeta = idSolicitacaoMaterialColeta;
    }

    public int getQuantidadeMaterialColeta() {
        return quantidadeMaterialColeta;
    }

    public void setQuantidadeMaterialColeta(int quantidadeMaterialColeta) {
        this.quantidadeMaterialColeta = quantidadeMaterialColeta;
    }

    public String getMedidaMaterialColeta() {
        return medidaMaterialColeta;
    }

    public void setMedidaMaterialColeta(String medidaMaterialColeta) {
        this.medidaMaterialColeta = medidaMaterialColeta;
    }

    public String getTipoMaterialColeta() {
        return tipoMaterialColeta;
    }

    public void setTipoMaterialColeta(String tipoMaterialColeta) {
        this.tipoMaterialColeta = tipoMaterialColeta;
    }

    public String getDescricaoMaterialColeta() {
        return descricaoMaterialColeta;
    }

    public void setDescricaoMaterialColeta(String descricaoMaterialColeta) {
        this.descricaoMaterialColeta = descricaoMaterialColeta;
    }*/


    private String nomeDoador;
    private String enderecoDoador;
    private String distanciaDoador;
    private String tipoMaterialDoador;
    private String pesoDoador;

    public Coleta(String nomeDoador, String enderecoDoador, String distanciaDoador, String tipoMaterialDoador, String pesoDoador) {

        this.nomeDoador = nomeDoador;
        this.enderecoDoador = enderecoDoador;
        this.distanciaDoador = distanciaDoador;
        this.tipoMaterialDoador = tipoMaterialDoador;
        this.pesoDoador = pesoDoador;
    }

    public String getNomeDoador() {
        return nomeDoador;
    }

    public void setNomeDoador(String nomeDoador) {
        this.nomeDoador = nomeDoador;
    }

    public String getEnderecoDoador() {
        return enderecoDoador;
    }

    public void setEnderecoDoador(String enderecoDoador) {
        this.enderecoDoador = enderecoDoador;
    }

    public String getDistanciaDoador() {
        return distanciaDoador;
    }

    public void setDistanciaDoador(String distanciaDoador) {
        this.distanciaDoador = distanciaDoador;
    }

    public String getTipoMaterialDoador() {
        return tipoMaterialDoador;
    }

    public void setTipoMaterialDoador(String tipoMaterialDoador) {
        this.tipoMaterialDoador = tipoMaterialDoador;
    }

    public String getPesoDoador() {
        return pesoDoador;
    }

    public void setPesoDoador(String pesoDoador) {
        this.pesoDoador = pesoDoador;
    }
}
