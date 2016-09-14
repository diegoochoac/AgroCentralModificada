package com.dinoxindustrial.app.agro_central.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.dinoxindustrial.app.agro_central.R;

import java.util.List;

public class CrearUsuario extends Fragment implements OnClickListener {

    //Base de Datos
    //private DatabaseCrud database;
    //private Dao<Contratista, Integer> contratistaDao;
    //private List<Contratista> contratistaList;
    //private ContratistaAdapter adapterContratista = null;

    private EditText nombre, telefono, labor;
    private ImageView foto;
    private Button Btnagregar;
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
        //database = new DatabaseCrud(container.getContext());
        inicializarComponentes(rootview);
        thiscontext = container.getContext();
        return rootview;
    }


    private void inicializarComponentes(final View view) {
        nombre = (EditText)view.findViewById(R.id.cmpNombre);
        telefono = (EditText)view.findViewById(R.id.cmpTelefono);
        labor = (EditText)view.findViewById(R.id.cmpLabor);
        contratista = (TextView)view.findViewById(R.id.cmpAgreContratista);
        contratista.setOnClickListener(this);

        foto = (ImageView)view.findViewById(R.id.imageView);
        foto.setOnClickListener(this);

        Btnagregar= (Button)view.findViewById(R.id.btnAgregarUsuario);
        Btnagregar.setOnClickListener(this);
    }

   /* private void AlerDialogListContratista() {

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
                    adapterContratista = new ContratistaAdapter(thiscontext, R.layout.row, contratistaList);
                    listview.setAdapter(adapterContratista);
                }
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                select= adapterContratista.getItem(position);
                contratista.setText(adapterContratista.getItem(position).getContratistaName().toString());
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

    public void agregarUsuario(String name, String specialty, String phoneNumber, String avatarUri, Contratista contratista){
        //Usuario usuario = new Usuario(name, specialty,phoneNumber,avatarUri,contratista);
        //database.crearUsuario(usuario);
    }*/

    public void limpiarCampos(){
        nombre.getText().clear();
        labor.getText().clear();
        telefono.getText().clear();
        foto.setImageResource(R.drawable.ic_user);
        contratista.setText("Presione para seleccionar contratista");
        nombre.requestFocus();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode== Activity.RESULT_OK && requestCode == request_code){
            foto.setImageURI(data.getData());
            foto.setTag(data.getData());
        }
    }

    @Override
    public void onClick(View view) {
        Log.i("MenuCrearUsuario", "onClick");
        switch (view.getId()) {
            case R.id.cmpAgreContratista:
               /* contratistaList = database.obtenerContratistas();
                if(contratistaList.size()>0 && contratistaList != null){
                    adapterContratista = new ContratistaAdapter(thiscontext, R.layout.row, contratistaList);
                    AlerDialogListContratista();
                }*/
                break;

            case R.id.btnAgregarUsuario:
                if(nombre.getText().toString().length()>0 && labor.getText().toString().length()>0
                        && telefono.getText().toString().length()>0 && select !=null){

                   /* agregarUsuario(
                            nombre.getText().toString(),
                            labor.getText().toString(),
                            telefono.getText().toString(),
                            String.valueOf(foto.getTag()),
                            (Contratista) select
                    );*/
                    Toast.makeText(view.getContext(),"Usuario Agregado", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                }else{
                    Toast.makeText(view.getContext(),"Complete la informacion", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.imageView:
                Intent intent=null;
                if(Build.VERSION.SDK_INT<19){
                    intent= new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                }else{
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                }
                intent.setType("image/*");
                startActivityForResult(intent,request_code);
                break;
        }
    }

}