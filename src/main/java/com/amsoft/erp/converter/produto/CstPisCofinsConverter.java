package com.amsoft.erp.converter.produto;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.amsoft.erp.model.produto.CSTIPI;
import com.amsoft.erp.repository.produtos.CstIpis;
import com.amsoft.erp.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = CSTIPI.class)
public class CstPisCofinsConverter implements Converter {

	//@Inject
	private CstIpis cstIpis;
	
	public CstPisCofinsConverter() {
		cstIpis = CDIServiceLocator.getBean(CstIpis.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		CSTIPI retorno = null;
		
		if (value != null) {
			Long id = new Long(value);
			retorno = cstIpis.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			CSTIPI ret = (CSTIPI) value;
			return ret.getId() == null ? null : ret.getId().toString();
		}
		
		return "";
	}

}
