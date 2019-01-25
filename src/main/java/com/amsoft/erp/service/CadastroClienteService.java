package com.amsoft.erp.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.amsoft.erp.model.Cliente;
import com.amsoft.erp.model.enun.TipoPessoa;
import com.amsoft.erp.repository.Clientes;
import com.amsoft.erp.repository.filter.ClienteFilter;
import com.amsoft.erp.security.UsuarioLogado;
import com.amsoft.erp.security.UsuarioSistema;
import com.amsoft.erp.util.jpa.Transactional;

public class CadastroClienteService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Clientes clientes;

	@Inject
	@UsuarioLogado
	private UsuarioSistema usuarioLogado;

	@Transactional
	public Cliente salvar(Cliente cliente) {

		String validacoes = this.getValidacoes(cliente);

		if (!validacoes.equals("")) {
			throw new NegocioException(validacoes);
		}

		if (cliente.getEmpresas().isEmpty()) {
			cliente.getEmpresas().add(this.usuarioLogado.getUsuario().getEmpresa());
		}

		return clientes.guardar(cliente);
	}

	@Transactional
	public void excluir(Cliente cliente) {
		clientes.remover(cliente);
	}

	private String getValidacoes(Cliente cliente) {

		if (this.isEstrangeiro(cliente)) {
			if (this.isDoc(cliente)) {
				if (this.isNotDocDisponivel(cliente)) {
					return "Documento já cadastrado";
				}
			}
		}

		if (this.isNotDocDisponivel(cliente)) {
			if (cliente.getTipoPessoa().equals(TipoPessoa.FISICA)) {
				return "CPF já cadastrado";
			} else {
				return "CNPJ já cadastrado";
			}
		}

		if (this.isNotEstrangeiro(cliente) && this.isNotExportacao(cliente)) {
			if (cliente.getEnderecos().isEmpty()) {
				return "Endereço: informe pelo menos um endereço";
			}
		}

		return "";
	}

	private boolean isEstrangeiro(Cliente cliente) {
		return cliente.getEstrangeiro().equals(true);
	}

	private boolean isNotEstrangeiro(Cliente cliente) {
		return !this.isEstrangeiro(cliente);
	}

	boolean isExportacao(Cliente cliente) {
		return cliente.getExterior().equals(true);
	}

	boolean isNotExportacao(Cliente cliente) {
		return !this.isExportacao(cliente);
	}

	private boolean isDocDisponivel(Cliente cliente) {

		if (this.isDoc(cliente)) {

			ClienteFilter filtro = new ClienteFilter();
			List<Cliente> clientesTodos = clientes.pesquisaCliente(filtro);

			for (Cliente item : clientesTodos) {
				if (this.isDoc(item)) {
					if (item.getDocReceitaFederal().equals(cliente.getDocReceitaFederal())) {
						if (!item.getId().equals(cliente.getId()))
							return false;
					}
				}
			}
		}

		return true;
	}

	private boolean isNotDocDisponivel(Cliente cliente) {
		return !this.isDocDisponivel(cliente);
	}

	private boolean isDoc(Cliente cliente) {
		return cliente.getDocReceitaFederal() != null && !cliente.getDocReceitaFederal().equals("");
	}

}