package com.n3twork.controldegastos.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.n3twork.controldegastos.Clases.Balance;

/**
 * Created by Juan Cruz Abregú on 13/11/2017.
 *
 * Base de datos.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "gastos.db";
    public static final String TABLA_BALANCE = "balance";
    public static final String TABLA_TOTAL = "totalidad";
    public static final String COLUMNA_ID = "_id";
    public static final String COLUMNA_MONTO = "monto";
    public static final String COLUMNA_DESCRIPCION = "descripcion";
    public static final String COLUMNA_FECHA = "fecha";
    public static final String COLUMNA_IDENT = "_id";
    public static final String COLUMNA_INGRESO = "ingreso";
    public static final String COLUMNA_GASTO = "gasto";
    public static final String COLUMNA_TOTAL = "total";

    public String sql = "CREATE TABLE " + TABLA_BALANCE + "(" +
            COLUMNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMNA_MONTO + " TEXT NOT NULL, " +
            COLUMNA_DESCRIPCION + " TEXT NOT NULL, " +
            COLUMNA_FECHA + " TEXT" +
            ");";

    public String sql2 = "CREATE TABLE " + TABLA_TOTAL + "(" +
            COLUMNA_IDENT + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMNA_INGRESO + " TEXT, " +
            COLUMNA_GASTO + " TEXT, " +
            COLUMNA_TOTAL + " TEXT NOT NULL" +
            ");";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql);
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_BALANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_TOTAL);
        db.execSQL(sql);
        db.execSQL(sql2);
    }

    public void addSaldo(){

    }

    /**
     * Método que inserta registros en la tabla balance.
     *
     * @param balance
     */

    public void addGasto(Balance balance){

        ContentValues values = new ContentValues();
        values.put(COLUMNA_MONTO, balance.getMonto());
        values.put(COLUMNA_DESCRIPCION, balance.getDescripcion());
        values.put(COLUMNA_FECHA, balance.getFecha());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLA_BALANCE, null, values);
        db.close();

    }

    /**
     * Método que devuelve todos los balances.
     *
     * @return
     */

    public Cursor obtenerTodosBalances(){

        String[] columnas = new String[]{COLUMNA_ID, COLUMNA_MONTO, COLUMNA_DESCRIPCION, COLUMNA_FECHA};
        Cursor cursor = this.getReadableDatabase().query(TABLA_BALANCE, columnas, null, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    /**
     * Método que cierra la base de datos.
     *
     */

    public void cerrar(){
        this.close();
    }
}
