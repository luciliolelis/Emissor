package com.amsoft.erp.model.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import javax.persistence.Transient;

public class DadosCertificado implements Serializable {

	private static final long serialVersionUID = 1L;

	private String descricao;
	private LocalDateTime dataInicial;
	private LocalDateTime dataFinal;

	private String strDataInicial;
	private String strDataFinal;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDateTime getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(LocalDateTime date) {
		this.dataInicial = date;
	}

	public LocalDateTime getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(LocalDateTime dataFinal) {
		this.dataFinal = dataFinal;
	}

	public String getStrDataInicial() {
		return strDataInicial;
	}

	public void setStrDataInicial(String strDataInicial) {
		this.strDataInicial = strDataInicial;
	}

	public String getStrDataFinal() {
		return strDataFinal;
	}

	public void setStrDataFinal(String strDataFinal) {
		this.strDataFinal = strDataFinal;
	}

	@Transient
	public long getDiasValidos() {

		if (this.getDataFinal() != null) {
			return ChronoUnit.DAYS.between(this.getAgora(), this.getDataFinal());
		}
		return 0;
	}

	private LocalDateTime getAgora() {

		LocalDateTime agora = LocalDateTime.now();
		DateTimeFormatter formatador = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
				.withLocale(new Locale("pt", "br"));
		agora.format(formatador);
		return agora;
	}

}
