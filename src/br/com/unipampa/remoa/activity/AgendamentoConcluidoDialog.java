package br.com.unipampa.remoa.activity;

import java.util.Calendar;

import br.com.unipampa.remoa.R;
import br.com.unipampa.remoa.beans.Agendamento;
import br.com.unipampa.remoa.beans.AgenteSaude;

import br.com.unipampa.remoa.services.AgendamentoService;
import br.com.unipampa.remoa.services.ConexaoBanco;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
/**
 * @author Ricardo Burg Machado, Alencar Machado
 * @version 2.0
 * @since 2012
 * @license Opensource baseada em GNU-GPL
 * Classe que extende AlertDialog que tem como finalidade a cria��o de uma caixa de di�logo que mostra os dados de um agendamento rec�m conclu�do
 */
public class AgendamentoConcluidoDialog extends AlertDialog {

	private Context context;
	private View promptsView;
	private Button btnFechar;
	private TextView textViewPaciente;
	private TextView textViewData;
	private TextView textViewHora;
	private TextView textViewAgente;
	private TextView textViewPosto;
	private TextView textViewDescricao;
	private LayoutInflater layoutInflater;
	private Agendamento agendamento;
	
	/**
	 * M�todo construtor que inicializa todos os componentes da interface do usu�rio mostrando os dados preenchidos referente a um novo agendamento
	 * Tamb�m � feita a inicializa��o de m�todos para gerenciamento de listners e de inser��o de dados neste dialog
	 * O par�metro do tipo AgenteSaude � um objeto que vem justamente trazendo dados oriundos do cadastramento de um agendamento que foi bem sucedido
	 * @param Context context
	 * @param Agendamento agendamento
	 * @param SQLiteDatabase sQLiteDatabase
	 */
	protected AgendamentoConcluidoDialog(Context context,  SQLiteDatabase sQLiteDatabase, Agendamento agendamento) {
		super(context);

		this.context = context;
		this.agendamento = agendamento;
		
		setTitle("AGENDAMENTO");
		setIcon(R.drawable.ic_launcher);
		layoutInflater = LayoutInflater.from(context);
		promptsView = layoutInflater.inflate(R.layout.agendamentoconcluido, null);
		setView(promptsView);
		
		setaDadosForm();
		gerenciarListners();
		
	}
	
	/**
	 * M�todo que recupera os componentes textView do arquivo xml correpondente e insere os dados em cada um deles 
	 */
	private void setaDadosForm(){
		
		textViewPaciente = (TextView) promptsView.findViewById(R.id.textViewPaciente);
		textViewData = (TextView) promptsView.findViewById(R.id.textViewData);
		textViewHora = (TextView) promptsView.findViewById(R.id.textViewHora);
		textViewAgente = (TextView) promptsView.findViewById(R.id.textViewAgente);
		textViewPosto = (TextView) promptsView.findViewById(R.id.textViewPosto);
		textViewDescricao = (TextView) promptsView.findViewById(R.id.textViewDescricao);
		
		textViewPaciente.setText(agendamento.getNomePaciente());
		textViewData.setText("Dia "+agendamento.getDataConsulta());
		textViewHora.setText(" �s "+agendamento.getHoraConsulta()+" horas");
		textViewAgente.setText(agendamento.getAgente().getNome());
		textViewPosto.setText(agendamento.getPosto().getNome());
		textViewDescricao.setText(agendamento.getDescricao());
	}
	
	/**
	 * M�todo que encapsula o gerenciamento do bot�o (fechar) presente na interface
	 */
	private void gerenciarListners(){
		
		btnFechar = (Button) promptsView.findViewById(R.id.buttonFechar);
		btnFechar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				cancel();
			}
		});		
		
	}


}
