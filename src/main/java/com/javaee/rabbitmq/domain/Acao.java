package com.javaee.rabbitmq.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Acao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long empresa;
	private String nome;
	private String valorinicial;
	private String valoratual;
	private String datacompra;
	private Long comprador;
	private int disponivel;
}
