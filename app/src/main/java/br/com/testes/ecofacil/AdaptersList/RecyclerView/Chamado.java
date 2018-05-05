package br.com.testes.ecofacil.AdaptersList.RecyclerView;

import br.com.testes.ecofacil.ObjetoTransferencia.Contribuinte;
import br.com.testes.ecofacil.ObjetoTransferencia.ContribuinteEndereco;
import br.com.testes.ecofacil.ObjetoTransferencia.Solicitacao;
import br.com.testes.ecofacil.ObjetoTransferencia.SolicitacaoMateriais;

/**
 * Created by samue on 22/03/2018.
 */

public class Chamado {

    //Encapsulamento de objetos;
    private Contribuinte contribuinteColeta;
    private ContribuinteEndereco contribuinteEnderecoColeta;
    private Solicitacao solicitacaoColeta;
    private SolicitacaoMateriais materiaisColeta;

    public Chamado() {


    }

    //Método construtor para os parâmetros de objetos;
    public Chamado(Contribuinte contribuinteColeta, ContribuinteEndereco contribuinteEnderecoColeta, Solicitacao solicitacaoColeta, SolicitacaoMateriais materiaisColeta) {
        this.contribuinteColeta = contribuinteColeta;
        this.contribuinteEnderecoColeta = contribuinteEnderecoColeta;
        this.solicitacaoColeta = solicitacaoColeta;
        this.materiaisColeta = materiaisColeta;
    }

    //Métodos de rotina;

    //Retorno do resultado do objeto de Materiais;
    public String materiaisSolicitacaoDetalhes () {

        return "Quant:" + quantidadeMaterialColeta + " - Tipo: " + tipoMaterialColeta + " - Medida:" + medidaMaterialColeta;
    }

    //Retorno do resultado da quantidade e descrição do material;
    public String materiaisSolicitacaoDescricao () {

        return "Quant:" + quantidadeMaterialColeta + " - Descricao: " + descricaoMaterialColeta;
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
    //Objeto - Consulta chamado pelo contribuinte;
    private int idContribuinteAtual;
    private String estadoAtualPesquisar;
    //Objeto - Solicitação;
    private int idSolicitacaoColeta;
    private String estadoAtualColeta;
    private String inicioSolicitacaoColeta;
    private String finalSolicitacaoColeta;
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

    //Construtor para pesquisa de solicitacoes pelo contribuinte;
    public Chamado(int idContribuinteAtual, String estadoAtualPesquisar) {
        this.idContribuinteAtual = idContribuinteAtual;
        this.estadoAtualPesquisar = estadoAtualPesquisar;
    }

    public Chamado(Contribuinte contribuinteColeta, ContribuinteEndereco contribuinteEnderecoColeta,
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
    public int getIdContribuinteAtual() {
        return idContribuinteAtual;
    }

    public void setIdContribuinteAtual(int idContribuinteAtual) {
        this.idContribuinteAtual = idContribuinteAtual;
    }

    public String getEstadoAtualPesquisar() {
        return estadoAtualPesquisar;
    }

    public void setEstadoAtualPesquisar(String estadoAtualPesquisar) {
        this.estadoAtualPesquisar = estadoAtualPesquisar;
    }

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

    public String getFinalSolicitacaoColeta() {
        return finalSolicitacaoColeta;
    }

    public void setFinalSolicitacaoColeta(String finalSolicitacaoColeta) {
        this.finalSolicitacaoColeta = finalSolicitacaoColeta;
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
    }

    /*private int idChamado;
    private Contribuinte pessoaInformacoes;
    private ContribuinteEndereco pessoaEndereco;
    private double quantidadeMaterial;
    private String tipoMaterial;
    private String tipoMedida;

    public Chamado(int idChamado, Contribuinte pessoaInformacoes, ContribuinteEndereco pessoaEndereco, double quantidadeMaterial, String tipoMaterial, String tipoMedida) {
        this.idChamado = idChamado;
        this.pessoaInformacoes = pessoaInformacoes;
        this.pessoaEndereco = pessoaEndereco;
        this.quantidadeMaterial = quantidadeMaterial;
        this.tipoMaterial = tipoMaterial;
        this.tipoMedida = tipoMedida;
    }

    public int getIdChamado() {
        return idChamado;
    }

    public void setIdChamado(int idChamado) {
        this.idChamado = idChamado;
    }

    public Contribuinte getPessoaInformacoes() {
        return pessoaInformacoes;
    }

    public void setPessoaInformacoes(Contribuinte pessoaInformacoes) {
        this.pessoaInformacoes = pessoaInformacoes;
    }

    public ContribuinteEndereco getPessoaEndereco() {
        return pessoaEndereco;
    }

    public void setPessoaEndereco(ContribuinteEndereco pessoaEndereco) {
        this.pessoaEndereco = pessoaEndereco;
    }

    public double getQuantidadeMaterial() {
        return quantidadeMaterial;
    }

    public void setQuantidadeMaterial(double quantidadeMaterial) {
        this.quantidadeMaterial = quantidadeMaterial;
    }

    public String getTipoMaterial() {
        return tipoMaterial;
    }

    public void setTipoMaterial(String tipoMaterial) {
        this.tipoMaterial = tipoMaterial;
    }

    public String getTipoMedida() {
        return tipoMedida;
    }

    public void setTipoMedida(String tipoMedida) {
        this.tipoMedida = tipoMedida;
    }*/
}
