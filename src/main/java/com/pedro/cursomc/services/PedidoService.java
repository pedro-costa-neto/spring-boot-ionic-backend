package com.pedro.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.pedro.cursomc.domain.Cliente;
import com.pedro.cursomc.domain.ItemPedido;
import com.pedro.cursomc.domain.PagamentoComBoleto;
import com.pedro.cursomc.domain.Pedido;
import com.pedro.cursomc.domain.enums.EstadoPagamento;
import com.pedro.cursomc.repositories.ItemPedidoRepository;
import com.pedro.cursomc.repositories.PagamentoRepository;
import com.pedro.cursomc.repositories.PedidoRepository;
import com.pedro.cursomc.security.UserSS;
import com.pedro.cursomc.services.exception.AuthorizationException;
import com.pedro.cursomc.services.exception.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;
	
	
	public Pedido find( Integer id ) {
		Optional<Pedido> obj = repo.findById( id );
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
	
	public Pedido insert( Pedido obj ) {
		obj.setId( null );
		obj.setInstante( new Date() );
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado( EstadoPagamento.PENDENTE );
		obj.getPagamento().setPedido( obj );

		if( obj.getPagamento() instanceof PagamentoComBoleto ) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto( pagto, obj.getInstante() );
		}
		
		obj = repo.save( obj );
		pagamentoRepository.save( obj.getPagamento() );
		
		for(ItemPedido ip : obj.getItens()) {
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setDesconto(0.0);
			ip.setPedido(obj);
		}
		
		itemPedidoRepository.saveAll(obj.getItens());
		
		emailService.sendOrderConfirmationHtmlEmail(obj);
		
		return obj;
		
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		UserSS user = UserServices.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente =  clienteService.find(user.getId());
		return repo.findByCliente(cliente, pageRequest);
	}
}
