package com.amsoft.erp.converter.produto;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.amsoft.erp.model.produto.OrigemProduto;
import com.amsoft.erp.repository.produtos.OrigemProdutos;
import com.amsoft.erp.util.cdi.CDIServiceLocator;

@FacesConverter(forClass = OrigemProduto.class)
public class OrigemProdutoConverter implements Converter {

	//@Inject
	private OrigemProdutos origemProdutos;
	
	public OrigemProdutoConverter() {
		origemProdutos = CDIServiceLocator.getBean(OrigemProdutos.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		OrigemProduto retorno = null;
		
		if (value != null) {
			Long id = new Long(value);
			retorno = origemProdutos.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			OrigemProduto origem = (OrigemProduto) value;
			return origem.getId() == null ? null : origem.getId().toString();
		}
		
		return "";
	}

}
