package com.amsoft.erp.service;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.model.UploadedFile;

public class UploadCertificadoService implements Serializable {

	private static final long serialVersionUID = 1L;


	public void upload(UploadedFile uploadedFile) {
		try {

			File file = new File(getRaizProjeto(), uploadedFile.getFileName());

			OutputStream out = new FileOutputStream(file);
			out.write(uploadedFile.getContents());
			out.close();

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Upload completo", "O arquivo " + uploadedFile.getFileName() + " foi salvo!"));
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", e.getMessage()));
		}

	}
	
	public String getRaizProjeto() {
		return FacesContext.getCurrentInstance().getExternalContext().getRealPath("") + "resources/";
	}

}
