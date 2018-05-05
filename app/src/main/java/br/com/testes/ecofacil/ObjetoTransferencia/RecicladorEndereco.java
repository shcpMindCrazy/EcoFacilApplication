package br.com.testes.ecofacil.ObjetoTransferencia;

/**
 * Created by samue on 17/03/2018.
 */

public class RecicladorEndereco {

    private int idRecicladorEndereco;
    private int fkReciclador;
    private String nomeEnderecoReciclador;
    private String numeroEnderecoReciclador;
    private String bairroEnderecoReciclador;
    private String cidadeEnderecoReciclador;
    private String estadoEnderecoReciclador;
    private String paisEnderecoReciclador;
    private String cepEnderecoReciclador;
    private String complementoEnderecoReciclador;

    public RecicladorEndereco() {
    }

    /*
    * Contribinte;
    * Coletor;
    * Ponto Reciclador;

     Sacola ou Quantidade (Litro ou Kilo)

     Adicionar tipos de materiais;

     é recliclavel, é litros ou kilos - ?;

    * */

    public RecicladorEndereco(int idRecicladorEndereco, int fkReciclador, String nomeEnderecoReciclador, String numeroEnderecoReciclador, String bairroEnderecoReciclador, String cidadeEnderecoReciclador, String estadoEnderecoReciclador, String paisEnderecoReciclador, String cepEnderecoReciclador, String complementoEnderecoReciclador) {
        this.idRecicladorEndereco = idRecicladorEndereco;
        this.fkReciclador = fkReciclador;
        this.nomeEnderecoReciclador = nomeEnderecoReciclador;
        this.numeroEnderecoReciclador = numeroEnderecoReciclador;
        this.bairroEnderecoReciclador = bairroEnderecoReciclador;
        this.cidadeEnderecoReciclador = cidadeEnderecoReciclador;
        this.estadoEnderecoReciclador = estadoEnderecoReciclador;
        this.paisEnderecoReciclador = paisEnderecoReciclador;
        this.cepEnderecoReciclador = cepEnderecoReciclador;
        this.complementoEnderecoReciclador = complementoEnderecoReciclador;
    }

    @Override
    public String toString() {
        return nomeEnderecoReciclador + "," + numeroEnderecoReciclador + " - " + bairroEnderecoReciclador + ", " + cidadeEnderecoReciclador + " - " + estadoEnderecoReciclador + ", " + paisEnderecoReciclador + " - " + complementoEnderecoReciclador;
    }

    public int getIdRecicladorEndereco() {
        return idRecicladorEndereco;
    }

    public void setIdRecicladorEndereco(int idRecicladorEndereco) {
        this.idRecicladorEndereco = idRecicladorEndereco;
    }

    public int getFkReciclador() {
        return fkReciclador;
    }

    public void setFkReciclador(int fkReciclador) {
        this.fkReciclador = fkReciclador;
    }

    public String getNomeEnderecoReciclador() {
        return nomeEnderecoReciclador;
    }

    public void setNomeEnderecoReciclador(String nomeEnderecoReciclador) {
        this.nomeEnderecoReciclador = nomeEnderecoReciclador;
    }

    public String getNumeroEnderecoReciclador() {
        return numeroEnderecoReciclador;
    }

    public void setNumeroEnderecoReciclador(String numeroEnderecoReciclador) {
        this.numeroEnderecoReciclador = numeroEnderecoReciclador;
    }

    public String getBairroEnderecoReciclador() {
        return bairroEnderecoReciclador;
    }

    public void setBairroEnderecoReciclador(String bairroEnderecoReciclador) {
        this.bairroEnderecoReciclador = bairroEnderecoReciclador;
    }

    public String getCidadeEnderecoReciclador() {
        return cidadeEnderecoReciclador;
    }

    public void setCidadeEnderecoReciclador(String cidadeEnderecoReciclador) {
        this.cidadeEnderecoReciclador = cidadeEnderecoReciclador;
    }

    public String getEstadoEnderecoReciclador() {
        return estadoEnderecoReciclador;
    }

    public void setEstadoEnderecoReciclador(String estadoEnderecoReciclador) {
        this.estadoEnderecoReciclador = estadoEnderecoReciclador;
    }

    public String getPaisEnderecoReciclador() {
        return paisEnderecoReciclador;
    }

    public void setPaisEnderecoReciclador(String paisEnderecoReciclador) {
        this.paisEnderecoReciclador = paisEnderecoReciclador;
    }

    public String getCepEnderecoReciclador() {
        return cepEnderecoReciclador;
    }

    public void setCepEnderecoReciclador(String cepEnderecoReciclador) {
        this.cepEnderecoReciclador = cepEnderecoReciclador;
    }

    public String getcomplementoEnderecoReciclador() {
        return complementoEnderecoReciclador;
    }

    public void setcomplementoEnderecoReciclador(String complementoEnderecoReciclador) {
        this.complementoEnderecoReciclador = complementoEnderecoReciclador;
    }
}
