package com.amsoft.erp.service;

import java.io.Serializable;
import java.util.Date;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.amsoft.erp.controller.LoginEmpresaBean;
import com.amsoft.erp.model.Cliente;
import com.amsoft.erp.model.Grupo;
import com.amsoft.erp.model.LogAcesso;
import com.amsoft.erp.model.Usuario;
import com.amsoft.erp.model.emitente.Empresa;
import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.model.produto.Produto;
import com.amsoft.erp.repository.LogAcessos;
import com.amsoft.erp.security.UsuarioLogado;
import com.amsoft.erp.security.UsuarioSistema;

public class LogAcesssoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private HttpServletRequest request;

	@Inject
	private LogAcessos logAcessos;

	@Inject
	private LoginEmpresaBean loginEmpresaBean;

	@Inject
	@UsuarioLogado
	private UsuarioSistema usuarioLogado;

	public LogAcesso produtoAlterado(@Observes Produto produto) {

		LogAcesso log = logPadrao();
		log.setAcao("Produto: " + produto.getSku() + " - " + produto.getNome());
		return logAcessos.guardar(log);

	}

	public LogAcesso clienteAlterado(@Observes Cliente cliente) {
		LogAcesso log = logPadrao();
		log.setAcao(cliente.getTipoCliente()+": " + cliente.getId().toString() + " - " + cliente.getNome()  );

		
		return logAcessos.guardar(log);

	}

	public LogAcesso nfceAlterado(@Observes NFCe nfce) {

		LogAcesso log = logPadrao();
		log.setAcao("NFC-e: " + nfce.getStatus().getDescricao() + " - "
				+ ((nfce.getNumero() != null) ? nfce.getNumero() : "") + " Cliente: " + nfce.getCliente().getNome()
				+ " Valor: R$" + nfce.getValorTotalNota());
		return logAcessos.guardar(log);

	}

	public LogAcesso nfeAlterado(@Observes Nfe nfe) {

		LogAcesso log = logPadrao();
		log.setAcao("NF-e: " + nfe.getStatus().getDescricao() + " - "
				+ ((nfe.getNfNumero() != null) ? nfe.getNfNumero() : "") + " Cliente: " + nfe.getCliente().getNome()
				+ " Valor: R$" + nfe.getValorTotalNota());
		return logAcessos.guardar(log);

	}

	public LogAcesso usuarioAlterado(@Observes Usuario user) {

		LogAcesso log = logPadrao();
		log.setAcao("Usuario: " + user.getId().toString() + " - " + user.getNome() + " Email : " + user.getEmail());
		return logAcessos.guardar(log);

	}

	public LogAcesso grupoAlterado(@Observes Grupo grup) {

		LogAcesso log = logPadrao();
		log.setAcao("Grupo: " + grup.getId().toString() + " - " + grup.getNome());
		return logAcessos.guardar(log);

	}

	public LogAcesso emitenteAlterado(@Observes Empresa empr) {

		LogAcesso log = logPadrao();
		log.setAcao(
				"Emitente:  " + empr.getId().toString() + " - " + empr.getRazao_social() + " CNPJ : " + empr.getCnpj());
		return logAcessos.guardar(log);

	}

	private LogAcesso logPadrao() {
		LogAcesso log = new LogAcesso();

		log.setData_hora(new Date());
		log.setNome(usuarioLogado.getUsuario().getNome());
		log.setEmail(usuarioLogado.getUsername());
		log.setEmpresa(usuarioLogado.getUsuario().getEmpresa().getRazao_social());
		log.setNome_maquina(loginEmpresaBean.retNomeMaquinaCliente());
		log.setIp_cliente(request.getRemoteHost());
		return log;

	}

}
