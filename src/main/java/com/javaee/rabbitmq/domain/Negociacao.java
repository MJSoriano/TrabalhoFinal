package com.javaee.rabbitmq.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Negociacao {
	private Long idAcao;
	private Long idComprador;
	private String valorNegociado;
}
