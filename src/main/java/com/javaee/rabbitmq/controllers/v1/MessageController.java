package com.javaee.rabbitmq.controllers.v1;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.Quota.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.javaee.rabbitmq.domain.Acao;
import com.javaee.rabbitmq.domain.Comprador;
import com.javaee.rabbitmq.domain.Empresa;
import com.javaee.rabbitmq.domain.Message;
import com.javaee.rabbitmq.domain.Negociacao;
import com.javaee.rabbitmq.repository.AcaoRepository;
import com.javaee.rabbitmq.repository.CompradorRepository;
import com.javaee.rabbitmq.repository.EmpresaRepository;
import com.javaee.rabbitmq.services.MessageService;

@RestController
@RequestMapping(MessageController.BASE_URL)
public class MessageController {

	public static final String BASE_URL = "/api/v1/operacoes";

	private MessageService messageService;

	public MessageController(MessageService messageService) {
		this.messageService = messageService;
	}

	@PostMapping("/compra")
	@ResponseStatus(HttpStatus.CREATED)
	public String realizaCompra(@RequestBody Negociacao negocio) {
		Message mensagem = new Message();

		Acao acao = new Acao();
		acao = listacao(negocio.getIdAcao());
		
		Comprador vendedor = new Comprador();
		vendedor = listcomprador(acao.getComprador());

		Comprador comprador = new Comprador();
		comprador = listcomprador(negocio.getIdComprador());

		Empresa empresa = new Empresa();
		empresa = listempresa(acao.getEmpresa());

		Date data = new Date();
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
		formatador.format(data);

		String Obter = acao.getNome() + ";" + empresa.getEmpresa() + ";" + data.toString() + ";" + comprador.getNome()
				+ ";" + negocio.getValorNegociado() + ";" + comprador.getEmail() + ";" + vendedor.getNome() + ";" + vendedor.getEmail() + ";" + negocio.getIdAcao() + ";" + comprador.getId();
		mensagem.setSubject("Compra");
		mensagem.setBody(Obter);

		if(acao.getDisponivel() == 1)
			return createNewGarage(mensagem);
		else
			return "Ação indisponível para compra";
	}

	@PostMapping("/venda")
	@ResponseStatus(HttpStatus.CREATED)
	public String realizaVenda(@RequestBody Negociacao negocio) {
		Message mensagem = new Message();
		
		Acao acao = new Acao();
		acao = listacao(negocio.getIdAcao());
		
		Comprador vendedor = new Comprador();
		vendedor = listcomprador(acao.getComprador());
			
		Empresa empresa = new Empresa();
		empresa = listempresa(acao.getEmpresa());

		Date data = new Date();
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
		formatador.format(data);

		String Obter = acao.getNome() + ";" + empresa.getEmpresa() + ";" + data.toString() + ";" + ""
				+ ";" + negocio.getValorNegociado() + ";" + "nulo@ninguem.com.br" + ";" + vendedor.getNome() + ";" + vendedor.getEmail() + ";" + negocio.getIdAcao() + ";" + 0;
		mensagem.setSubject("Venda");
		mensagem.setBody(Obter);

		if(acao.getDisponivel() == 0)
			return createNewGarage(mensagem);
		else
			return "Ação indisponível para venda";
	}

	@Autowired
	private EmpresaRepository repositoryEmpresa;

	@PostMapping("/empresa")
	@ResponseStatus(HttpStatus.CREATED)
	public String AddEmpresa(@RequestBody Empresa empresa) {

		repositoryEmpresa.save(empresa);
		return "Empresa cadastrada com sucesso!";
	}

	@GetMapping("/empresa")
	@ResponseStatus(HttpStatus.OK)
	public Iterable<Empresa> listempresa() {
		return repositoryEmpresa.findAll();
	}

	@GetMapping("/empresa/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Empresa listempresa(@PathVariable Long id) {
		return repositoryEmpresa.findById(id).orElseThrow(null);
	}

	@Autowired
	private AcaoRepository repositoryAcao;

	@PostMapping("/acao")
	@ResponseStatus(HttpStatus.CREATED)
	public String AddAcao(@RequestBody Acao acaoCad) {

		repositoryAcao.save(acaoCad);
		return "Ação cadastrada com sucesso!";
	}

	@GetMapping("/acao")
	@ResponseStatus(HttpStatus.OK)
	public Iterable<Acao> listacao() {
		return repositoryAcao.findAll();
	}

	@GetMapping("/acao/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Acao listacao(@PathVariable Long id) {
		return repositoryAcao.findById(id).orElseThrow(null);
	}

	@Autowired
	private CompradorRepository repositoryComprador;

	@PostMapping("/comprador")
	@ResponseStatus(HttpStatus.CREATED)
	public String AddAcao(@RequestBody Comprador comprador) {

		repositoryComprador.save(comprador);
		return "Comprador cadastrada com sucesso!";
	}

	@GetMapping("/comprador")
	@ResponseStatus(HttpStatus.OK)
	public Iterable<Comprador> listcomprador() {
		return repositoryComprador.findAll();
	}

	@GetMapping("/comprador/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Comprador listcomprador(@PathVariable Long id) {
		return repositoryComprador.findById(id).orElseThrow(null);
	}

	public String createNewGarage(Message message) {
		messageService.sendMessage(message);
		return "Operacao Enviada";
	}
}
