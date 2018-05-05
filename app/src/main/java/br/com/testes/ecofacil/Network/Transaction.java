package br.com.testes.ecofacil.Network;

import org.json.JSONArray;

import br.com.testes.ecofacil.Domain.WrapObjToNetwork;

/**
 * Created by samue on 05/04/2018.
 */

public interface Transaction {

    public WrapObjToNetwork doBefore();

    public void doAfter(JSONArray jsonArray);
}
