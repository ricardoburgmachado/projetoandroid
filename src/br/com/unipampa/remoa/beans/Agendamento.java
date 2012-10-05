package br.com.unipampa.remoa.beans;

/**
 * @author Ricardo Burg Machado, Alencar Machado
 * @version 2.0
 * @since 2012
 * @license Opensource baseada em GNU-GPL
 * Classe que cont�m todas as caracter�sticas de um agendamento e seus m�todos acessores e modificadores
 */
public class Agendamento {

	private int id;
	private String nomePaciente;
	private String descricao;
	private String dataConsulta;
	private String horaConsulta;
	private AgenteSaude agente;
	private PostoSaude posto;
	

	/**
	 * M�todo construtor sem nenhuma a��o ineterna
	 */
	public Agendamento(){   }
	
	/**
	 * M�todo construtor que faz a atribui��o (seta) dos par�metros aos seus devidos atributos globais.
	 * Indicado para cria��o de um agendamento com dados vindos da base de dados.
	 * @param id
	 * @param nomePaciente
	 * @param descricao
	 * @param dataConsulta
	 * @param horaConsulta
	 * @param agente
	 * @param posto
	 */
	public Agendamento(int id, String nomePaciente, String descricao, String dataConsulta, String horaConsulta, AgenteSaude agente, PostoSaude posto){
		
		setId(id);
		setNomePaciente(nomePaciente);
		setDescricao(descricao);
		setDataConsulta(dataConsulta);
		setHoraConsulta(horaConsulta);
		setAgente(agente);
		setPosto(posto);
	}
	
	/**
	 * M�todo construtor SEM o atributo id, faz a atribui��o dos par�metros aos seus devidos atributos globais
	 * Utilizado na cria��o de um Agendamento para ser inserido na base de dados, pois ent�o n�o se sabe o 'id' identificador do novo Agendamento
	 * @param nomePaciente
	 * @param descricao
	 * @param dataConsulta
	 * @param horaConsulta
	 * @param agente
	 * @param posto
	 */
	public Agendamento(String nomePaciente, String descricao, String dataConsulta, String horaConsulta, AgenteSaude agente, PostoSaude posto){
		
		setNomePaciente(nomePaciente);
		setDescricao(descricao);
		setDataConsulta(dataConsulta);
		setHoraConsulta(horaConsulta);
		setAgente(agente);
		setPosto(posto);		
	}

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNomePaciente() {
		return nomePaciente;
	}
	public void setNomePaciente(String nomePaciente) {
		this.nomePaciente = nomePaciente;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getDataConsulta() {
		return dataConsulta;
	}
	public void setDataConsulta(String dataConsulta) {
		this.dataConsulta = dataConsulta;
	}
	public String getHoraConsulta() {
		return horaConsulta;
	}
	public void setHoraConsulta(String horaConsulta) {
		this.horaConsulta = horaConsulta;
	}
	public AgenteSaude getAgente() {
		return agente;
	}
	public void setAgente(AgenteSaude agente) {
		this.agente = agente;
	}
	
	public PostoSaude getPosto() {
		return posto;
	}

	public void setPosto(PostoSaude posto) {
		this.posto = posto;
	}
	
	
}
