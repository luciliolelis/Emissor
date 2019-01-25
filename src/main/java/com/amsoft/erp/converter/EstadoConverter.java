package com.amsoft.erp.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.amsoft.erp.model.cep.CepEstado;
import com.amsoft.erp.repository.Estados;
import com.amsoft.erp.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = CepEstado.class)
public class EstadoConverter implements Converter {

	//@Inject
	private Estados estados;
	
	public EstadoConverter() {
		this.estados =  CDIServiceLocator.getBean(Estados.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		CepEstado retorno = null;

		if (value != null) {
			retorno = this.estados.porUF(value);
		}

		return retorno;
	}
	
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			CepEstado estado = (CepEstado) value;
			return estado.getUf() == null ? null : estado.getUf().toString();
		}
		
		return "";
	}

}