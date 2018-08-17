package com.amsoft.erp.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.repository.NFCes;
import com.amsoft.erp.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = NFCe.class)
public class NFCeConverter implements Converter {

	//@Inject
	private NFCes nfces;
	
	public NFCeConverter() {
		this.nfces =  CDIServiceLocator.getBean(NFCes.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		NFCe retorno = null;

		if (value != null) {
			retorno = this.nfces.porId(new Long(value));
		}

		return retorno;
	}
	
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			NFCe nfce = (NFCe) value;
			return nfce.getId() == null ? null : nfce.getId().toString();
		}
		
		return "";
	}

}