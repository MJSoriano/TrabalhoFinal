package com.javaee.rabbitmq.listeners;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.javaee.rabbitmq.config.RabbitMQConfig;
import com.javaee.rabbitmq.domain.Acao;
import com.javaee.rabbitmq.domain.EmailInfo;
import com.javaee.rabbitmq.domain.Message;
import com.javaee.rabbitmq.domain.Negociacao;
import com.javaee.rabbitmq.repository.AcaoRepository;
import com.javaee.rabbitmq.services.SenderMailService;

@Component
public class MessageListener {

	static final Logger logger = LoggerFactory.getLogger(MessageListener.class);

	@Autowired
	private AcaoRepository repositoryAcao;

	public void AcaoCompra(Long id, Long comprador, String Valor) {
		Date data = new Date();
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
		formatador.format(data);

		Acao updateAcao = repositoryAcao.findById(id).map(acao -> {
			acao.setComprador(comprador);
			acao.setDatacompra(data.toString());
			acao.setValoratual(Valor);
			acao.setDisponivel(0);
			return repositoryAcao.save(acao);
		}).orElseThrow(null);
	}

	public void AcaoVenda(Long id, String Valor) {
		Date data = new Date();
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
		formatador.format(data);

		Acao updateAcao = repositoryAcao.findById(id).map(acao -> {
			acao.setDatacompra(data.toString());
			acao.setValoratual(Valor);
			acao.setDisponivel(1);
			return repositoryAcao.save(acao);
		}).orElseThrow(null);
	}

	@Autowired
	SenderMailService senderMailService;

	@RabbitListener(queues = RabbitMQConfig.QUEUE_MESSAGES)
	public void processMessage(Message message) {

		logger.info("Operacao Recebida");
		logger.info("Operacao:" + message.getSubject());

		String[] Teste = message.getBody().split(";");
		EmailInfo mail = new EmailInfo();

		switch (message.getSubject().toLowerCase()) {
		case "compra":
			for (int i = 0; i < Teste.length; i++) {
				logger.info("Compra:" + Teste[i]);
				mail.setTipo("compra");
				mail.setMensagem("Informamos que a ação " + Teste[0] + " da empresa " + Teste[1] + " pertencente a "
						+ Teste[6] + " foi comprada na data " + Teste[2] + " por " + Teste[3] + " pelo valor de "
						+ Teste[4] + ".");
				mail.setEmailComprador(Teste[5]);
				mail.setEmailVendedor(Teste[7]);
			}
			
			AcaoCompra(Long.parseLong(Teste[8]),Long.parseLong(Teste[9]),Teste[4]);
			
			senderMailService.EnviarEmail(mail);
			logger.info("Email de compra enviado!");
			break;
		case "venda":
			for (int i = 0; i < Teste.length; i++) {
				logger.info("Venda:" + Teste[i]);
				mail.setTipo("venda");
				mail.setMensagem("Informamos que a ação " + Teste[0] + " da empresa " + Teste[1] + " pertencente a "
						+ Teste[6] + " foi vendida na data " + Teste[2] + " por " + Teste[3] + " pelo valor de "
						+ Teste[4] + ".");
				mail.setEmailComprador(Teste[5]);
				mail.setEmailVendedor(Teste[7]);
			}
			
			AcaoVenda(Long.parseLong(Teste[8]),Teste[4]);
			
			senderMailService.EnviarEmail(mail);
			logger.info("Email de venda enviado!");
			break;
		}
	}
}
