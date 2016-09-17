package com.dinoxindustrial.app.agro_central.fragments.administrar;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dinoxindustrial.app.agro_central.R;
import com.dinoxindustrial.app.agro_central.basedatos.DatabaseCrud;
import com.dinoxindustrial.app.agro_central.basedatos.terreno.Hacienda;
import com.dinoxindustrial.app.agro_central.basedatos.terreno.Suerte;
import com.dinoxindustrial.app.agro_central.basedatos.terreno.Variedad;
import com.dinoxindustrial.app.agro_central.basedatos.terreno.Zona;
import com.dinoxindustrial.app.agro_central.fragments.OnFragmentInteractionListener;


public class CrearTerreno extends Fragment implements OnClickListener {

    //Base de Datos
    private DatabaseCrud database;


    private EditText codigo, hacienda, suerte, variedad, zona, area;
    private Button Btnagregar,BtnCargar;

    public static final String BTN_CREARTERRENOARCHIVO = "crearTerreArchivo";


    private OnFragmentInteractionListener mListener;

    Context thiscontext;

    public CrearTerreno() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_crearterreno, container, false);
        database = new DatabaseCrud(container.getContext());
        inicializarComponentes(rootview);
        thiscontext = container.getContext();
        return rootview;
    }

    private void inicializarComponentes(final View view) {
        codigo = (EditText)view.findViewById(R.id.cmpCodigo);
        hacienda = (EditText)view.findViewById(R.id.cmpHacienda);
        suerte = (EditText)view.findViewById(R.id.cmpSuerte);
        variedad = (EditText)view.findViewById(R.id.cmpVariedad);
        zona = (EditText)view.findViewById(R.id.cmpZona);
        area = (EditText)view.findViewById(R.id.cmpArea);

        Btnagregar= (Button)view.findViewById(R.id.btnAgregarTerreno);
        Btnagregar.setOnClickListener(this);

        BtnCargar= (Button)view.findViewById(R.id.btnCargarArchivo);
        BtnCargar.setOnClickListener(this);
    }



    public void agregarHacienda(String codigo, String hacienda, Suerte suerte){

        Hacienda nuevo = new Hacienda(codigo,hacienda,suerte);
        database.crearHacienda(nuevo);
    }

    public Suerte agregarSuerte(String nombre, String area, Variedad variedad, Zona zona){

        Suerte nuevo = new Suerte(nombre,area,variedad,zona);
        database.crearSuerte(nuevo);
        return nuevo;
    }

    public Zona agregarZona(String valor){
        Zona nuevo = new Zona(valor);
        database.crearZona(nuevo);
        return nuevo;
    }

    public Variedad agregarVariedad(String valor){
        Variedad nuevo = new Variedad(valor);
        database.crearVariedad(nuevo);
        return nuevo;
    }

    public void CrearTerreno(String codigo,String hacienda, String suerte,String variedad, String zona, String area){
        agregarHacienda(codigo,hacienda,agregarSuerte(suerte, area,agregarVariedad(variedad),agregarZona(zona)));
    }

    public void limpiarCampos(){
        codigo.getText().clear();
        hacienda.getText().clear();
        suerte.getText().clear();
        variedad.getText().clear();
        zona.getText().clear();
        area.getText().clear();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    @Override
    public void onClick(View view) {
        Log.i("CrearTerreno", "onClick");
        Uri uri = Uri.parse("");
        switch (view.getId()) {
            case R.id.btnAgregarTerreno:
                if(codigo.getText().toString().length()>0 && hacienda.getText().toString().length()>0
                        && suerte.getText().toString().length()>0 && variedad.getText().toString().length()>0
                        &&  zona.getText().toString().length()>0 && area.getText().toString().length()>0 ){

                    CrearTerreno(
                            codigo.getText().toString(),
                            hacienda.getText().toString(),
                            suerte.getText().toString(),
                            variedad.getText().toString(),
                            zona.getText().toString(),
                            area.getText().toString()
                    );
                    Toast.makeText(view.getContext(),"Terreno Agregado", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                }else{
                    Toast.makeText(view.getContext(),"Complete la informacion", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnCargarArchivo:
                uri = Uri.parse(BTN_CREARTERRENOARCHIVO+":");
                mListener.onFragmentInteraction(uri);
                break;

        }
    }

}