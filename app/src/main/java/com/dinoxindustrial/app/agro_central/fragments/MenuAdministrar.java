package com.dinoxindustrial.app.agro_central.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;


import com.dinoxindustrial.app.agro_central.R;

import java.sql.SQLException;
import java.util.List;

public class MenuAdministrar extends Fragment implements View.OnClickListener {

    //private DatabaseHelper databaseHelper = null;
    private Button BtnContratista, BtnUsuarios, BtnTerreno;
    private ListView listview;

   /* private Dao<Usuario, Integer> usuarioDao;
    private Dao<Terreno, Integer> terrenoDao;
    private Dao<Contratista, Integer> contratistaDao;

    private List<Usuario> usuarioList;
    private List<Terreno> terrenoList;
    private List<Contratista> contratistaList;

    private UsuarioAdapter adapterUsuario = null;
    private TerrenoAdapter adapterTerreno = null;
    private ContratistaAdapter adapterContratista = null;*/

    Context thiscontext;

    public MenuAdministrar() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_admi, container, false);
        setHasOptionsMenu(true);        //habilitar el action Bar para tener botones
        inicializarComponentes(rootview);
        thiscontext = container.getContext();
        return rootview;
    }

   /* private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }*/

    private void inicializarComponentes(View view) {

        listview = (ListView) view.findViewById(R.id.listViewfragment);
        BtnContratista = (Button)view.findViewById(R.id.btnContratista);
        BtnUsuarios = (Button)view.findViewById(R.id.btnUsuarios);
        BtnTerreno = (Button)view.findViewById(R.id.btnTerreno);
        BtnContratista.setOnClickListener(this);
        BtnUsuarios.setOnClickListener(this);
        BtnTerreno.setOnClickListener(this);

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
                Log.i("MenuFragment3", "btnContratista");
                /*try {
                    contratistaDao =  getHelper().getContratistaDao();
                    contratistaList = contratistaDao.queryForAll();
                    adapterContratista = new ContratistaAdapter(thiscontext, R.layout.row, contratistaList);
                    listview.setAdapter(adapterContratista);
                }catch (SQLException e) {
                    e.printStackTrace();
                }*/
                break;

            case R.id.btnUsuarios:
                Log.i("MenuFragment3", "btnAgregarContratista");
               /* try {
                    usuarioDao =  getHelper().getUsuarioDao();
                    usuarioList = usuarioDao.queryForAll();
                    adapterUsuario = new UsuarioAdapter(thiscontext, R.layout.row, usuarioList);
                    listview.setAdapter(adapterUsuario);
                }catch (SQLException e) {
                    e.printStackTrace();
                }*/
                break;

            case R.id.btnTerreno:
                Log.i("MenuFragment3", "btnTerreno");
                /*try {
                    terrenoDao =  getHelper().getTerrenoDao();
                    terrenoList = terrenoDao.queryForAll();
                    adapterTerreno = new TerrenoAdapter(thiscontext, R.layout.row, terrenoList);
                    listview.setAdapter(adapterTerreno);
                }catch (SQLException e) {
                    e.printStackTrace();
                }*/
                break;
        }
    }


}