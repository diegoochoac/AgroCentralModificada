package com.dinoxindustrial.app.agro_central.basedatos.terreno;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by diego on 15/09/16.
 */
public class Variedad {

    public static final String ID = "id";
    public static final String VALOR = "valor";


    @DatabaseField(generatedId = true, columnName = ID)
    private int id;

    @DatabaseField(columnName = VALOR)
    private String valor;


    public Variedad() {
    }

    public Variedad(String valor) {
        this.valor = valor;
    }

    //<editor-fold desc="Get Set">
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
    //</editor-fold>
}
