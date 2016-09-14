package com.dinoxindustrial.app.agro_central;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.util.Arrays;

/**
 * Created by jjurado on 29/06/15.
 */
public class RegistrationParameters {

    public final static String PARAM_HACIENDA = "Param Hacienda";
    public final static String PARAM_LOTE = "Param Lote";
    public final static String PARAM_CONTRATISTA = "Param Contratista";
    public final static String PARAM_OPERADOR = "Param Operador";
    public final static String PARAM_DISTANCIA_HERRAMIENTA = "Param Distancia Herramienta";
    public final static String PARAM_DISTANCIA_SUELO = "Param Dis SUelo";
    public final static String PARAM_MAQUINA = "Param Maquia";
    public final static String PARAM_ANCHO_LABOR = "Param Ancho Labor";
    public final static String PARAM_PROFUNDIDAD_DESEADA = "Param Profundidad Deseada";
    public final static String PARAM_ANG_CERO = "Param Ang Cero";
    public final static String PARAM_OFFSET_PROFUNDIDAD = "Param Offset profundidad";
    public final static String PARAM_DISTANCE_GROWND = "Param distance grownd";
    public final static String PARAM_DISTANCE_TOOL_TANDEM = "Param distance tool tandem";
    public final static String PARAM_DISTANCE_TOOL_DOBLE = "Param distance tool doble";
    public final static String PARAM_DISTANCE_TOOL_TRIPLE = "Param distance tool tiple";
    public final static String PARAM_CURRENT_TOOL = "Param current tool";

    private Bundle parametros;

    private String hacienda;
    private String lote;
    private String contratista;
    private String operador;

    private String distanciaHerramienta;
    private String distanciaSuelo;
    private String maquina;
    private String anchoLabor;
    private String profundidadDeseada;

    private String distanciaHerramientaTandem;
    private String distanciaHerramientaDoble;
    private String distanciaHerramientaTriple;

    private String angCero;

    private String offsetProfundidad;

    public RegistrationParameters()
    {

    }

    public void loadRegistrationParameters(MainActivity mainActivity)
    {
        SharedPreferences sharedPrefs = mainActivity.getPreferences(Context.MODE_PRIVATE);
        long highScore = sharedPrefs.getInt("Prueba", 0);

        hacienda = sharedPrefs.getString(PARAM_HACIENDA,"Hacienda Prueba");
        lote = sharedPrefs.getString(PARAM_LOTE,"Lote Prueba");
        contratista = sharedPrefs.getString(PARAM_CONTRATISTA,"Contratista Prueba");
        operador = sharedPrefs.getString(PARAM_OPERADOR,"Operador Prueba");

        distanciaHerramienta = sharedPrefs.getString(PARAM_DISTANCIA_HERRAMIENTA,"150");
        distanciaSuelo = sharedPrefs.getString(PARAM_DISTANCIA_SUELO,"40");
        maquina = sharedPrefs.getString(PARAM_MAQUINA,"Maquina Prueba");
        anchoLabor = sharedPrefs.getString(PARAM_ANCHO_LABOR,"2.5");
        profundidadDeseada = sharedPrefs.getString(PARAM_PROFUNDIDAD_DESEADA,"Nose");

        distanciaHerramientaTandem = sharedPrefs.getString(PARAM_DISTANCE_TOOL_TANDEM,"155");
        distanciaHerramientaDoble = sharedPrefs.getString(PARAM_DISTANCE_TOOL_DOBLE,"150");
        distanciaHerramientaTriple = sharedPrefs.getString(PARAM_DISTANCE_TOOL_TRIPLE,"150");

        angCero = sharedPrefs.getString(PARAM_ANG_CERO,"45");

        offsetProfundidad = sharedPrefs.getString(PARAM_OFFSET_PROFUNDIDAD,"0");
    }

    public String[] getParametrosRegister()
    {
        String[] ret = {hacienda,lote,contratista,operador};
        System.out.println("*****Parametros Registro ******");
        for (int i = 0 ; i < ret.length ; i++)
        {
            System.out.println(ret[i]);
        }
        return ret;
    }

    public String getParametrosRegisterJSON()
    {
        String[] ret = {hacienda,lote,contratista,operador};
        System.out.println("*****Parametros Registro ******");
        for (int i = 0 ; i < ret.length ; i++)
        {
            System.out.println(ret[i]);
        }
        JSONArray arr = new JSONArray(Arrays.asList(ret));
        return arr.toString();
    }

    public String[] getParametrosNodos()
    {
        String[] ret = {angCero,offsetProfundidad};
        System.out.println("*****Parametros Nodos ******");
        for (int i = 0 ; i < ret.length ; i++)
        {
            System.out.println(ret[i]);
        }
        return ret;
    }

    public String[] getParametrosMachine()
    {
        String[] ret = {distanciaHerramienta,distanciaSuelo,maquina,anchoLabor,distanciaHerramientaTandem,distanciaHerramientaDoble,distanciaHerramientaTriple};
        System.out.println("*****Parametros Maquina ******");
        for (int i = 0 ; i < ret.length ; i++)
        {
            System.out.println(ret[i]);
        }
        return ret;
    }

    public void saveRegistrationParameter(MainActivity mainActivity,String key_parameter,String value)
    {
        SharedPreferences sharedPref = mainActivity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key_parameter, value);
        editor.commit();
    }

    public void initReport(ReporteProfundidad nReporte)
    {
        nReporte.setHacienda(hacienda);
        nReporte.setLote(lote);
        nReporte.setContratista(contratista);
        nReporte.setOperador(operador);
        nReporte.setMaquina(maquina);
        nReporte.setProfundidadDeseada(profundidadDeseada);
        if(isNumeric(anchoLabor))
        {
            nReporte.setAnchoLabor(Double.parseDouble(anchoLabor));
        }
        else
        {
            nReporte.setAnchoLabor(0);
        }
        nReporte.setProfundidadDeseada("Sencillo");
    }

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
}
