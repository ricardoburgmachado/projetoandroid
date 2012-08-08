package br.com.unipampa.remoa.activity;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import br.com.unipampa.remoa.R;
import br.com.unipampa.remoa.beans.AgenteSaude;
import br.com.unipampa.remoa.beans.PostoSaude;
import br.com.unipampa.remoa.services.AgendamentoService;
import br.com.unipampa.remoa.services.AgenteSaudeService;
import br.com.unipampa.remoa.services.ConexaoBanco;
import br.com.unipampa.remoa.services.PostoSaudeService;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;

/**
 * @author Ricardo Burg Machado, Alencar Machado
 * @version 2.0
 * @since 2012
 * Classe que extende MapActicity e implementa LocationListener, é chamada após o usuário estar logado.
 *
 */
public class RemoaMapaActivity extends MapActivity  implements LocationListener{

	private static final int INSERT_ID = Menu.FIRST;
	private static final int LISTAR_ID = Menu.FIRST + 1;
	private static final int VOLTAR_ID = Menu.FIRST + 2;
	private MapView mapView;
	private AgendamentoService agService;
	private AgenteSaudeService agSaudeService;
	private PostoSaudeService postoService;
	private AgenteSaude agenteLogado;
	
	
	

	/**
	 * Este método executa as seguintes ações:
	 * - inicialização do mapa;
	 * - carregamento do overlay na posição (GPS) atual do usuário;
	 * - inicializa uma conexão com base de dados
	 * - inicializa a classe eventoService 
	 * - inicializa a classe tipoEventoService a qual tem em seu construtor uma verificação importante  para o cadastro de eventos
	 * - inicializa a classe localizacaoGPS para disponibilizar a localização do usuário 
	 * - executa o método inicializaEventos para inicializar todos os eventos contidos na base de dados
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_remoa_mapa);
        mapView = (MapView) findViewById(R.id.mapa);
        new ConexaoBanco(this.openOrCreateDatabase("bancoremoa2", Context.MODE_PRIVATE, null));

        setValoresAgenteLogado();
	    
    }

    @Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
     
    private void setValoresAgenteLogado(){
    	
    	agSaudeService = new AgenteSaudeService(ConexaoBanco.sQLiteDatabase);
        Intent intent = getIntent();
        //int idPosto = (int)intent.getIntExtra("idPosto", 0);
        int idAgente = intent.getIntExtra("idAgente",0);
        this.agenteLogado = agSaudeService.getAgenteSaude(idAgente);
    }
    
    /**
     * Cria o menu de opções(Cadastrar novo evento) que é ativado ao clicar no botão menu do dispositivo/emulador
     * @param Menu menu
     * @return boolean true
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_remoa_mapa, menu);
        boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, INSERT_ID, 0, "AGENDAR CONSULTA");
		return result;
    }

   
    
    /**
     * Faz o gerenciamento das ações do menu 
     * @param MenuItem menuItem
     * @param int featureId
     * @return boolean
     */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
		switch (item.getItemId()) {
			case INSERT_ID:
				addAgendamento();
				return true;
				
			case LISTAR_ID:
				return true;
				
			case VOLTAR_ID:
				finish();
				return true;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}

	/**
	 * Método que chama AddEventoDialog que representa um caixa de diálogo para cadastrar novo evento
	 * @param Context context
	 * @param List mapOverlays
	 * @param MapView mapView
	 * @param GeoPoint geoPoint
	 * @return void
	 */
	private void addAgendamento(){
		
		AgendaConsultaDialog adEvD = new AgendaConsultaDialog(this, ConexaoBanco.sQLiteDatabase, this.agenteLogado );
		adEvD.show();
	}
	

	/**
	 * Método que diponibiliza opções ao usuário por meio de teclas do dispositivo as seguintes ações:
	 * tecla 's' -> mostra o mapa no modo satélite
	 * tecla 'r' ou 't' -> mostra o mapa no modo satélite
	 * @param int keyCode
	 * @param KeyEvent event
	 * @return boolean
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_S) {
				mapView.setSatellite(true);
				mapView.setStreetView(false);
				mapView.setTraffic(false);
				return true;
		} else if (keyCode == KeyEvent.KEYCODE_R) {
				mapView.setSatellite(false);
				mapView.setStreetView(true);
				mapView.setTraffic(false);
				return true;
		} else if (keyCode == KeyEvent.KEYCODE_T) {
				mapView.setSatellite(false);
				mapView.setStreetView(false);
				mapView.setTraffic(true);
				return true;
		}

			return super.onKeyDown(keyCode, event);
	}
	    
	public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
	}

	public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
	}

	public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
	}
		
	@Override
	protected void onResume(){
		super.onResume();
		
	}
		    
	@Override
	protected void onPause(){
		
		super.onPause();
		
	}
		
	private AgenteSaude getAgenteSaude() {
		return agenteLogado;
	}

	private void setAgenteSaude(AgenteSaude agenteSaude) {
		this.agenteLogado= agenteSaude;
	}	
	
		
		
}
