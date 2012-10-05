package br.com.unipampa.remoa.beans;

/**
 * @author Ricardo Burg Machado, Alencar Machado
 * @version 2.0
 * @since 2012
 * @license Opensource baseada em GNU-GPL
 */

public class PostoSaude {

	private int id;
	private String nome;
	
	/**
	 * M�todo construtor sem nenhuma a��o interna
	 */
	public PostoSaude(){  }

	/**
	 * M�todo construtor que faz a atribui��o (seta) dos par�metros aos seus devidos atributos globais.
	 * @param int id
	 * @param String nome
	 */
	public PostoSaude(int id, String nome){  
		
		setId(id);
		setNome(nome);
	}	
	
	/**
	 * M�todo construtor sem o atributo id, que faz a atribui��o (seta) dos par�metros aos seus devidos atributos globais.
	 * Utilizado na cria��o de um posto de saude para ser inserido na base de dados, pois ent�o n�o se sabe o 'id' identificador do novo posto
	 * @param String nome
	 */
	public PostoSaude(String nome){  
		
		setNome(nome);
	}	
	
	/**
	 * M�todo construtor sem o atributo nome, que faz a atribui��o (seta) dos par�metros aos seus devidos atributos globais.
	 * Utiliza para cria��o de um posto de sa�de, que vai ser inserido/encapsulado dentro de um objeto agendamento, por isso n�o precisa do atributo nome
	 * @param int id
	 */
	public PostoSaude(int id){  
		
		setId(id);
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
	
	
	
	
}
