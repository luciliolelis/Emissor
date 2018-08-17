package com.amsoft.erp.model.nfe;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "item_duplicata")
public class ItemDuplicata implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="numero_duplicata")		
	private String numeroDuplicata;
	
	@Column(name = "valor_parcela", nullable = false, precision = 10, scale = 2)
	private BigDecimal valorParcela = BigDecimal.ZERO;
	
	@Column(name = "data_vencimento")
	private Date dataVencimento = new Date();
	
	@ManyToOne
	@JoinColumn(name = "nfe_id", nullable = false)
	private Nfe nfe;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValorParcela() {
		return valorParcela;
	}

	public void setValorParcela(BigDecimal valorParcela) {
		this.valorParcela = valorParcela;
	}

	public Nfe getNfe() {
		return nfe;
	}

	public void setNfe(Nfe nfe) {
		this.nfe = nfe;
	}

	public String getNumeroDuplicata() {
		return numeroDuplicata;
	}

	public void setNumeroDuplicata(String numeroDuplicata) {
		this.numeroDuplicata = numeroDuplicata;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
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
		ItemDuplicata other = (ItemDuplicata) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


}
