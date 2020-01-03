package com.pedro.cursomc.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.pedro.cursomc.domain.Cidade;
import com.pedro.cursomc.domain.Cliente;
import com.pedro.cursomc.domain.Endereco;
import com.pedro.cursomc.domain.enums.TipoCliente;
import com.pedro.cursomc.dto.ClienteDTO;
import com.pedro.cursomc.dto.ClienteNewDTO;
import com.pedro.cursomc.repositories.ClienteRepository;
import com.pedro.cursomc.repositories.EnderecoRepository;
import com.pedro.cursomc.services.exception.DataIntegrityException;
import com.pedro.cursomc.services.exception.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	
	public Cliente find( Integer id ) {
		Optional<Cliente> obj = repo.findById( id );
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	
	@Transactional
	public Cliente insert( Cliente obj ) {
		obj.setId( null );
		obj = repo.save( obj );
		enderecoRepository.saveAll( obj.getEnderecos() );
		return obj;
	}
	
	public Cliente update( Cliente obj ) {
		Cliente newObj = find( obj.getId() );
		updateData( newObj, obj );
		return repo.save( newObj );
	}
	
	public void delete( Integer id ) {
		find( id );
		
		try {
			repo.deleteById( id );
		}catch ( DataIntegrityViolationException e ) {
			throw new DataIntegrityException( "Não é possivel excluir porque há entidades relacionadas!" );
		}
		
	}
	
	public List<Cliente> findAll() {
		return repo.findAll();
	}
	
	@SuppressWarnings("deprecation")
	public Page<Cliente> findPage( Integer page, Integer linesPage, String orderBy, String direction ){
		PageRequest pageRequest = new PageRequest( page, linesPage, Direction.valueOf( direction ), orderBy );
		return repo.findAll( pageRequest );
	}
	
	public Cliente fromDTO( ClienteDTO objDto) {
		return new Cliente( objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null);
	}
	
	public Cliente fromDTO( ClienteNewDTO objDto) {
		Cliente cliente   = new Cliente( null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum( objDto.getTipo() ) );
		Cidade cidade     = new Cidade( objDto.getCidadeId(), null, null );
		Endereco endereco = new Endereco( null, objDto.getLougradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cliente, cidade );
		
		cliente.getEnderecos().add( endereco );
		cliente.getTelefones().add( objDto.getTelefone1() );
		
		if( objDto.getTelefone2() != null ) {
			cliente.getTelefones().add( objDto.getTelefone2() );
		}
		
		if( objDto.getTelefone3() != null ) {
			cliente.getTelefones().add( objDto.getTelefone3() );
		}
		
		return cliente;
		
	}
	
	private void updateData( Cliente newObj, Cliente obj ) {
		newObj.setNome( obj.getNome() );
		newObj.setEmail( obj.getEmail() );
	}
	
	
}
