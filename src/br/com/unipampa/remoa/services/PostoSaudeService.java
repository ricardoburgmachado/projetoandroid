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
 * Classe responsável pela persistência de dados dos objetos do tipo PostoSaude
 */
public class PostoSaudeService {
	
	private SQLiteDatabase db;
	private final static String TABELA_POSTOS = "postosSaude";
	private final static String ATRIBUTO_ID = "id";
	private final static String ATRIBUTO_NOME= "nome";
	private Context context;
	
	
	/**
	 * Método construtor que faz a atribuição de seu parâmetro do tipo SQLiteDatabase a variável global.
	 * Faz a montagem e execução de uma instrução SQL que visa criar a tabela 'eventos' se a mesma ainda não existir
	 * Executa o método de verificação da tabela 'postosSaude' para saber se a mesma contém os valores default, e caso este método 
	 * retorne true executa um método de inserção desses valores necessários
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
	 * Método que visa inserir no banco de dados um posto de saúde recebido por parâmetro.
	 * É feita uma concatenação e valores com suas respectivas chaves para serem atribuidos a uma instrução SQL;
	 * É executada a instrução SQL a qual atribui ao atributo '_id' da tabela automaticamente o próximo id disponível 
	 * por isso o atributo id do objeto PostoSaude não é necessário
	 * @param PostoSaude ps
	 * @return long
	 */
	public long inserir(PostoSaude ps){
		
		long res = 0 ;
		ContentValues cv = new ContentValues();		
		cv.put(ATRIBUTO_NOME, ""+ ps.getNome()+"");
		
			
			res = db.insert(TABELA_POSTOS, ATRIBUTO_ID, cv);
		
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
	 * Método que verifica se há algum registro na tabela 'postosSaude'.
	 * Caso exista alguma registro na tabela é retornado false, caso contrário true
	 * @return boolean
	 */
	private boolean verificaTabelaVazia(){
		
		int retorno = 0;
		Cursor c = db.rawQuery("SELECT COUNT("+ATRIBUTO_ID+") FROM "+TABELA_POSTOS , null );					
		c.moveToFirst();
		retorno = c.getInt(0);
		
		if(retorno == 0)
			return true;// a tabela está vazia
		else
			return false;// a tabela contém dados
	}
	
	/**
	 * Método que executa o método inserir, inserindo novos registros na tabela 'postosSaude', ou seja, são valores default
	 * que serão necessários para o usuário poder utilizar o programa
	 */
	private void insereDadosTabela (){
	    
        inserir(new PostoSaude("Centro de Saúde Santa Marta"));
        inserir(new PostoSaude("Unidade Básica de Saúde"));
	}
	
	
	
	/**
	 * Método que obtém todos os postos de saúde contidos no banco e os transforma em um array de objetos
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
	 * Método que obtém único posto de saúde contido no banco que tenha o 'id' passado por parâmetro
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
