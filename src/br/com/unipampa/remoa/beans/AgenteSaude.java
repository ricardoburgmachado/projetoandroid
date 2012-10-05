package br.com.unipampa.remoa.beans;

import java.io.Serializable;

/**
 * @author Ricardo Burg Machado, Alencar Machado
 * @version 2.0
 * @since 2012
 * @license Opensource baseada em GNU-GPL
 * Classe que contém todas as características de um agendamento e seus métodos acessores e modificadores
 */
public class AgenteSaude implements Serializable{
	
	private int id;
	private String nome;
	private String usuario;
	private String senha;
	private PostoSaude posto;
	

	/**
	 * Método construtor sem nenhuma ação ineterna
	 */
	public AgenteSaude (){  }
	
	/**
	 * Método construtor que faz a atribuição (seta) dos parâmetros aos seus devidos atributos globais.
	 * @param int id
	 * @param String nome
	 * @param String usuario
	 * @param String senha
	 * @param PostoSaude posto
	 */
	public AgenteSaude (int id, String nome, String usuario, String senha, PostoSaude posto){ 
		
		setId(id);
		setNome(nome);
		setUsuario(usuario);
		setSenha(senha);
		setPosto(posto);
	}

	
	/**
	 * Método construtor SEM o atributo senha, que faz a atribuição (seta) dos parâmetros aos seus devidos atributos globais.
	 * @param int id
	 * @param String nome
	 * @param Stringusuario
	 * @param PostoSaude posto
	 */
	public AgenteSaude (int id, String nome, String usuario, PostoSaude posto){ 
		
		setId(id);
		setNome(nome);
		setUsuario(usuario);
		setPosto(posto);
	}

	/**
	 * Método construtor SEM o atributo ID, que faz a atribuição (seta) dos parâmetros aos seus devidos atributos globais.
	 * Utilizado na criação de um Agente de saúde para ser inserido na base de dados, pois então não se sabe o 'id' identificador do novo agente
	 * @param String nome
	 * @param String usuario
	 * @param String senha
	 * @param PostoSaude posto
	 */
	public AgenteSaude (String nome, String usuario, String senha, PostoSaude posto){ 
		
		setNome(nome);
		setUsuario(usuario);
		setSenha(senha);
		setPosto(posto);
	}

	/**
	 * Método construtor SEM os atributos id e senha, que faz a atribuição (seta) dos parâmetros aos seus devidos atributos globais.
	 * @param String nome
	 * @param String usuario
	 * @param PostoSaude posto
	 */
	public AgenteSaude (String nome, String usuario, PostoSaude posto){ 
		
		setNome(nome);
		setUsuario(usuario);
		setPosto(posto);
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public PostoSaude getPosto() {
		return posto;
	}
	public void setPosto(PostoSaude posto) {
		this.posto = posto;
	}
	
	
	

}
