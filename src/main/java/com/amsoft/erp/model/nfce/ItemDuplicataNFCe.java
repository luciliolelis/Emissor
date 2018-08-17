package com.amsoft.erp.model.nfce;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "item_duplicata_nfce")
public class ItemDuplicataNFCe implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "forma_pagamento", nullable = true, length = 20)
	private FormaPagamentoNFCe formaPagamento;

	@Column(name = "valor", nullable = false, precision = 10, scale = 2)
	private BigDecimal valor = BigDecimal.ZERO;

	@ManyToOne
	@JoinColumn(name = "nfce_id", nullable = false)
	private NFCe nfce;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FormaPagamentoNFCe getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamentoNFCe formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public NFCe getNfce() {
		return nfce;
	}

	public void setNfce(NFCe nfce) {
		this.nfce = nfce;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemDuplicataNFCe other = (ItemDuplicataNFCe) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Transient
	public boolean isFormaPagamentoAssociado() {
		return (this.getFormaPagamento() != null && this.getFormaPagamento() != null);
	}

}
