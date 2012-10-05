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
 * Classe que extende AlertDialog que tem como finalidade a cria��o de uma caixa de di�logo que mostra campos a serem preenchidos 
 * com as caracter�sticas de um novo agendamento/consulta
 */
public class AgendaConsultaDialog extends AlertDialog {

	private Context context;
	private View promptsView;
	private Button btnAlterarData;
	private int year;
	private int month;
	private int day;
	private Button btnAlterarHora;
	private int hour;
	private int minute;
	private Button btnSalvar;
	private Button btnCancelar;
	private EditText editTextData;
	private EditText editTextHora;	
	private EditText editTextPaciente;
	private EditText editTextAgente;
	private EditText editTextPosto;
	private EditText editTextDescricao;
	private AgenteSaude agenteSaude;
	private AgendamentoService agendamentoService;
	private LayoutInflater layoutInflater;
	private SQLiteDatabase sQLiteDatabase;
	
	/**
	 * M�todo construtor que inicializa todos os componentes da interface do usu�rio mostrando os campos a serem preenchidos
	 * Tamb�m � feita a inicializa��o de m�todos para gerenciamento de listners e de inser��o de dados
	 * O par�metro do tipo AgenteSaude � um objeto que vem justamente trazendo dados oriundos do login do usu�rio para
	 *  preencher os campos Agente de sa�de e posto de sa�de
	 * @param Context context
	 * @param AgenteSaude agenteSaude
	 * @param SQLiteDatabase sQLiteDatabase
	 */
	protected AgendaConsultaDialog(Context context,  SQLiteDatabase sQLiteDatabase, AgenteSaude agenteSaude) {
		super(context);

		this.agendamentoService = new AgendamentoService(sQLiteDatabase);
		this.context = context;
		this.agenteSaude = agenteSaude;
		this.sQLiteDatabase = sQLiteDatabase;
		
		setTitle("AGENDAR CONSULTA");
		setIcon(R.drawable.ic_launcher);
		layoutInflater = LayoutInflater.from(context);
		promptsView = layoutInflater.inflate(R.layout.addconsultas, null);
		setView(promptsView);
		
		setDataAtual();
		setaDadosForm();
		
		gerenciarListners();	
	}
	
	/**
	 * M�todo que recupera os campos criados em xml atrav�s de 'ids' e os inicializa para manipula��es de dados nesses campos
	 * Posteriormente ocorre a inser��o "for�ada" de dados (recuperados do objeto agenteSaude) nos campos Agente de sa�de e posto de sa�de 
	 * do formul�rio, frizando que ambos campos n�o podem ser editados pois no xml cont�m a instru��o "android:editable="false""
	 */
	private void setaDadosForm(){
		
		editTextAgente = (EditText) promptsView.findViewById(R.id.edTextAgente);
		editTextPosto = (EditText) promptsView.findViewById(R.id.editTextPosto);
		editTextAgente.setText(agenteSaude.getNome());
		editTextPosto.setText(agenteSaude.getPosto().getNome());
	}
	
	/**
	 * M�todo que encapsula a inicializa��o e gerenciamento de bot�es (chamada do componente DatePickerDialog, chamada do componente 
	 * TimePickerDialog, salvar, cancelar ) presentes na caixa de di�logo
	 */
	private void gerenciarListners(){
		
		btnAlterarData = (Button) promptsView.findViewById(R.id.btnChangeDate);
		btnAlterarData.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				new DatePickerDialog(AgendaConsultaDialog.this.context, datePickerListener, year, month, day).show();
			}
		});		
		
		btnAlterarHora = (Button) promptsView.findViewById(R.id.btnChangeTime);
		btnAlterarHora.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				new TimePickerDialog(AgendaConsultaDialog.this.context, timePickerListener, hour, minute,false).show();
			}
		});
		
		
		btnSalvar = (Button) promptsView.findViewById(R.id.btnSalvar);
		btnSalvar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				editTextPaciente = (EditText) promptsView.findViewById(R.id.edTextPaciente);
				editTextDescricao = (EditText) promptsView.findViewById(R.id.editTextDescricao); 
				editTextData = (EditText) promptsView.findViewById(R.id.editTextData);
				editTextHora = (EditText) promptsView.findViewById(R.id.editTextHora);
				String nomePac = editTextPaciente.getText().toString();
				String desc = editTextDescricao.getText().toString();
				String data = editTextData.getText().toString();
				String hora = editTextHora.getText().toString();
				Agendamento novoAgend = new Agendamento(nomePac, desc, data, hora, agenteSaude, agenteSaude.getPosto());
				if ( agendamentoService.inserir(novoAgend, context) ){
					
					cancel();
					AgendamentoConcluidoDialog agConcluido = new AgendamentoConcluidoDialog(AgendaConsultaDialog.this.context, AgendaConsultaDialog.this.sQLiteDatabase, novoAgend);
					agConcluido.show();
					
				}
			}
		});


		btnCancelar = (Button) promptsView.findViewById(R.id.btnCancelar);
		btnCancelar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				cancel();
			}
		});
		
	}
	
	/**
	 * M�todo que inicializa a classe 'Calendar' e para atribui��o de valores referentes ano, m�s e dia (atual) 
	 * aos seus respectivos atributos globais
	 */
	public void setDataAtual() {
			
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
	 }

	/**
	 * M�todo que extende AlertDialog, faz a cria��o de um objeto do tipo OnDateSetListener.
	 * Implementa sua assinatura/m�todo que faz as seguintes a��es:
	 * - obt�m a atribui as vari�veis globais dia/mes e ano selecionados no componente
	 * - recupera e inicializa o objeto do tipo EditText posteriormente seta os dados (data selecionada) que foram selecionados pelo 
	 * usu�rio (principal utilidade do componente neste contexto)
	 */
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

			// when dialog box is closed, below method will be called.
			public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
				year = selectedYear;
				month = selectedMonth;
				day = selectedDay;
				

				editTextData = (EditText) promptsView.findViewById(R.id.editTextData);
				editTextData.setText(new StringBuilder()
				// Month is 0 based, just add 1
				.append(day).append("-").append(month + 1).append("-").append(year).append(" "));
			}
	 };

	 /**
	 * M�todo que extende AlertDialog, faz a cria��o de um objeto do tipo OnDateSetListener.
	 * Implementa sua assinatura/m�todo que faz as seguintes a��es:
	 * - obt�m a atribui as vari�veis globais hora e minutos selecionados no componente
	 * - recupera e inicializa o objeto do tipo EditText posteriormente seta os dados (horario selecionado) que foram selecionados pelo 
	 * usu�rio (principal utilidade do componente neste contexto)
	 */
	 private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
			
		 public void onTimeSet(TimePicker view, int selectedHour,int selectedMinute) {

				hour = selectedHour;
				minute = selectedMinute;
				editTextHora = (EditText) promptsView.findViewById(R.id.editTextHora);
				editTextHora.setText(new StringBuilder().append(pad(hour)).append(":").append(pad(minute)) );
		 }
	};
	 
	/**
	 * M�todo interno que insere o 0 (zero) em todos os n�meros menores de 10
	 * @param int c
	 * @return String
	 */
	private static String pad(int c) {
		
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}



}
