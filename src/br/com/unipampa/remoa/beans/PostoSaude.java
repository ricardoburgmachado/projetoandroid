package br.com.unipampa.remoa.beans;

public class PostoSaude {

	private int id;
	private String nome;
	
	/**
	 * Método construtor sem nenhuma ação interna
	 */
	public PostoSaude(){  }

	/**
	 * Método construtor que faz a atribuição (seta) dos parâmetros aos seus devidos atributos globais.
	 * @param int id
	 * @param String nome
	 */
	public PostoSaude(int id, String nome){  
		
		setId(id);
		setNome(nome);
	}	
	
	/**
	 * Método construtor sem o atributo id, que faz a atribuição (seta) dos parâmetros aos seus devidos atributos globais.
	 * Utilizado na criação de um posto de saude para ser inserido na base de dados, pois então não se sabe o 'id' identificador do novo posto
	 * @param String nome
	 */
	public PostoSaude(String nome){  
		
		setNome(nome);
	}	
	
	/**
	 * Método construtor sem o atributo nome, que faz a atribuição (seta) dos parâmetros aos seus devidos atributos globais.
	 * Utiliza para criação de um posto de saúde, que vai ser inserido/encapsulado dentro de um objeto agendamento, por isso não precisa do atributo nome
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
