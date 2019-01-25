package com.amsoft.erp.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.amsoft.erp.model.cep.CepCidade;
import com.amsoft.erp.repository.Cidades;
import com.amsoft.erp.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = CepCidade.class)
public class CidadeConverter implements Converter {

	// @Inject
	private Cidades cidades;

	public CidadeConverter() {
		this.cidades = CDIServiceLocator.getBean(Cidades.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		CepCidade retorno = null;

		if (value != null) {
			retorno = this.cidades.porId(new Long(value));
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			CepCidade cidade = (CepCidade) value;
			return cidade.getId_cidade() == null ? null : cidade.getId_cidade().toString();
		}

		return "";
	}

}