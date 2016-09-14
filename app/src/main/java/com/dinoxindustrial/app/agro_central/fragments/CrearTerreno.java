package com.dinoxindustrial.app.agro_central.fragments;

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


public class CrearTerreno extends Fragment implements OnClickListener {

    //Base de Datos
    //private DatabaseCrud database;


    private EditText codigo, hacienda, suerte, variedad, zona, area;
    private Button Btnagregar;

    public CrearTerreno() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_crearterreno, container, false);
        //database = new DatabaseCrud(container.getContext());
        inicializarComponentes(rootview);
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
    }

    public void agregarTerreno(String codigo, String hacienda, String suerte, String variedad, String zona, String area){
        //Terreno terreno = new Terreno(codigo, hacienda,suerte,variedad,zona,area);
        //database.crearTerreno(terreno);
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
    public void onClick(View view) {
        Log.i("MenuCrearTerreno", "onClick");
        switch (view.getId()) {
            case R.id.btnAgregarTerreno:
                if(codigo.getText().toString().length()>0 && hacienda.getText().toString().length()>0
                        && suerte.getText().toString().length()>0 && variedad.getText().toString().length()>0
                        &&  zona.getText().toString().length()>0 && area.getText().toString().length()>0 ){

                    agregarTerreno(
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

        }
    }
}