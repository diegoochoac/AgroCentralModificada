package com.dinoxindustrial.app.agro_central.fragments.administrar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dinoxindustrial.app.agro_central.R;
import com.dinoxindustrial.app.agro_central.basedatos.DatabaseCrud;
import com.dinoxindustrial.app.agro_central.basedatos.contratista.Contratista;
import com.dinoxindustrial.app.agro_central.basedatos.contratista.Usuario;
import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.List;

public class CrearUsuario extends Fragment implements OnClickListener {

    //Base de Datos
    private DatabaseCrud database;
    private Dao<Contratista, Integer> contratistaDao;
    private List<Contratista> contratistaList;
    private List<String> contratistaListName = new ArrayList<>();
    private ArrayAdapter<String> adapterContratista;

    private EditText nombre;
    private Button Btnagregar, BtnMenu;
    private TextView contratista;
    Object select= null;
    private int request_code = 1;
    Context thiscontext;

    public CrearUsuario() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_crearusuario, container, false);
        database = new DatabaseCrud(container.getContext());
        inicializarComponentes(rootview);
        thiscontext = container.getContext();
        return rootview;
    }


    private void inicializarComponentes(final View view) {
        nombre = (EditText)view.findViewById(R.id.cmpNombre);
        contratista = (TextView)view.findViewById(R.id.cmpAgreContratista);
        contratista.setOnClickListener(this);

        Btnagregar= (Button)view.findViewById(R.id.btnAgregarUsuario);
        Btnagregar.setOnClickListener(this);

        BtnMenu = (Button)view.findViewById(R.id.btnMenu);
        BtnMenu.setOnClickListener(this);
    }

    private void AlerDialogListContratista() {

        final AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(thiscontext);

        LinearLayout layout= new LinearLayout(thiscontext);
        final TextView Message        = new TextView(thiscontext);
        final EditText editText = new EditText(thiscontext);
        final ListView listview = new ListView(thiscontext);

        Message.setText("Ingrese busqueda:");
        editText.setSingleLine();
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(Message);
        layout.addView(editText);
        layout.addView(listview);
        alertdialogbuilder.setTitle("Por favor seleccione");
        alertdialogbuilder.setView(layout);

        listview.setAdapter(adapterContratista);
        final AlertDialog alert = alertdialogbuilder.create();

        editText.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s){

            }
            public void beforeTextChanged(CharSequence s,
                                          int start, int count, int after){

            }
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String text = editText.getText().toString().toLowerCase().trim();
                contratistaList = database.obtenerContratistaAutocompletar(Contratista.NOMBRE,text);
                if(contratistaList.size()>0 && contratistaList != null){
                    for(int i=0; i<contratistaList.size(); i++){
                        contratistaListName.add(contratistaList.get(i).getNombre());
                        Log.i("EventosFragment", "valores"+contratistaList.get(i).getNombre());
                    }
                    adapterContratista = new ArrayAdapter<String>(thiscontext,android.R.layout.simple_list_item_1,contratistaListName);
                    listview.setAdapter(adapterContratista);
                }
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                select= adapterContratista.getItem(position);
                contratista.setText(select.toString());
                alert.cancel();
            }
        });

        alertdialogbuilder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
    }

    public void agregarUsuario(String name, String contratista){
        Log.i("Crear Usuario","Contratista: "+database.obtenerContratista(contratista));
        Usuario usuario = new Usuario(name,database.obtenerContratista(contratista));
        database.crearUsuario(usuario);
    }

    public void limpiarCampos(){
        nombre.getText().clear();
        contratista.setText("Presione para seleccionar contratista");
        nombre.requestFocus();
    }

    @Override
    public void onClick(View view) {
        Log.i("MenuCrearUsuario", "onClick");
        switch (view.getId()) {
            case R.id.cmpAgreContratista:
                contratistaList = database.obtenerContratistas();
                if(contratistaList.size()>0 && contratistaList != null){
                    contratistaListName.clear();
                    for(int i=0; i<contratistaList.size(); i++){
                        contratistaListName.add(contratistaList.get(i).getNombre());
                    }
                    adapterContratista = new ArrayAdapter<String>(thiscontext,android.R.layout.simple_list_item_1,contratistaListName);
                    AlerDialogListContratista();
                }else{
                    Toast.makeText(view.getContext(),"No se encuentran registros", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnAgregarUsuario:
                if(nombre.getText().toString().length()>0 && select !=null){

                    agregarUsuario(
                            nombre.getText().toString(),
                            (String)select
                    );
                    Toast.makeText(view.getContext(),"Usuario Agregado", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                }else{
                    Toast.makeText(view.getContext(),"Complete la informacion", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.btnMenu:
                Log.i("CrearUsuario", "onClick regresar Menu");
                getActivity().onBackPressed();
                break;
        }
    }

}