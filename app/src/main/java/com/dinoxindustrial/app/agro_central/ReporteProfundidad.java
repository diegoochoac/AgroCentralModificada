package com.dinoxindustrial.app.agro_central;

import com.loopj.android.http.RequestParams;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jurado on 27/05/15.
 */
public class ReporteProfundidad
{
    private String contratista;
    private String operador;
    private String hacienda;
    private String lote;
    private String maquina;
    private String profundidadDeseada;
    private double anchoLabor;
    private double latitud;
    private double longitud;
    private double velocidad;
    private double altitud;
    private double precision;
    private double profundidad;

    private String state;

    public ReporteProfundidad()
    {
        contratista = "JPJS";
        operador = "Juan Jurado";
        hacienda = "Hacienda Prueba";
        lote = "Lote Prueba";
        maquina = "Maquina Prueba";
        profundidadDeseada = "Profundo";
        anchoLabor = 0;
        longitud = 0;
        latitud = 0;
        velocidad = 0;
        precision = 0;
        profundidad = 0;
        altitud = 0;

        state = "";
    }

    public String darEncabezado()
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        String encabezado = "Fecha = "+format.format(new Date())+"\n";
        encabezado+="Contratista = "+contratista+"\n";
        encabezado+="Operador = "+operador+"\n";
        encabezado+="Hacienda = "+hacienda+"\n";
        encabezado+="Lote = "+lote+"\n";
        encabezado+="Maquina = "+maquina+"\n";
        encabezado+="AnchoLabor = "+anchoLabor+"\n";
        encabezado+="ProfundidadDeseada = "+profundidadDeseada+"\n";
        encabezado+="Estado;Latitud;Longitud;Fecha;Velocidad[Km/h];Altura[m];HDOP;Profundidad[cm]";
        return encabezado;
    }

    public String darMedicion()
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        DecimalFormat df = new DecimalFormat("0.0000000");
        DecimalFormat df1 = new DecimalFormat("0.0");

        String medicion = getEstado()+";";
        medicion+=df.format(latitud)+";";
        medicion+=df.format(longitud)+";";
        medicion+=format.format(new Date())+";";
        medicion+=df1.format(velocidad)+";";
        medicion+=df1.format(altitud)+";";
        medicion+=df1.format(precision)+";";
        medicion+=df1.format(profundidad)+";";
        medicion = medicion.replace(",",".");
        return medicion;
    }


    public String getContratista() {
        return contratista;
    }

    public void setContratista(String contratista) {
        this.contratista = contratista;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public String getHacienda() {
        return hacienda;
    }

    public void setHacienda(String hacienda) {
        this.hacienda = hacienda;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getMaquina() {
        return maquina;
    }

    public void setMaquina(String maquina) {
        this.maquina = maquina;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(double velocidad) {
        this.velocidad = velocidad;
    }

    public double getAltitud() {
        return altitud;
    }

    public void setAltitud(double altitud) {
        this.altitud = altitud;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public double getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(double profundidad) {
        this.profundidad = profundidad;
    }

    public double getAnchoLabor() {
        return anchoLabor;
    }

    public void setAnchoLabor(double anchoLabor) {
        this.anchoLabor = anchoLabor;
    }

    public String getProfundidadDeseada() {
        return profundidadDeseada;
    }

    public void setProfundidadDeseada(String profundidadDeseada) {
        this.profundidadDeseada = profundidadDeseada;
    }

    public void setParametrosRegistro(String[] params)
    {
        hacienda = params[0];
        lote = params[1];
        contratista = params[2];
        operador = params[3];
    }

    public void setParametrosMachine(String[] params)
    {
        maquina = params[2];
        anchoLabor = Double.parseDouble(params[3]);
    }

    public String getEstado()
    {
        state = "";
        if(velocidad <= 2)
            state = "ST";
        else if(profundidad < 20)
            state = "TR";
        else
            state = "WK";
        return  state;
    }

    public RequestParams getRequestParamsRegister()
    {
        RequestParams params = new RequestParams();

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        DecimalFormat df = new DecimalFormat("0.0000000");
        DecimalFormat df1 = new DecimalFormat("0.0");

        params.put("wk_fecha",format.format(new Date()));
        params.put("wk_contratista",contratista);
        params.put("wk_operador",operador);
        params.put("wk_hacienda",hacienda);
        params.put("wk_lote",lote);
        params.put("wk_maquinaria",maquina);
        params.put("wk_anchoLabor",anchoLabor);
        params.put("wk_profundidaddeseada",profundidadDeseada);
        params.put("wk_maquina", "56561797f26490c76074198a");
        params.put("dt_state",getEstado());
        params.put("dt_latitude",""+latitud);
        params.put("dt_longitude",""+longitud);
        params.put("dt_date",format.format(new Date()));
        params.put("dt_speed",df1.format(velocidad));
        params.put("dt_altitude",df1.format(altitud));
        params.put("dt_accuracy",df1.format(precision));
        params.put("dt_depth",df1.format(profundidad));

        return params;
    }
}
