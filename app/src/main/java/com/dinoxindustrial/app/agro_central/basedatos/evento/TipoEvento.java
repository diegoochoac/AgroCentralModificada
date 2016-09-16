package com.dinoxindustrial.app.agro_central.basedatos.evento;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by diego on 12/09/16.
 */
public class TipoEvento implements Serializable {

    public static final String ID = "id";
    public static final String NOMBRE = "nombre";

    @DatabaseField(generatedId = true, columnName = ID)
    private int id;

    @DatabaseField(columnName = NOMBRE)
    private String nombre;

    public TipoEvento() {
    }

    public TipoEvento(final String tipoEventoName) {
        this.nombre = tipoEventoName;
    }

    //<editor-fold desc="Metodos Get-Set">
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
