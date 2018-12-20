package com.javaee.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.javaee.rabbitmq.domain.EmailInfo;
import com.javaee.rabbitmq.services.SenderMailService;

@SpringBootApplication
public class RabbitmqApplication implements CommandLineRunner {
	
	@Autowired
	SenderMailService senderMailService;

	public static void main(String[] args) {
		SpringApplication.run(RabbitmqApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		EmailInfo info = new EmailInfo();
		info.setTipo("Inicialização");
		info.setMensagem("Esse é um teste de inicialização!");
		info.setEmailVendedor("soriano.mateus@outlook.com");
		info.setEmailComprador("soriano.mateus@gmail.com");
		
		senderMailService.EnviarEmail(info);
		System.out.println("Sistema de e-mail iniciado...");
	}
}
