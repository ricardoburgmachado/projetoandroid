package br.com.unipampa.remoa.activity;

import java.io.IOException;

import br.com.unipampa.remoa.R;
import br.com.unipampa.remoa.R.layout;
import br.com.unipampa.remoa.R.menu;
import br.com.unipampa.remoa.beans.AgenteSaude;
import br.com.unipampa.remoa.beans.PostoSaude;
import br.com.unipampa.remoa.conexaoHTTP.Logar;
import br.com.unipampa.remoa.services.AgenteSaudeService;
import br.com.unipampa.remoa.services.ConexaoBanco;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author Ricardo Burg Machado, Alencar Machado
 * @version 2.0
 * @since 2012
 * Classe que inicia a aplicação apresentando uma tela de login ao usuário, extende Activity e implementa Runnable
 */
public class LogarActivity extends Activity implements Runnable{
	
	private ProgressDialog dialog;
	private EditText loginTela;
	private EditText senhaTela;
	private Handler handler = new Handler();
	private String usuario;
	private String senha;
	private AgenteSaude agenteSaude;

	/**
	 * Método responsável pela construção da interface e gerenciamento das ações/onClick do usuário com a mesma
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logar);
        
        loginTela = (EditText)findViewById(R.id.edUsuario);
        senhaTela = (EditText) findViewById(R.id.edSenha);
        
        gerenciarListners();
        
         
    }
    
	/**
	 * Método que encapsula a inicialização e gerenciamento de botões (logar, sair) presentes na interface do usuário
	 */
    private void gerenciarListners(){
    	
        Button btLog = (Button) findViewById(R.id.btLogar);
        Button btSair = (Button) findViewById(R.id.btSair);
        
        btLog.setOnClickListener(new View.OnClickListener() {
        	
			public void onClick(View v) {
				
				usuario = loginTela.getText().toString();
			    senha = senhaTela.getText().toString();
				
				new ConexaoBanco(openOrCreateDatabase("bancoremoa2", MODE_PRIVATE, null));
		    	AgenteSaudeService agService = new AgenteSaudeService(ConexaoBanco.sQLiteDatabase);
		    
		    	if( agService.verificaLogin(usuario, senha) ){
		    		//atribui a variável global um objeto do tipo AgenteSaude que representa os dados do usuário que fez login
		    		LogarActivity.this.agenteSaude = agService.getAgenteSaude(usuario, senha);
					logar();
		    	}else{
		    		
		    		Toast.makeText( getApplicationContext(), "Dados incorretos\nVerifique os dados e tente novamente.", Toast.LENGTH_SHORT).show();
		    	}
		    	
				ConexaoBanco.sQLiteDatabase.close();	
				
			}
		});
        
        btSair.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				finish();
			}
		});
    	
    	
    }
    
    

    /**
     * Método que inicializa um ProgressDialog que avisa ao usuário que o sistema está sendo carregado
     * Inicializa uma Thread
     */
    private void logar(){
    	dialog = ProgressDialog.show(this, "LOGAR", "LOGANDO NO REMOA, por favor aguarde...",false,true);
    	new Thread(this).start();
    }

    /**
     * Método de implementação de assinatura da classe Runnable
     */
	public void run() {
		String login = loginTela.getText().toString();
		String sen = senhaTela.getText().toString();

		try {
			final boolean logado = true;

			handler.post(new Runnable() {
				public void run() {

					iniciaApp(logado);
				}
			});
			
		} catch (Exception e) {
			
			Log.e("ERRO AO LOGAR", e.getMessage(), e);
		} finally {
			
			dialog.dismiss();
		}		
	}
	
	/**
	 * @param logado
	 * Verifia se logado for true é criada uma intent com a intenção de inicializar Activity principal da aplicação 'RemoaMapaActivity' 
	 * enviando como parâmetro (puxExtra) o id do agente de saúde que fez o login
	 */
    private void iniciaApp(boolean logado){

    	logado = true;
        	
        if(logado){
        	
	    	Intent it = new Intent(getBaseContext(), RemoaMapaActivity.class);
	    	//it.setClassName("br.com.unipampa.remoa.activity", RemoaMapaActivity.class.getName());
        	it.putExtra("idAgente", agenteSaude.getId());
        	
        	startActivity(it);
				
		}else{
			Log.d("LOGOU", "LOGOU NÂO LOGOU");
				(Toast.makeText(getBaseContext(), "ERRO AO LOGAR", Toast.LENGTH_SHORT)).show();
		}
    }
    
    
    
}
