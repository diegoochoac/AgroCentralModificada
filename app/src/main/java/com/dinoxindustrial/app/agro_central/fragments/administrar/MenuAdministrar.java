package com.dinoxindustrial.app.agro_central.fragments.administrar;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.dinoxindustrial.app.agro_central.R;
import com.dinoxindustrial.app.agro_central.basedatos.DatabaseCrud;
import com.dinoxindustrial.app.agro_central.basedatos.contratista.Contratista;
import com.dinoxindustrial.app.agro_central.basedatos.contratista.Usuario;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuAdministrar extends Fragment implements View.OnClickListener {

    //private DatabaseHelper databaseHelper = null;
    private Button BtnContratista, BtnUsuarios, BtnTerreno, BtnMenu;
    private ListView listview;

    private DatabaseCrud database;
    private Dao<Usuario, Integer> usuarioDao;
    //private Dao<Terreno, Integer> terrenoDao;
    private Dao<Contratista, Integer> contratistaDao;

    //private List<Terreno> terrenoList;

    private List<Usuario> usuarioList;
    private List<String> usuarioListName = new ArrayList<>();
    private ArrayAdapter<String> adapterUsuario;

    private List<Contratista> contratistaList;
    private List<String> contratistaListName = new ArrayList<>();
    private ArrayAdapter<String> adapterContratista;

    Context thiscontext;

    public MenuAdministrar() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_admi, container, false);
        database = new DatabaseCrud(container.getContext());
        inicializarComponentes(rootview);
        thiscontext = container.getContext();
        return rootview;
    }

    private void inicializarComponentes(View view) {

        listview = (ListView) view.findViewById(R.id.listViewfragment);
        BtnContratista = (Button)view.findViewById(R.id.btnContratista);
        BtnUsuarios = (Button)view.findViewById(R.id.btnUsuarios);
        BtnTerreno = (Button)view.findViewById(R.id.btnTerreno);
        BtnContratista.setOnClickListener(this);
        BtnUsuarios.setOnClickListener(this);
        BtnTerreno.setOnClickListener(this);

        BtnMenu = (Button)view.findViewById(R.id.btnMenu);
        BtnMenu.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnContratista:
                Log.i("MenuAdministrar", "btnContratista");
                contratistaList = database.obtenerContratistas();
                if(contratistaList.size()>0 && contratistaList != null) {
                    contratistaListName.clear();
                    for (int i = 0; i < contratistaList.size(); i++) {
                        contratistaListName.add(contratistaList.get(i).getNombre());
                    }
                    adapterContratista = new ArrayAdapter<String>(thiscontext,android.R.layout.simple_list_item_1,contratistaListName);
                    listview.setAdapter(adapterContratista);
                }else{
                    Toast.makeText(view.getContext(),"No se encuentran registros", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnUsuarios:
                Log.i("MenuAdministrar", "btnAgregarContratista");
                usuarioList = database.obtenerUsuarios();
                if(usuarioList.size()>0 && usuarioList != null) {
                    usuarioListName.clear();
                    for (int i = 0; i < usuarioList.size(); i++) {
                        usuarioListName.add(usuarioList.get(i).getNombre());
                    }
                    adapterUsuario = new ArrayAdapter<String>(thiscontext,android.R.layout.simple_list_item_1,usuarioListName);
                    listview.setAdapter(adapterUsuario);
                }else{
                    Toast.makeText(view.getContext(),"No se encuentran registros", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnTerreno:
                Log.i("MenuAdministrar", "btnTerreno");
                /*try {
                    terrenoDao =  getHelper().getTerrenoDao();
                    terrenoList = terrenoDao.queryForAll();
                    adapterTerreno = new TerrenoAdapter(thiscontext, R.layout.row, terrenoList);
                    listview.setAdapter(adapterTerreno);
                }catch (SQLException e) {
                    e.printStackTrace();
                }*/
                break;

            case R.id.btnMenu:
                Log.i("MenuAdministrar", "btnMenu");
                getActivity().onBackPressed();
                break;
        }
    }


}