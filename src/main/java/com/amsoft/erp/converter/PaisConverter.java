package com.amsoft.erp.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.amsoft.erp.model.Pais;
import com.amsoft.erp.repository.Paises;
import com.amsoft.erp.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Pais.class)
public class PaisConverter implements Converter {

	//@Inject
	private Paises paises;
	
	public PaisConverter() {
		this.paises =  CDIServiceLocator.getBean(Paises.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Pais retorno = null;

		if (value != null) {
			retorno = this.paises.porId(new Long(value));
		}

		return retorno;
	}
	
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Pais pais = (Pais) value;
			return pais.getId() == null ? null : pais.getId().toString();
		}
		
		return "";
	}

}