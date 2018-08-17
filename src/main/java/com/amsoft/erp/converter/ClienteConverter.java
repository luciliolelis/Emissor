package com.amsoft.erp.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.amsoft.erp.model.Cliente;
import com.amsoft.erp.repository.Clientes;
import com.amsoft.erp.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Cliente.class)
public class ClienteConverter implements Converter {

	//@Inject
	private Clientes clientes;
	
	public ClienteConverter() {
		this.clientes =  CDIServiceLocator.getBean(Clientes.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Cliente retorno = null;

		if (value != null) {
			retorno = this.clientes.porId(new Long(value));
		}

		return retorno;
	}
	
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Cliente cliente = (Cliente) value;
			return cliente.getId() == null ? null : cliente.getId().toString();
		}
		
		return "";
	}

}