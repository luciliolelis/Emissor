package com.amsoft.erp.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.repository.Nfes;
import com.amsoft.erp.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Nfe.class)
public class NfeConverter implements Converter {

	//@Inject
	private Nfes nfes;
	
	public NfeConverter() {
		this.nfes =  CDIServiceLocator.getBean(Nfes.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Nfe retorno = null;

		if (value != null) {
			retorno = this.nfes.porId(new Long(value));
		}

		return retorno;
	}
	
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Nfe nfe = (Nfe) value;
			return nfe.getId() == null ? null : nfe.getId().toString();
		}
		
		return "";
	}

}