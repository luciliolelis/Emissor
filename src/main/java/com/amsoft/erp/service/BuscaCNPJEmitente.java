package com.amsoft.erp.service;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;

import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

import com.amsoft.erp.controller.JsonReader;
import com.amsoft.erp.model.Cliente;
import com.amsoft.erp.model.emitente.Empresa;
import com.amsoft.erp.util.AmsoftUtils;

public class BuscaCNPJEmitente implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private JsonReader jsonReader;

	@SuppressWarnings("static-access")
	public Empresa getCadastro(Empresa empresa) throws JAXBException, JSONException, IOException, ParseException {

		String url = "https://www.receitaws.com.br/v1/cnpj/" + AmsoftUtils.removerMascara(empresa.getCnpj());
		JSONObject json = jsonReader.readJsonFromUrl(url);
		
		return jsonToEmpresa(json, empresa);
	}

	private Empresa jsonToEmpresa(JSONObject json, Empresa empresa) throws JSONException, ParseException {

		empresa.setRazao_social((String) json.get("nome"));
		empresa.setNome_fantasia((String) json.get("fantasia"));

		if (empresa.getNome_fantasia().equals("")) {
			empresa.setNome_fantasia(empresa.getRazao_social());
		}

		String cep = (String) json.get("cep");

		empresa.setCep(AmsoftUtils.removerMascara(cep));
		empresa.setUf((String) json.get("uf"));
		empresa.setCidade((String) json.get("municipio"));
		empresa.setBairro((String) json.get("bairro"));
		empresa.setLogradouro((String) json.get("logradouro"));
		empresa.setNumero((String) json.get("numero"));
		empresa.setComplemento((String) json.get("complemento"));
		empresa.setWsAmbiente(2);
		empresa.setFone((String) json.get("telefone"));
		empresa.setMail((String) json.get("email"));

		return empresa;
	}

	@SuppressWarnings("unused")
	private List<Object> getAtividadePrincipal(JSONObject json, Cliente cliente) {

		List<Object> atividade_principal = new ArrayList<>();
		atividade_principal.add(json.get("atividade_principal"));

		return atividade_principal;
	}

}
