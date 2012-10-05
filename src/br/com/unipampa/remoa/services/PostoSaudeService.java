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
 * Classe respons�vel pela persist�ncia de dados dos objetos do tipo PostoSaude
 */
public class PostoSaudeService {
	
	private SQLiteDatabase db;
	private final static String TABELA_POSTOS = "postosSaude";
	private final static String ATRIBUTO_ID = "id";
	private final static String ATRIBUTO_NOME= "nome";
	private Context context;
	
	
	/**
	 * M�todo construtor que faz a atribui��o de seu par�metro do tipo SQLiteDatabase a vari�vel global.
	 * Faz a montagem e execu��o de uma instru��o SQL que visa criar a tabela 'eventos' se a mesma ainda n�o existir
	 * Executa o m�todo de verifica��o da tabela 'postosSaude' para saber se a mesma cont�m os valores default, e caso este m�todo 
	 * retorne true executa um m�todo de inser��o desses valores necess�rios
	 * @param SQLiteDatabase d
	 */
	public PostoSaudeService(SQLiteDatabase d) {				
		
		this.db = d;		

		StringBuilder sb = new StringBuilder();
		sb.append(" CREATE TABLE IF NOT EXISTS "+TABELA_POSTOS);
		sb.append(" ( ");
		sb.append(""+ATRIBUTO_ID+" integer primary key autoincrement NOT NULL,");	
		sb.append(""+ATRIBUTO_NOME+" varchar(50) NOT NULL ");
		sb.append(" ); ");
		//Log.d("SQL GERADA construtor PostoSaudeService:  ", ""+sb.toString() );		
		db.execSQL(sb.toString());
		
		if( verificaTabelaVazia() ){
			
			insereDadosTabela();
		}
	
	}
	

	/**
	 * M�todo que visa inserir no banco de dados um posto de sa�de recebido por par�metro.
	 * � feita uma concatena��o e valores com suas respectivas chaves para serem atribuidos a uma instru��o SQL;
	 * � executada a instru��o SQL a qual atribui ao atributo '_id' da tabela automaticamente o pr�ximo id dispon�vel 
	 * por isso o atributo id do objeto PostoSaude n�o � necess�rio
	 * @param PostoSaude ps
	 * @return long
	 */
	public long inserir(PostoSaude ps){
		
		long res = 0 ;
		ContentValues cv = new ContentValues();		
		cv.put(ATRIBUTO_NOME, ""+ ps.getNome()+"");
		
			
			res = db.insert(TABELA_POSTOS, ATRIBUTO_ID, cv);
		
		Log.d("RETORNO INSER��O: ", ""+res);
		
		if(res > 0){

			Log.d("INSER��O DE DADOS ", "Dados inseridos com sucesso!");
			return res;
		}else{
			
			Log.d("ERRO INSER��O DE DADOS ", "Erro, a inser��o n�o pode ser efetuada.!");
			return res;
		}
	}
	
	/**
	 * M�todo que verifica se h� algum registro na tabela 'postosSaude'.
	 * Caso exista alguma registro na tabela � retornado false, caso contr�rio true
	 * @return boolean
	 */
	private boolean verificaTabelaVazia(){
		
		int retorno = 0;
		Cursor c = db.rawQuery("SELECT COUNT("+ATRIBUTO_ID+") FROM "+TABELA_POSTOS , null );					
		c.moveToFirst();
		retorno = c.getInt(0);
		
		if(retorno == 0)
			return true;// a tabela est� vazia
		else
			return false;// a tabela cont�m dados
	}
	
	/**
	 * M�todo que executa o m�todo inserir, inserindo novos registros na tabela 'postosSaude', ou seja, s�o valores default
	 * que ser�o necess�rios para o usu�rio poder utilizar o programa
	 */
	private void insereDadosTabela (){
	    
        inserir(new PostoSaude("Centro de Sa�de Santa Marta"));
        inserir(new PostoSaude("Unidade B�sica de Sa�de"));
	}
	
	
	
	/**
	 * M�todo que obt�m todos os postos de sa�de contidos no banco e os transforma em um array de objetos
	 * @return ArrayList<PostoSaude>
	 */
	public ArrayList<PostoSaude> getPostos(){				
		
		ArrayList<PostoSaude> list = new ArrayList<PostoSaude>();
		
		Cursor c = db.query(TABELA_POSTOS, new String[]{ATRIBUTO_ID,ATRIBUTO_NOME},
				null, null, null, null, null);					
		
			//int id, String nomePaciente, String descricao, String dataConsulta, String horaConsulta, AgenteSaude agente		
		while(c.moveToNext()){
			
			list.add( new PostoSaude(
					c.getInt(c.getColumnIndex(ATRIBUTO_ID)),
					c.getString(c.getColumnIndex(ATRIBUTO_NOME))
					));
		}
		//c.close();		
		
		return list;
	}
	
	
	
	/**
	 * M�todo que obt�m �nico posto de sa�de contido no banco que tenha o 'id' passado por par�metro
	 * @param int id
	 * @return PostoSaude
	 */
	public PostoSaude getPostoSaude(int id){
		
		Cursor c = db.query(TABELA_POSTOS, new String[]{ATRIBUTO_ID,ATRIBUTO_NOME},
				ATRIBUTO_ID +" = "+id, null, null, null, null);
		
		while(c.moveToNext()){
			
			PostoSaude postoRetorno = new PostoSaude(
					c.getInt(c.getColumnIndex(ATRIBUTO_ID)),
					c.getString(c.getColumnIndex(ATRIBUTO_NOME))
					);
			
			//c.close();
			return postoRetorno;				
		}
		
		return null;
	}

	

}
