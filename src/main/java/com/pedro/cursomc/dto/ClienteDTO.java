package com.pedro.cursomc.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.pedro.cursomc.domain.Cliente;
import com.pedro.cursomc.services.validation.ClienteUpdate;

@ClienteUpdate
public class ClienteDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	@NotEmpty ( message = "Preenchimento obrigatorio" )
	@Length (min = 5, max = 120, message = "O tamanho deve ser entre 5 a 120 caracteres")
	private String nome;
	
	@NotEmpty ( message = "Preenchimento obrigatorio" )
	@Email ( message = "E-mail inválido" )
	private String email;
	
	@NotEmpty ( message = "Preenchimento obrigatorio" )
	private String senha;
	
	public ClienteDTO() {
		
	}

	public ClienteDTO( Cliente obj ) {
		super();
		this.id = obj.getId();
		this.nome = obj.getNome();
		this.email = obj.getEmail();
		this.senha = obj.getSenha();
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
