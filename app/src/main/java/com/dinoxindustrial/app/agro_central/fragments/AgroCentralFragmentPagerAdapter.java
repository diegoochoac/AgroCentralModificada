package com.dinoxindustrial.app.agro_central.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dinoxindustrial.app.agro_central.MainActivity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jurado on 15/06/15.
 */
public class AgroCentralFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 6;
    private String tabTitles[] = new String[] { "Parametros","Maquina", "Nodos", "GPS", "Registro","Mapa" };

    private MainActivity context;

    private ParametrosFragment parametrosFragment;
    private GPSFragment gpsFragment;
    private NodosFragment nodosFragment;
    private RegistroFragment registroFragment;
    private MapaFragment mapaFragment;

    private boolean enableConsole;

    public AgroCentralFragmentPagerAdapter(FragmentManager fm, MainActivity context) {
        super(fm);
        this.context = context;
        parametrosFragment = null;
        nodosFragment = null;
        registroFragment = null;

        enableConsole = true;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        System.out.println("Position fragment: "+position);
        switch (position)
        {
            case 0:
                String[] param = context.getRegistrationParameters().getParametrosRegister();
                parametrosFragment = ParametrosFragment.newInstance(param[0],param[1],param[2],param[3]);
                return parametrosFragment;
            case 1:
                String[] paramMachine = context.getRegistrationParameters().getParametrosMachine();
                return ParametrosMaquinaFragment.newInstance(paramMachine[0],paramMachine[1],paramMachine[2],paramMachine[3]);
            case 2:
                String[] paramNodos = context.getRegistrationParameters().getParametrosNodos();
                nodosFragment = NodosFragment.newInstance(paramNodos[0],paramNodos[1]);
                return nodosFragment;
            case 3:
                gpsFragment = GPSFragment.newInstance("GPS","Fragment");
                return gpsFragment;
            case 4:
                registroFragment = RegistroFragment.newInstance("Registro","Fragment");
                return registroFragment;
            case 5:
                mapaFragment = MapaFragment.newInstance("Registro","Fragment");
                return mapaFragment;
        }
        return null;
    }

    public void pruebaBoton()
    {
        if(parametrosFragment != null)
        {
            System.out.println("Parametros Fragment != null");
            parametrosFragment.setOperador("JJ Rendon");
        }
    }

    public void updateGPS(String nVelocidad)
    {
        if(gpsFragment != null)
        {
            gpsFragment.update(nVelocidad);
        }
        if(registroFragment != null)
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            registroFragment.updateVelocidad(nVelocidad, format.format(new Date()));
        }
    }

    public void updateGPS(String nLatitud,String nLongitud,String nAltitud,String nPrecision)
    {
        if(gpsFragment != null)
        {
            gpsFragment.update(nLatitud, nLongitud, nAltitud, nPrecision);
        }
    }

    public void updateGPS(String nLatitud,String nLongitud,String nVelocidad,String nAltitud,String nPrecision)
    {
        if(gpsFragment != null)
        {
            gpsFragment.update(nLatitud, nLongitud, nVelocidad, nAltitud, nPrecision);
        }
        if(registroFragment != null)
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            registroFragment.updateVelocidad(nVelocidad, format.format(new Date()));
        }
    }

    public void updateGPSConsole(String msg)
    {
        if(gpsFragment != null && enableConsole)
        {
            gpsFragment.updateMessage(msg);
        }
    }

    public void updateGPSConsoleUSBSerial(String msg)
    {
        if(gpsFragment != null && enableConsole)
        {
            gpsFragment.updateSerial(msg);
            //enableConsole = false;
        }
    }

    public void updateProfundidad(double nProfundidad,double nAngleTractor,double nAngleImplemento,double nDifAngle,double angCero,double angMedicion)
    {
        if(nodosFragment != null)
        {
            DecimalFormat df = new DecimalFormat("0.0");
            DecimalFormat df_int = new DecimalFormat("0");
            nodosFragment.update(df_int.format(nProfundidad), df.format(nAngleTractor), df.format(nAngleImplemento), df.format(nDifAngle), df.format(angCero), df.format(angMedicion));
        }
        if(registroFragment != null)
        {
            DecimalFormat df = new DecimalFormat("0.0");
            DecimalFormat df_int = new DecimalFormat("0");
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            registroFragment.updateProfundidad(df_int.format(nProfundidad), format.format(new Date()));
        }
    }

    public void updateRegistro(double nVelocidad,double nProfundidad,String nEstado,String fecha)
    {
        if(registroFragment != null)
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            DecimalFormat df = new DecimalFormat("0.0");
            DecimalFormat df_int = new DecimalFormat("0");
            registroFragment.update(df.format(nVelocidad), df_int.format(nProfundidad), nEstado, format.format(new Date()));
        }
    }

    public void updateEstado(String nEstado)
    {
        if(registroFragment != null)
        {
            registroFragment.updateEstado(nEstado);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

    public void addPointToMap(double lat,double lon, double meass)
    {
        if(mapaFragment != null)
            mapaFragment.addDataToMap(lat,lon,meass);
    }
}
