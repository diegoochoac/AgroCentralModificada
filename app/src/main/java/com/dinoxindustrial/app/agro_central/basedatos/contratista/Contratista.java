package com.dinoxindustrial.app.agro_central.basedatos.contratista;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by diego on 15/09/16.
 */
public class Contratista {

    public static final String ID = "id";
    public static final String NOMBRE = "nombre";

    @DatabaseField(generatedId = true, columnName = ID)
    private int id;

    @DatabaseField(columnName = NOMBRE)
    private String nombre;

    public Contratista() {
    }

    public Contratista(String nombre) {
        this.nombre = nombre;
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
    //</editor-fold>
}
