package com.amsoft.erp.service.inutilizacao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;

import com.amsoft.erp.model.Cliente;
import com.amsoft.erp.model.Empresa;
import com.amsoft.erp.model.Endereco;
import com.amsoft.erp.model.nfe.EnderecoEntrega;
import com.amsoft.erp.model.nfe.FormaPagamento;
import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.repository.Nfes;
import com.amsoft.erp.security.UsuarioLogado;
import com.amsoft.erp.security.UsuarioSistema;
import com.amsoft.erp.service.NegocioException;
import com.amsoft.erp.service.nfe.CadastroNFeService;
import com.amsoft.erp.util.jsf.FacesUtil;

public class InutilizacaoNFeService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Nfes nfes;

	@Inject
	private CadastroNFeService cadastroNFeService;

	@Inject
	private XMLInutilizacaoNFeService xmlService;

	private Endereco enderecoSelecionado = new Endereco();
	
	private List<Endereco> todosEnderecos = new ArrayList<Endereco>();
	
	@Inject
	@UsuarioLogado
	private UsuarioSistema usuarioLogado;
	
	public Nfe inutilizar(Nfe nfe) throws NegocioException, JAXBException {

		Empresa empresa = usuarioLogado.getUsuario().getEmpresa();
		nfe.setCliente(empresa.getCliente());
		nfe.setCfop(empresa.getCfop_nfce());
		nfe.setEmpresa(empresa);

		loadEnderecoSelecionado(nfe.getCliente());

		nfe.setEnderecoEntrega(new EnderecoEntrega());
		nfe.getEnderecoEntrega().setUf(enderecoSelecionado.getUf());
		nfe.getEnderecoEntrega().setCidade(enderecoSelecionado.getCidade());
		nfe.getEnderecoEntrega().setBairro(enderecoSelecionado.getBairro());
		nfe.getEnderecoEntrega().setComplemento(enderecoSelecionado.getComplemento());
		nfe.getEnderecoEntrega().setCep(enderecoSelecionado.getCep());
		nfe.getEnderecoEntrega().setLogradouro(enderecoSelecionado.getLogradouro());
		nfe.getEnderecoEntrega().setNumero(enderecoSelecionado.getNumero());

		nfe.setFormaPagamento(FormaPagamento.DINHEIRO);

		
		nfe = this.cadastroNFeService.salvar(nfe);
		nfe = this.xmlService.enviar(nfe);
		nfe = this.nfes.guardar(nfe);
		return nfe;
	}
	
	private void loadEnderecoSelecionado(Cliente cliente) {
		try {
			this.todosEnderecos = cliente.getEnderecos();
			this.enderecoSelecionado.setCep(this.todosEnderecos.get(0).getCep());
			this.enderecoSelecionado.setCidade(this.todosEnderecos.get(0).getCidade());
			this.enderecoSelecionado.setIbgeCidade(this.todosEnderecos.get(0).getIbgeCidade());

			this.enderecoSelecionado.setLogradouro(this.todosEnderecos.get(0).getLogradouro());
			this.enderecoSelecionado.setNumero(this.todosEnderecos.get(0).getNumero());
			this.enderecoSelecionado.setUf(this.todosEnderecos.get(0).getUf());

			this.enderecoSelecionado.setIbgeEstado(this.todosEnderecos.get(0).getIbgeEstado());
			this.enderecoSelecionado.setBairro(this.todosEnderecos.get(0).getBairro());
			this.enderecoSelecionado.setComplemento(this.todosEnderecos.get(0).getComplemento());
		} catch (Exception e) {
			FacesUtil.addErrorMessage("loadEnderecoSelecionado " + e.getStackTrace().toString());
		}
	}

}
