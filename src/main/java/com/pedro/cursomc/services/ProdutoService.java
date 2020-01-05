package com.pedro.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.pedro.cursomc.domain.Categoria;
import com.pedro.cursomc.domain.Produto;
import com.pedro.cursomc.repositories.CategoriaRepository;
import com.pedro.cursomc.repositories.ProdutoRepository;
import com.pedro.cursomc.services.exception.ObjectNotFoundException;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository repo;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	
	
	public Produto find( Integer id ) {
		Optional<Produto> obj = repo.findById( id );
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Produto.class.getName()));
	}
	
	@SuppressWarnings("deprecation")
	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPage, String orderBy, String direction){
		PageRequest pageRequest = new PageRequest( page, linesPage, Direction.valueOf( direction ), orderBy );
		
		List<Categoria> categorias = categoriaRepository.findAllById( ids );
		
		return repo.findDistinctByNomeContainingAndCategoriasIn( nome, categorias, pageRequest );
	}
}
