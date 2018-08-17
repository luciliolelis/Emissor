package com.amsoft.erp.converter.produto;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.amsoft.erp.model.produto.CSTPISCOFINS;
import com.amsoft.erp.repository.produtos.CstPisCofins;
import com.amsoft.erp.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = CSTPISCOFINS.class)
public class CstIpiConverter implements Converter {

	//@Inject
	private CstPisCofins cstpiscofins;
	
	public CstIpiConverter() {
		cstpiscofins = CDIServiceLocator.getBean(CstPisCofins.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		CSTPISCOFINS retorno = null;
		
		if (value != null) {
			Long id = new Long(value);
			retorno = cstpiscofins.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			CSTPISCOFINS ret = (CSTPISCOFINS) value;
			return ret.getId() == null ? null : ret.getId().toString();
		}
		
		return "";
	}

}
