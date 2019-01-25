package com.amsoft.erp.service;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.amsoft.erp.model.vo.Webservicecep;

public class BuscaCep implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Webservicecep getEndereco(String cep) throws JAXBException, MalformedURLException {

		JAXBContext jc = JAXBContext.newInstance(Webservicecep.class);

		Unmarshaller u = jc.createUnmarshaller();
		URL url = new URL("http://cep.republicavirtual.com.br/web_cep.php?cep=" + cep + "&formato=xml");
		Webservicecep wCep = (Webservicecep) u.unmarshal(url);

		return wCep;

	}

}
