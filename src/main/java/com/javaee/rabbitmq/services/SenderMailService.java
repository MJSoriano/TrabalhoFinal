package com.javaee.rabbitmq.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.javaee.rabbitmq.domain.EmailInfo;

@Component
public class SenderMailService {
	@Autowired
	private JavaMailSender mailSender;

	public void EnviarEmail(EmailInfo mail) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(mail.getEmailVendedor());
		if(mail.getEmailComprador() != "" && mail.getEmailComprador() != null)
			email.setCc(mail.getEmailComprador());
		email.setSubject("Mercado de Acoes - " + mail.getTipo());
		email.setText(mail.getMensagem());
		mailSender.send(email);
	}
}
