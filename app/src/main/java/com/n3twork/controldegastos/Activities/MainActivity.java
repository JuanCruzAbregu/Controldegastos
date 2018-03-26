package com.n3twork.controldegastos.Activities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.n3twork.controldegastos.Adapters.BalanceAdapter;
import com.n3twork.controldegastos.Clases.Balance;
import com.n3twork.controldegastos.DB.DBHelper;
import com.n3twork.controldegastos.R;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ListView listViewBalance;
    FloatingActionButton fabIngreso, fabGasto;

    DBHelper dbHelper;
    SQLiteDatabase db;
    BalanceAdapter cursorAdapter;

    Calendar calendar = Calendar.getInstance();

    String fecha;
    String aux_id = "";
    String aux_monto = "";
    String aux_desc = "";
    String aux_fecha = "";

    String[] opciones = new String[]{"Editar","Borrar"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewBalance = findViewById(R.id.listViewBalance);
        fabIngreso = findViewById(R.id.ingresosFAB);
        fabGasto = findViewById(R.id.gastosFAB);
        capturarFechaActual();
        recuperarTodosLosIngresos();


        fabIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogIngreso("Nuevo Ingreso");
            }
        });

        fabGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogGasto("Nuevo Gasto");
            }
        });
    }

    /**
     * Método que genera un cuadro de diálogo para ingresar un nuevo ingreso.
     *
     * @param title
     */

    public void dialogIngreso(String title) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_agregar_dinero, null);
        builder.setView(viewInflated);

        final TextView textViewTitle = viewInflated.findViewById(R.id.textViewTitle);
        final EditText editTextMonto = viewInflated.findViewById(R.id.editTextMonto);
        final EditText editTextDesc  = viewInflated.findViewById(R.id.editTextDesc);
        textViewTitle.setText(title);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String monto = editTextMonto.getText().toString();
                if (monto.length() > 0) {
                    String desc = editTextDesc.getText().toString();

                    agregarIngreso("$"+monto, desc, fecha);
                    agregarSaldo("$"+monto);
                    recuperarTodosLosIngresos();

                } else {

                    Toast.makeText(getApplicationContext(), "Ingrese un monto", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void agregarSaldo(String monto){



    }

    /**
     * Método que captura un nuevo ingreso para colocarlo en la base de datos.
     *
     * @param monto
     * @param desc
     * @param fecha
     */

    public void agregarIngreso(String monto, String desc, String fecha){
        dbHelper = new DBHelper(this);
        Balance balance = new Balance(monto, desc, fecha);
        dbHelper.addGasto(balance);

    }

    /**
     * Método que genera un cuadro de diálogo para ingresar un nuevo gasto.
     *
     * @param title
     */

    public void dialogGasto(String title){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_agregar_dinero, null);
        builder.setView(viewInflated);

        final TextView textViewTitle = viewInflated.findViewById(R.id.textViewTitle);
        final EditText editTextMonto = viewInflated.findViewById(R.id.editTextMonto);
        final EditText editTextDesc  = viewInflated.findViewById(R.id.editTextDesc);
        textViewTitle.setText(title);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String monto = editTextMonto.getText().toString();
                if (monto.length() > 0) {
                    String desc = editTextDesc.getText().toString();

                    agregarIngreso("$-"+monto, desc, fecha);
                    recuperarTodosLosIngresos();

                } else {

                    Toast.makeText(getApplicationContext(), "Ingrese un monto", Toast.LENGTH_SHORT).show();

                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Método que genera un cuadro de diálogo de lista con las opciones al realizar un LongClick
     *
     * @param opciones
     * @param aux_monto
     * @param aux_id
     * @param aux_desc
     * @param aux_fecha
     */

    public void dialogOpcionesBalance(final String[] opciones, final String aux_monto, final String aux_id,
                                      final String aux_desc, final String aux_fecha){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {

                if(position == 0){

                    dialogEditar("Editar", aux_id, aux_monto, aux_desc, aux_fecha);

                }else if(position == 1){

                    dialogConfirmar("Borrar Historial", "¿Desea borrar el historial? " +
                            "Esto no afectará a los ingresos actuales, gastos y al saldo total", aux_monto, aux_desc, aux_fecha);

                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    /**
     * Método que genera un cuadro de diálogo con los datos a editar del campo seleccionado.
     *
     * @param title
     * @param aux_id
     * @param aux_monto
     * @param aux_desc
     * @param aux_fecha
     */

    public void dialogEditar(String title, final String aux_id, final String aux_monto, final String aux_desc, final String aux_fecha){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_agregar_dinero, null);

        final TextView textViewTitle = viewInflated.findViewById(R.id.textViewTitle);
        final TextView textViewFecha = viewInflated.findViewById(R.id.textViewFecha);
        final EditText editTextMonto = viewInflated.findViewById(R.id.editTextMonto);
        final EditText editTextDesc  = viewInflated.findViewById(R.id.editTextDesc);

        textViewTitle.setText(title);
        editTextMonto.setText(aux_monto);
        editTextDesc.setText(aux_desc);

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String monto = editTextMonto.getText().toString();
                String desc  = editTextDesc.getText().toString();
                String fecha = textViewFecha.getText().toString();

                db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put("monto", monto);
                values.put("descripcion", desc);
                values.put("fecha", fecha);
                db.update("balance", values, "_id='"+aux_id+"'",null);

                recuperarTodosLosIngresos();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    /**
     * Este método genera un cuadro de diálogo para confirmar si el usuario desea borrar
     * de la base de datos el monto seleccionado.
     *
     * @param title
     * @param message
     * @param aux_monto
     * @param aux_desc
     * @param aux_fecha
     */

    public void dialogConfirmar(String title, String message, final String aux_monto, final String aux_desc, final String aux_fecha){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(title !=null){
            builder.setTitle(title);
        }

        if(message != null){
            builder.setMessage(message);
        }

        builder.setCancelable(true);

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    db = dbHelper.getWritableDatabase();
                    db.delete("Pacientes", "monto='" +aux_monto+"' and descripcion='"+aux_desc+"' and fecha='"+aux_fecha+"'", null);
                    recuperarTodosLosIngresos();

                }catch (Exception e){

                    Log.e("Error", "Error: "+e.getMessage());

                }finally {

                    db.close();

                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Método que captura la fecha actual del sistema y la formatea a dd/mm/aaaa.
     *
     */

    public void capturarFechaActual(){

        int year = calendar.get(Calendar.YEAR); //Obtenemos el año
        int month = calendar.get(Calendar.MONTH); //Obtenemos el mes

        //Como los meses van de 0 a 11
        //sumamos 1 a la variable month

        month = month + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH); //Obtenemos el dia
        fecha = day+"/"+month+"/"+year;

    }

    /**
     * Método que recupera todos los datos de la base de datos y los muestra en una lista.
     *
     */

    public void recuperarTodosLosIngresos(){
        try{

            dbHelper = new DBHelper(this);
            //Devuelve todos los ingresos en un objeto cursor.

            Cursor cursor = dbHelper.obtenerTodosBalances();

            String[] from = new String[]{
                    "_id",
                    "monto",
                    "descripcion",
                    "fecha"
            };

            int[] to = new int[]{
                    R.id.textViewID,
                    R.id.textViewMonto,
                    R.id.textViewDesc,
                    R.id.textViewFecha
            };
            cursorAdapter = new BalanceAdapter(this, cursor, from, to, 0);
            listViewBalance.setAdapter(cursorAdapter);
            listViewBalance.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView textoMonto = view.findViewById(R.id.textViewMonto);
                    TextView textoID    = view.findViewById(R.id.textViewID);
                    TextView textoDesc  = view.findViewById(R.id.textViewDesc);
                    TextView textoFecha = view.findViewById(R.id.textViewFecha);

                    aux_monto = textoMonto.getText().toString();
                    aux_id    = textoID.getText().toString();
                    aux_desc  = textoDesc.getText().toString();
                    aux_fecha = textoFecha.getText().toString();

                    dialogOpcionesBalance(opciones, aux_monto, aux_id, aux_desc, aux_fecha);

                    return true;
                }
            });

        }catch (Exception e){

            Log.e("Error","Error: "+e.getMessage());

        }finally {
            dbHelper.cerrar();
        }
    }

}
