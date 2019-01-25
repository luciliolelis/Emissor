package com.amsoft.erp.converter.produto;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.amsoft.erp.model.produto.CSOSN;
import com.amsoft.erp.repository.produtos.CsOsons;
import com.amsoft.erp.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = CSOSN.class)
public class CsosnConverter implements Converter {

	//@Inject
	private CsOsons csOsons;
	
	public CsosnConverter() {
		csOsons = CDIServiceLocator.getBean(CsOsons.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		CSOSN retorno = null;
		
		if (value != null) {
			Long id = new Long(value);
			retorno = csOsons.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			CSOSN ret = (CSOSN) value;
			return ret.getId() == null ? null : ret.getId().toString();
		}
		
		return "";
	}

}
