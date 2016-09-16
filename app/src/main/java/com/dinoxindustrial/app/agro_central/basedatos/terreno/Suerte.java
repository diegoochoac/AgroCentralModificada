package com.dinoxindustrial.app.agro_central.basedatos.terreno;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by diego on 15/09/16.
 */
public class Suerte {

    public static final String ID = "id";
    public static final String NOMBRE = "nombre";
    public static final String AREA = "area";

    @DatabaseField(generatedId = true, columnName = ID)
    private int id;

    @DatabaseField(columnName = NOMBRE)
    private String nombre;

    @DatabaseField(columnName = AREA)
    private String area;

    // Foreign key defined to hold associations
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public Variedad variedad;

    // Foreign key defined to hold associations
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public Zona zona;

    public Suerte() {
    }

    public Suerte(String nombre, String area, Variedad variedad, Zona zona) {
        this.nombre = nombre;
        this.area = area;
        this.variedad = variedad;
        this.zona = zona;
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Variedad getVariedad() {
        return variedad;
    }

    public void setVariedad(Variedad variedad) {
        this.variedad = variedad;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    //</editor-fold>
}
