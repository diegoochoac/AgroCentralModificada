package com.dinoxindustrial.app.agro_central.fragments.administrar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dinoxindustrial.app.agro_central.R;
import com.dinoxindustrial.app.agro_central.basedatos.DatabaseCrud;
import com.dinoxindustrial.app.agro_central.basedatos.contratista.Contratista;


/**
 * Created by diego on 9/09/16.
 */
public class CrearContratista extends Fragment implements OnClickListener {

    //Base de Datos
    private DatabaseCrud database;

    private EditText nombre;
    private Button Btnagregar;

    public CrearContratista() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_crearcontratista, container, false);
        database = new DatabaseCrud(container.getContext());
        inicializarComponentes(rootview);
        return rootview;
    }

    private void inicializarComponentes(final View view) {
        nombre = (EditText)view.findViewById(R.id.cmpNombreContratista);

        Btnagregar= (Button)view.findViewById(R.id.btnAgregarContratista);
        Btnagregar.setOnClickListener(this);
    }


    public void agregarContratista(String nombre){
        Contratista contratista = new Contratista(nombre);
        database.crearContratista(contratista);
    }

    public void limpiarCampos(){
        nombre.getText().clear();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAgregarContratista:
                if(nombre.getText().toString().length()>0){
                    agregarContratista(
                            nombre.getText().toString()
                    );
                    Toast.makeText(view.getContext(),"Contratista Agregado", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                }else{
                    Toast.makeText(view.getContext(),"Complete la informacion", Toast.LENGTH_SHORT).show();
                    nombre.setFocusable(true);
                }
                break;
        }
    }
}
