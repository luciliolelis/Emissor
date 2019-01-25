package com.amsoft.erp.controller.imagem;

import java.io.ByteArrayInputStream;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.amsoft.erp.model.emitente.Empresa;
import com.amsoft.erp.repository.Empresas;

@Named
@RequestScoped
public class ImageBean {

	@Inject
	Empresas empresas;
	private Empresa empresaEdicao = new Empresa();

	public Empresa getEmpresaEdicao() {
		return empresaEdicao;
	}

	public void setEmpresaEdicao(Empresa empresaEdicao) {
		this.empresaEdicao = empresaEdicao;
	}

	public StreamedContent getStreamedImageById() {
		FacesContext context = FacesContext.getCurrentInstance();

		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			return new DefaultStreamedContent();
		} else {
			String id = context.getExternalContext().getRequestParameterMap().get("id");
			empresaEdicao = empresas.porId(new Integer(id));
			return new DefaultStreamedContent(new ByteArrayInputStream(this.getEmpresaEdicao().getLogo()));
		}
	}
	
	
	
}