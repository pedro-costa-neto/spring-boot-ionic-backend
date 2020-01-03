package com.pedro.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.pedro.cursomc.domain.Cliente;
import com.pedro.cursomc.dto.ClienteDTO;
import com.pedro.cursomc.repositories.ClienteRepository;
import com.pedro.cursomc.services.exception.DataIntegrityException;
import com.pedro.cursomc.services.exception.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
	
	
	public Cliente find( Integer id ) {
		Optional<Cliente> obj = repo.findById( id );
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	
	
	public Cliente insert( Cliente obj ) {
		obj.setId( null );
		return repo.save( obj );
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
	
	private void updateData( Cliente newObj, Cliente obj ) {
		newObj.setNome( obj.getNome() );
		newObj.setEmail( obj.getEmail() );
	}
}
