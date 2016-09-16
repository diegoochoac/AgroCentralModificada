package com.dinoxindustrial.app.agro_central.basedatos.terreno;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by diego on 15/09/16.
 */
public class Hacienda {

    public static final String ID = "id";
    public static final String CODIGO = "codigo";
    public static final String NOMBRE = "nombre";


    @DatabaseField(generatedId = true, columnName = ID)
    private int id;

    @DatabaseField(columnName = CODIGO)
    private String codigo;

    @DatabaseField(columnName = NOMBRE)
    private String nombre;

    // Foreign key defined to hold associations
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public Suerte suerte;


    public Hacienda() {
    }

    public Hacienda(String nombre, String codigo, Suerte suerte) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.suerte = suerte;
    }

    //<editor-fold desc="Get- Set">
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Suerte getSuerte() {
        return suerte;
    }

    public void setSuerte(Suerte suerte) {
        this.suerte = suerte;
    }

    //</editor-fold>
}
