package com.amsoft.erp.controller;

import java.io.Serializable;
import java.util.Locale;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.velocity.tools.generic.NumberTool;

import com.amsoft.erp.model.Usuario;
import com.amsoft.erp.repository.Usuarios;
import com.amsoft.erp.service.CadastroUsuarioService;
import com.amsoft.erp.service.NegocioException;
import com.amsoft.erp.util.jsf.FacesUtil;
import com.amsoft.erp.util.mail.Mailer;
import com.amsoft.erp.util.mail.MyVelocityTemplate;
import com.outjected.email.api.MailMessage;

@Named
@RequestScoped
public class RecuperaSenhaEmailBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Mailer mailer;

	private String email;

	@Inject
	private Usuarios usuarios;
	
	@Inject
	private CadastroUsuarioService usuarioService;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {

		}
	}

	public void recuperar() {

		try {
			Usuario usuario = this.usuarios.porEmail(this.email);

			if (usuario == null) {
				FacesUtil
						.addInfoMessage("Nenhum usuário encontrado com o email informado.");
			} else {
				
				usuario.setSenha(gerarSenhatemporaria());
				this.usuarioService.salvar(usuario);
				
				MailMessage message = mailer.novaMensagem();

				message.to(usuario.getEmail())
						.subject("Recuperação de senha Amsoft Nf-e")
						.bodyHtml(
								new MyVelocityTemplate(getClass()
										.getResourceAsStream(
												"/emails/senha.template")))
						.put("usuario", usuario)
						.put("numberTool", new NumberTool())
						.put("locale", new Locale("pt", "BR")).send();

				FacesUtil
						.addInfoMessage("Sucesso! Verifique sua caixa de entrada.");
			}
		} catch (Exception e) {
			throw new NegocioException("Falha ao enviar o email." + e.getMessage() + e.getCause());
		}
	}
	
	private String gerarSenhatemporaria () {
		UUID uuid = UUID.randomUUID();
		String myRandom = uuid.toString();
		return myRandom.substring(0,8);
	}
}
