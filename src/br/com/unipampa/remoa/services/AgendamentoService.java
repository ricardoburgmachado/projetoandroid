package br.com.unipampa.remoa.services;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;
import br.com.unipampa.remoa.beans.Agendamento;
import br.com.unipampa.remoa.beans.AgenteSaude;
import br.com.unipampa.remoa.beans.PostoSaude;

/**
 * @author Ricardo Burg Machado, Alencar Machado
 * @version 2.0
 * @since 2012
 * @license Opensource baseada em GNU-GPL
 * Classe respons�vel pela persist�ncia de dados dos objetos do tipo Agendamento
 */
public class AgendamentoService {
	
	private SQLiteDatabase db;
	private final static String TABELA_AGENDAMENTOS = "agendamentos";
	private final static String ATRIBUTO_ID = "id";
	private final static String ATRIBUTO_NOME_PACIENTE = "nomePaciente";
	private final static String ATRIBUTO_DESCRICAO = "descricao";
	private final static String ATRIBUTO_DATA_CONSULTA = "dataConsulta";
	private final static String ATRIBUTO_HORA_CONSULTA = "horaConsulta";
	private final static String ATRIBUTO_ID_AGENTE = "idAgente";
	private final static String ATRIBUTO_ID_POSTO = "idPosto";
	private Context context;
	
	
	/**
	 * M�todo construtor que faz a atribui��o de seu par�metro do tipo SQLiteDatabase a vari�vel global.
	 * Faz a montagem e execu��o de uma instru��o SQL que visa criar a tabela 'agendamentos' se a mesma ainda n�o existir
	 * @param SQLiteDatabase d
	 */
	public AgendamentoService(SQLiteDatabase d) {				
		
		this.db = d;		

		StringBuilder sb = new StringBuilder();
		sb.append(" CREATE TABLE IF NOT EXISTS "+TABELA_AGENDAMENTOS);
		sb.append(" ( ");
		sb.append(""+ATRIBUTO_ID+" integer primary key autoincrement NOT NULL,");	
		sb.append(""+ATRIBUTO_NOME_PACIENTE+" varchar(50) NOT NULL,");
		sb.append(""+ATRIBUTO_DESCRICAO+" varchar(500) NULL,");
		sb.append(""+ATRIBUTO_DATA_CONSULTA+" varchar(10) NOT NULL,");
		sb.append(""+ATRIBUTO_HORA_CONSULTA+" varchar(8) NOT NULL,");
		sb.append(""+ATRIBUTO_ID_AGENTE+" integer NOT NULL,");
		sb.append(""+ATRIBUTO_ID_POSTO+" integer NOT NULL,");
		sb.append(" FOREIGN KEY ("+ATRIBUTO_ID_AGENTE+") REFERENCES agentesSaude (id) ,");
		sb.append(" FOREIGN KEY ("+ATRIBUTO_ID_POSTO+") REFERENCES postosSaude (id)");
		sb.append(" ); ");
				
		db.execSQL(sb.toString());
	
	}
	

	/**
	 * M�todo que visa inserir no banco de dados um agendamento recebido por par�metro.
	 * � feita uma concatena��o e valores com suas respectivas chaves para serem atribuidos a uma instru��o SQL;
	 * � executado o m�todo de verfica��o de dados.
	 * Se a verifica��o retornar 'true' � executada a instru��o SQL
	 * a qual atribui ao atributo 'id' da tabela automaticamente o pr�ximo id dispon�vel por isso o atributo id do objeto agendamento n�o � necess�rio
	 * No final caso a instru��o SQL seja executada com sucesso � mostrada uma mensagem ao usu�rio 'Toast', por isso a necessidade do 'Context' como par�metro
	 * @param Agendamento agendamento
	 * @param Context context
	 * @return boolean
	 */
	public boolean inserir(Agendamento agendamento, Context context){
		
		long res = 0;
		ContentValues cv = new ContentValues();		
		cv.put(ATRIBUTO_NOME_PACIENTE, 		   ""+agendamento.getNomePaciente()+"");
		cv.put(ATRIBUTO_DESCRICAO, 		   ""+agendamento.getDescricao()+"");
		cv.put(ATRIBUTO_DATA_CONSULTA, 		   ""+agendamento.getDataConsulta()+"");
		cv.put(ATRIBUTO_HORA_CONSULTA, 		   ""+agendamento.getHoraConsulta()+"");
		cv.put(ATRIBUTO_ID_AGENTE, 		   ""+agendamento.getAgente().getId()+"");
		cv.put(ATRIBUTO_ID_POSTO, 		   ""+agendamento.getAgente().getPosto().getId()+"");
		
		
		if( verificaDados(agendamento, context) ){
			
			res = db.insert(TABELA_AGENDAMENTOS, ATRIBUTO_ID, cv);
		}
		
		Log.d("RETORNO INSER��O: ", ""+res);
		
		if(res > 0){
			Toast.makeText( context , "Agendamento cadastrado com sucesso!", Toast.LENGTH_LONG).show();				
			Log.d("INSER��O DE DADOS ", "Dados inseridos com sucesso!");
			return true;
		}else{
			
			Log.d("ERRO INSER��O DE DADOS ", "Erro, a inser��o n�o pode ser efetuada.!");
			return false;
		}
	}
	
	/**
	 * M�todo para uso interno da classe que verifica se alguns atributos do agendamento est�o preenchidos
	 * Caso exista um ou mais erros � apresentado uma mensagem 'Toast' ao usu�rio com a descri��o dos erros encontrados (por isso o Context) 
	 * @param Agendamento ag
	 * @param Context context
	 * @return boolean
	 */
	 private boolean verificaDados(Agendamento ag, Context context){
		
	    int erros = 0;
	    String errosDescricao = "";
	    
		
		if(ag.getNomePaciente().isEmpty()){
			
			erros++;
			errosDescricao += "\n[ Paciente ] est� vazio.";
		}
		
		if(ag.getDataConsulta().isEmpty()){
			
			erros++;
			errosDescricao += "\n[ Data da consulta ] est� vazio.";
		}
		
		if(ag.getHoraConsulta().isEmpty()){
			
			erros++;
			errosDescricao += "\n[ Hora da consulta ] est� vazio.";
		}

		
		if(erros > 0){
			Toast.makeText( context , "Os seguintes campos est�o com problemas:"+errosDescricao, 60).show();				
			return false;
		}else{
			
			return true;
		}
		
	}
	
	
	
	/**
	 * M�todo que obt�m todos os agendamentos contidos no banco e os transforma em um array de objetos
	 * @return ArrayList<Agendamento>
	 */
	public ArrayList<Agendamento> getAgendamentos(){				
		
		AgenteSaudeService aService = new AgenteSaudeService(db);
		PostoSaudeService pService = new PostoSaudeService(db);
		ArrayList<Agendamento> list = new ArrayList<Agendamento>();
		
		
		Cursor c = db.query(TABELA_AGENDAMENTOS, new String[]{ATRIBUTO_ID,ATRIBUTO_NOME_PACIENTE,ATRIBUTO_DESCRICAO,ATRIBUTO_DATA_CONSULTA,ATRIBUTO_HORA_CONSULTA,ATRIBUTO_ID_AGENTE,ATRIBUTO_ID_POSTO},
				null, null, null, null, null);					
					
		while(c.moveToNext()){
			
			list.add(new Agendamento( 
					c.getInt(c.getColumnIndex(ATRIBUTO_ID)),
					c.getString(c.getColumnIndex(ATRIBUTO_NOME_PACIENTE)),
					c.getString(c.getColumnIndex(ATRIBUTO_DESCRICAO)),
					c.getString(c.getColumnIndex(ATRIBUTO_DATA_CONSULTA)),
					c.getString(c.getColumnIndex(ATRIBUTO_HORA_CONSULTA)),
					aService.getAgenteSaude(c.getInt(c.getColumnIndex(ATRIBUTO_ID_AGENTE) ) ),
					pService.getPostoSaude(c.getInt(c.getColumnIndex(ATRIBUTO_ID_POSTO) ) )
					));
					
		}
		//c.close();		
		
		return list;
	}

	
	
	
	/**
	 * M�todo que obt�m �nico agendamento contido no banco que tenha o 'id' passado por par�metro.
	 * Caso n�o encontre, � retornado null
	 * @param int id
	 * @return Agendamento
	 */
	public Agendamento getAgendamento(int id){
		
		AgenteSaudeService aService = new AgenteSaudeService(db);
		PostoSaudeService pService = new PostoSaudeService(db);
		Cursor c = db.query(TABELA_AGENDAMENTOS, new String[]{ATRIBUTO_ID,ATRIBUTO_NOME_PACIENTE,ATRIBUTO_DESCRICAO,ATRIBUTO_DATA_CONSULTA,ATRIBUTO_HORA_CONSULTA,ATRIBUTO_ID_AGENTE,ATRIBUTO_ID_POSTO},
				ATRIBUTO_ID +" = "+id, null, null, null, null);
		
		while(c.moveToNext()){
			
			
			Agendamento agendamentoRetorno = new Agendamento( 
					c.getInt(c.getColumnIndex(ATRIBUTO_ID)),
					c.getString(c.getColumnIndex(ATRIBUTO_NOME_PACIENTE)),
					c.getString(c.getColumnIndex(ATRIBUTO_DESCRICAO)),
					c.getString(c.getColumnIndex(ATRIBUTO_DATA_CONSULTA)),
					c.getString(c.getColumnIndex(ATRIBUTO_HORA_CONSULTA)),
					aService.getAgenteSaude(c.getInt(c.getColumnIndex(ATRIBUTO_ID_AGENTE) ) ),
					pService.getPostoSaude(c.getInt(c.getColumnIndex(ATRIBUTO_ID_POSTO) ) )
					);
			
			//c.close();
			return agendamentoRetorno;				
		}
		
		return null;
	}

}
