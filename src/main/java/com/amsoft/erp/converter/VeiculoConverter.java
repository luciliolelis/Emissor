package com.amsoft.erp.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.amsoft.erp.model.Veiculo;
import com.amsoft.erp.repository.Veiculos;
import com.amsoft.erp.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = Veiculo.class)
public class VeiculoConverter implements Converter {

	//@Inject
	private Veiculos veiculos;
	
	public VeiculoConverter() {
		this.veiculos =  CDIServiceLocator.getBean(Veiculos.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Veiculo retorno = null;

		if (value != null) {
			retorno = this.veiculos.porId(new Long(value));
		}

		return retorno;
	}
	
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Veiculo veiculo = (Veiculo) value;
			return veiculo.getId() == null ? null : veiculo.getId().toString();
		}
		
		return "";
	}

}