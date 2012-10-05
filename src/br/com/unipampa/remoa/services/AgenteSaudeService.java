package br.com.unipampa.remoa.services;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import br.com.unipampa.remoa.beans.AgenteSaude;
import br.com.unipampa.remoa.beans.PostoSaude;

/**
 * @author Ricardo Burg Machado, Alencar Machado
 * @version 2.0
 * @since 2012
 * @license Opensource baseada em GNU-GPL
 * Classe responsável pela persistência de dados dos objetos do tipo AgenteSaude
 */
public class AgenteSaudeService {

	
	private SQLiteDatabase db;
	private final static String TABELA_AGENTES_SAUDE = "agentesSaude";
	private final static String ATRIBUTO_ID = "id";
	private final static String ATRIBUTO_NOME= "nome";
	private final static String ATRIBUTO_ID_POSTO= "idPosto";
	private final static String ATRIBUTO_USUARIO= "usuario";
	private final static String ATRIBUTO_SENHA= "senha";
	private Context context;
	
	
	/**
	 * Método construtor que faz a atribuição de seu parâmetro do tipo SQLiteDatabase a variável global.
	 * Faz a montagem e execução de uma instrução SQL que visa criar a tabela 'agentesSaude' se a mesma ainda não existir
	 * Executa o método de verificação da tabela 'agentesSaude' para saber se a mesma contém os valores default, e caso este método 
	 * retorne true executa um método de inserção desses valores necessários
	 * @param SQLiteDatabase d
	 */
	public AgenteSaudeService(SQLiteDatabase d) {				
		
		this.db = d;		

		StringBuilder sb = new StringBuilder();
		sb.append(" CREATE TABLE IF NOT EXISTS "+TABELA_AGENTES_SAUDE);
		sb.append(" ( ");
		sb.append(""+ATRIBUTO_ID+" integer primary key autoincrement NOT NULL,");	
		sb.append(""+ATRIBUTO_NOME+" varchar(50) NOT NULL ,");
		sb.append(""+ATRIBUTO_ID_POSTO+" integer NOT NULL ,");
		sb.append(""+ATRIBUTO_USUARIO+" varchar(50) NOT NULL ,");
		sb.append(""+ATRIBUTO_SENHA+" varchar(50) NOT NULL ");
		sb.append(" ); ");
		Log.d("SQL GERADA construtor AgenteSaudeService:  ", ""+sb.toString() );		
		db.execSQL(sb.toString());
	
		if( verificaTabelaVazia() ){
			
			insereDadosTabela();
		}
	}
	
	/**
	 * Método que verifica se há algum registro na tabela 'agentesSaude'
	 * @return boolean
	 */
	private boolean verificaTabelaVazia(){
		
		int retorno = 0;
		Cursor c = db.rawQuery("SELECT COUNT("+ATRIBUTO_ID+") FROM "+TABELA_AGENTES_SAUDE , null );					
		c.moveToFirst();
		retorno = c.getInt(0);
		
		if(retorno == 0)
			return true;// a tabela está vazia
		else
			return false;// a tabela contém dados
	}
	
	/**
	 * Método que executa o método inserir inserindo novos registros na tabela 'agentesSaude', ou seja, são valores default
	 * que serão necessários para o usuário poder utilizar o programa
	 */
	private void insereDadosTabela (){
	    
        inserir( new AgenteSaude("Ricardo Burg", "admin", "admin", new PostoSaude(1) ) );
        inserir( new AgenteSaude("João das Couves", "joao", "joao", new PostoSaude(2) ) );
        inserir( new AgenteSaude("Maria da Silva Sauro", "maria", "senhamaria", new PostoSaude(2) ) );
	}
	

	/**
	 * Método que visa inserir no banco de dados um Agente de Saúde recebido por parâmetro.
	 * É feita uma concatenação de valores com suas respectivas chaves para serem atribuídos a uma instrução SQL;
	 * É executado o método de verficação de dados.
	 * Se a verificação retornar 'true' é executada a instrução SQL
	 * a qual atribui ao atributo 'id' da tabela automaticamente o próximo id disponível por isso o atributo id do objeto AgenteSaude não é necessário
	 * No final caso a instrução SQL seja executa com sucesso é mostrada uma mensagem ao usuário 'Toast', por isso a necessidade do 'Context' como parâmetro
	 * @param Evento evento
	 * @param Context context
	 * @return long
	 */
	public long inserir(AgenteSaude ag){
		
		long res = 0 ;
		ContentValues cv = new ContentValues();		
		cv.put(ATRIBUTO_NOME, ""+ ag.getNome()+"");
		cv.put(ATRIBUTO_ID_POSTO, ""+ ag.getPosto().getId()+"");
		cv.put(ATRIBUTO_USUARIO, ""+ ag.getUsuario()+"");
		cv.put(ATRIBUTO_SENHA, ""+ ag.getSenha()+"");

			
			res = db.insert(TABELA_AGENTES_SAUDE, ATRIBUTO_ID, cv);
		
		Log.d("RETORNO INSERÇÃO: ", ""+res);
		
		if(res > 0){

			Log.d("INSERÇÃO DE DADOS ", "Dados inseridos com sucesso!");
			return res;
		}else{
			
			Log.d("ERRO INSERÇÃO DE DADOS ", "Erro, a inserção não pode ser efetuada.!");
			return res;
		}
	}
	
	
	/**
	 * Método que obtém todos os agentes de saúde contidos no banco e os transforma em um array de objetos
	 * @return ArrayList<AgenteSaude>
	 */
	public ArrayList<AgenteSaude> getAgentes(){				
		
		PostoSaudeService pService = new PostoSaudeService(this.db);
		ArrayList<AgenteSaude> list = new ArrayList<AgenteSaude>();
		
		Cursor c = db.query(TABELA_AGENTES_SAUDE, new String[]{ATRIBUTO_ID,ATRIBUTO_NOME,ATRIBUTO_ID_POSTO,ATRIBUTO_USUARIO, ATRIBUTO_SENHA},
				null, null, null, null, null);					
		
		while(c.moveToNext()){
			
			list.add( new AgenteSaude(
					c.getInt(c.getColumnIndex(ATRIBUTO_ID)),
					c.getString(c.getColumnIndex(ATRIBUTO_NOME)),
					c.getString(c.getColumnIndex(ATRIBUTO_USUARIO)),
					c.getString(c.getColumnIndex(ATRIBUTO_SENHA)),
					pService.getPostoSaude(c.getInt(c.getColumnIndex(ATRIBUTO_ID_POSTO) ))
					));
		}
		//c.close();		
		
		return list;
	}
	
	
	
	/**
	 * Método que obtém único AgenteSaude contido no banco que tenha o 'id' passado por parâmetro, 
	 * caso não encontre é retornado null
	 * @param String informacao
	 * @return AgenteSaude
	 */
	public AgenteSaude getAgenteSaude(int id){
		
		PostoSaudeService pService = new PostoSaudeService(this.db);
		Cursor c = db.query(TABELA_AGENTES_SAUDE, new String[]{ATRIBUTO_ID,ATRIBUTO_NOME,ATRIBUTO_ID_POSTO,ATRIBUTO_USUARIO, ATRIBUTO_SENHA},
				ATRIBUTO_ID +" = "+id, null, null, null, null);
		
		while(c.moveToNext()){
			
			AgenteSaude agenteRetorno = new AgenteSaude(
					c.getInt(c.getColumnIndex(ATRIBUTO_ID)),
					c.getString(c.getColumnIndex(ATRIBUTO_NOME)),
					c.getString(c.getColumnIndex(ATRIBUTO_USUARIO)),
					pService.getPostoSaude(c.getInt(c.getColumnIndex(ATRIBUTO_ID_POSTO) ))
					);
			
			//c.close();
			return agenteRetorno;				
		}
		
		return null;
	}

	/**
	 * Método que obtém único AgenteSaude contido no banco que tenha o 'usuário' e senha passados por parâmetro, 
	 * caso não encontre é retornado null
	 * @param String informacao
	 * @return AgenteSaude
	 */
	public AgenteSaude getAgenteSaude(String usuario, String senha){
		
		PostoSaudeService pService = new PostoSaudeService(this.db);
		Cursor c = db.query(TABELA_AGENTES_SAUDE, new String[]{ATRIBUTO_ID,ATRIBUTO_NOME,ATRIBUTO_ID_POSTO,ATRIBUTO_USUARIO, ATRIBUTO_SENHA},
				ATRIBUTO_USUARIO +" = '"+usuario+"' AND "+ATRIBUTO_SENHA+" = '"+senha+"'", null, null, null, null);
		
		while(c.moveToNext()){
			
			AgenteSaude agenteRetorno = new AgenteSaude(
					c.getInt(c.getColumnIndex(ATRIBUTO_ID)),
					c.getString(c.getColumnIndex(ATRIBUTO_NOME)),
					c.getString(c.getColumnIndex(ATRIBUTO_USUARIO)),
					pService.getPostoSaude(c.getInt(c.getColumnIndex(ATRIBUTO_ID_POSTO) ))
					);
			
			//c.close();
			return agenteRetorno;				
		}
		
		return null;
	}
	
	/**
	 * Método que faz a verificação se os dados/parâmetros são válidos.
	 * Percorre um array com todos os agentes comparando-os um a um com os parâmetros.
	 * Se encontrar uma ocorrência retorna true, caso não encontre nenhuma igualdade (usuario e senha) retorna false.
	 * @param String usuario
	 * @param String senha
	 * @return boolean
	 */
	public boolean verificaLogin(String usuario, String senha){
	
		for (AgenteSaude agente : getAgentes() ) {
			
			if(agente.getUsuario().equalsIgnoreCase(usuario) && agente.getSenha().equalsIgnoreCase(senha)){
				
				return true;
			}
		}
		return false;
		
	}
	
	
}
