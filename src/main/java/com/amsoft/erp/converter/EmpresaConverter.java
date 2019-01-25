package com.amsoft.erp.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.amsoft.erp.model.emitente.Empresa;
import com.amsoft.erp.repository.Empresas;
import com.amsoft.erp.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Empresa.class)
public class EmpresaConverter implements Converter {

	//@Inject
	private Empresas empresas;
	
	public EmpresaConverter() {
		this.empresas =  CDIServiceLocator.getBean(Empresas.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Empresa retorno = null;

		if (value != null) {
			retorno = this.empresas.porId(new Integer(value));
		}

		return retorno;
	}
	
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Empresa empresa = (Empresa) value;
			return empresa.getId() == null ? null : empresa.getId().toString();
		}
		
		return "";
	}

}