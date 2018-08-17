package com.amsoft.erp.converter.produto;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.amsoft.erp.model.produto.CSTICMS;
import com.amsoft.erp.repository.produtos.CstIcms;
import com.amsoft.erp.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = CSTICMS.class)
public class CstIcmsConverter implements Converter {

	//@Inject
	private CstIcms cstIcms;
	
	public CstIcmsConverter() {
		cstIcms = CDIServiceLocator.getBean(CstIcms.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		CSTICMS retorno = null;
		
		if (value != null) {
			Long id = new Long(value);
			retorno = cstIcms.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			CSTICMS icms = (CSTICMS) value;
			return icms.getId() == null ? null : icms.getId().toString();
		}
		
		return "";
	}

}
