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
import com.amsoft.erp.model.Endereco;
import com.amsoft.erp.model.enun.TipoPessoa;

public class BuscaCNPJ implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private JsonReader jsonReader;
	


	@SuppressWarnings("static-access")
	public Cliente getCadastro(Cliente cliente) throws JAXBException, JSONException, IOException, ParseException {

		String url = "https://www.receitaws.com.br/v1/cnpj/" + removerMascara(cliente.getDocReceitaFederal());

		JSONObject json = jsonReader.readJsonFromUrl(url);

		return jsonToCliente(json, cliente);

	}

	private Cliente jsonToCliente(JSONObject json, Cliente cliente) throws JSONException, ParseException {

		cliente.setTipoPessoa(TipoPessoa.JURIDICA);
		cliente.setNome((String) json.get("nome"));
		cliente.setNomeFantasia((String) json.get("fantasia"));

		if (cliente.getNomeFantasia().equals("")) {
			cliente.setNomeFantasia(cliente.getNome());
		}


		cliente.getEnderecos().add(getEndereco(json, cliente));
		

		cliente.setTelefone((String) json.get("telefone"));
		cliente.setEmail((String) json.get("email"));

	
		return cliente;
	}

	@SuppressWarnings("unused")
	private List<Object> getAtividadePrincipal(JSONObject json, Cliente cliente) {

		List<Object> atividade_principal = new ArrayList<>();
		atividade_principal.add(json.get("atividade_principal"));

		return atividade_principal;
	}

	@SuppressWarnings("static-access")
	private Endereco getEndereco(JSONObject json, Cliente cliente) {

		Endereco endereco = new Endereco();

		String cep = (String) json.get("cep");
		endereco.setCep(this.removerMascara(cep));
		
		
		endereco.setUf((String) json.get("uf"));
		endereco.setCidade((String) json.get("municipio"));
	
		
		
		endereco.setBairro((String) json.get("bairro"));
		endereco.setLogradouro((String) json.get("logradouro"));
		endereco.setNumero((String) json.get("numero"));
		endereco.setComplemento((String) json.get("complemento"));

		endereco.setCliente(cliente);

		return endereco;
	}

	

	@SuppressWarnings("unused")
	private List<Object> getAtividadesSecundarias(JSONObject json, Cliente cliente) {

		List<Object> atividades_secundarias = new ArrayList<>();
		atividades_secundarias.add(json.get("atividades_secundarias"));

		return atividades_secundarias;
	}

	@SuppressWarnings("unused")
	private List<Object> getQuadoSocios(JSONObject json, Cliente cliente) {

		try {
			List<Object> quadro_socios = new ArrayList<>();
			quadro_socios.add(json.get("quadro_socios"));
			return quadro_socios;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return new ArrayList<>();

	}

	

	

	static String removerMascara(String str) {

		if (str.equals("") || str == null)
			return "";

		str = str.replace(".", "");
		str = str.replace("-", "");
		str = str.replace("/", "");

		return str;
	}

	

}
