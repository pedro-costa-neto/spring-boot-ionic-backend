package com.pedro.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.pedro.cursomc.domain.Cliente;
import com.pedro.cursomc.domain.enums.TipoCliente;
import com.pedro.cursomc.dto.ClienteNewDTO;
import com.pedro.cursomc.repositories.ClienteRepository;
import com.pedro.cursomc.resources.exception.FieldMessage;
import com.pedro.cursomc.services.validation.utils.BR;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteNewDTO> {
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private HttpServletRequest request;
	
	@Override
	public void initialize(ClienteUpdate ann) {
	}

	@Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {

		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) request.getAttribute( HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE );
		Integer uriId = Integer.parseInt( map.get( "id" ) );
		
		List<FieldMessage> list = new ArrayList<>();
		
		
		Cliente aux = repo.findByEmail( objDto.getEmail() );
		if( aux != null && !aux.getId().equals( uriId ) ) {
			list.add( new FieldMessage( "email" , "Já existe o email (" + objDto.getEmail() + ") cadastrado!") );
		}
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}