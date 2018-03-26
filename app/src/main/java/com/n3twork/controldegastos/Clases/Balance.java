package com.n3twork.controldegastos.Clases;

/**
 * Created by Juan Cruz Abregú on 13/11/2017.
 *
 * Clase en donde se especifica los atributos y métodos de Balance.
 *
 */

public class Balance {

    private int _id;
    private String monto;
    private String descripcion;
    private String fecha;

    public Balance(){

    }

    public Balance(String monto, String descripcion, String fecha) {
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
