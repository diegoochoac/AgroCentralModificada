package com.dinoxindustrial.app.agro_central.fragments;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dinoxindustrial.app.agro_central.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diego on 14/09/16.
 */
public class EventoFragment extends Fragment implements OnClickListener {

    private Button btnSelecTipoEven, btnIniciarEvento, btnDetenerEvento;
    private ImageView imageHome;

    //BASE DE DATOS
    /*private DatabaseCrud database;
    private List<TipoEvento> tipoEventoList;
    private List<String> tipoEventoListName = new ArrayList<>();
    private ArrayAdapter<String> adapterTipoEvento;*/

    //Variables que se van hacia el MAINACTIVITY
    public final static String SET_EVENTO = "Tipo evento";

    Context thiscontext;
    private OnFragmentInteractionListener mListener;

    public EventoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_evento, container, false);
        //database = new DatabaseCrud(container.getContext());
        inicializarComponentes(rootview);
        thiscontext = container.getContext();
        return rootview;
    }


    private void inicializarComponentes(final View view) {
        btnSelecTipoEven= (Button)view.findViewById(R.id.btnSeleEvento);
        btnSelecTipoEven.setOnClickListener(this);
        btnIniciarEvento = (Button)view.findViewById(R.id.btnInicarEvento);
        btnIniciarEvento.setOnClickListener(this);
        btnDetenerEvento = (Button)view.findViewById(R.id.btnDetenerEvento);
        btnDetenerEvento.setOnClickListener(this);
        imageHome = (ImageView)view.findViewById(R.id.imageHome);
        imageHome.setOnClickListener(this);
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        Uri uri = Uri.parse("");
        switch (view.getId()) {
            case R.id.btnSeleEvento:
                Log.i("EventosFragment", "onClick btnSeleEvento");
               /* tipoEventoList = database.obtenerTipoEvento();
                if(tipoEventoList.size()>0 && tipoEventoList != null){
                    for(int i=0; i<tipoEventoList.size(); i++){
                        tipoEventoListName.add(tipoEventoList.get(i).getTipoEventoName());
                    }
                    adapterTipoEvento = new ArrayAdapter<String>(thiscontext,android.R.layout.simple_list_item_1,tipoEventoListName);
                    AlerDialogListEvento();
                }*/
                break;
            case R.id.imageHome:
                Log.i("EventosFragment", "onClick imageHome");

                break;
            case R.id.btnInicarEvento:
        }
    }

}
