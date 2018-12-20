package com.javaee.rabbitmq.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailInfo {
	private String tipo;
	private String mensagem;
	private String emailVendedor;
	private String emailComprador;
}
