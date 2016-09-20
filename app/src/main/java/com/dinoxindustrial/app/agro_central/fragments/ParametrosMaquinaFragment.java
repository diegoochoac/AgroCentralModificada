package com.dinoxindustrial.app.agro_central.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dinoxindustrial.app.agro_central.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ParametrosMaquinaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ParametrosMaquinaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParametrosMaquinaFragment extends Fragment implements TextWatcher, View.OnClickListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public final static String SET_DISTANCIA_HERRAMIENTA = "Distacia herramienta";
    public final static String SET_DISTANCIA_EJE_SUELO = "Distacia suelo";
    public final static String SET_MAQUINARIA = "Distacia maquinaria";
    public final static String SET_ANCHO_LABOR = "Distacia ancho labor";
    public final static String SET_PROFUNDIDAD_DESEADA = "Profundidad deseada";

    public static final String BTN_REGISTRO_MAQUINA = "Registro desde maquina";
    public static final String BTN_PARAMETROS_REGISTRO = "Parametros registros";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public final static String ARG_DISTANCIA_HERRAMIENTA = "Arg distancia herramienta";
    public final static String ARG_DISTANCIA_EJE_SUELO = "Arg distancia suelo";
    public final static String ARG_MAQUINARIA = "Arg maquinaria";
    public final static String ARG_ANCHO_LABOR = "Arg ancho labor";


    private String mParam1;
    private String mParam2;
    private String distancia_herramienta;
    private String distancia_suelo;
    private String ancho_labor;

    private TextView txtDistanciaHerramienta;
    private TextView txtDistanciaSuelo;
    private TextView txtMaquinaria;
    private TextView txtAnchoLabor;

    private RelativeLayout btnParametrosRegistro;
    private RelativeLayout btnParametrosParamRegistro;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ParametrosMaquinaFragment.
     */
    public static ParametrosMaquinaFragment newInstance(String mDistanciaHerramienta, String mDistanciaSuelo,String mMaquinaria,String mAnchoLabor) {
        ParametrosMaquinaFragment fragment = new ParametrosMaquinaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DISTANCIA_HERRAMIENTA, mDistanciaHerramienta);
        args.putString(ARG_DISTANCIA_EJE_SUELO, mDistanciaSuelo);
        args.putString(ARG_MAQUINARIA, mMaquinaria);
        args.putString(ARG_ANCHO_LABOR, mAnchoLabor);
        fragment.setArguments(args);
        return fragment;
    }

    public ParametrosMaquinaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            distancia_herramienta = getArguments().getString(ARG_DISTANCIA_HERRAMIENTA);
            distancia_suelo = getArguments().getString(ARG_DISTANCIA_EJE_SUELO);
            //maquinaria = getArguments().getString(ARG_MAQUINARIA);
            ancho_labor = getArguments().getString(ARG_ANCHO_LABOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parametros_maquina_2, container, false);

        txtDistanciaHerramienta = (TextView)view.findViewById(R.id.txtParametrosDistanciaHerramienta);
        txtDistanciaSuelo = (TextView)view.findViewById(R.id.txtParametrosDistanciaSuelo);
        txtAnchoLabor = (TextView)view.findViewById(R.id.txtParametrosAnchoLabor);

        txtDistanciaHerramienta.setText(distancia_herramienta);
        txtDistanciaSuelo.setText(distancia_suelo);
        txtAnchoLabor.setText(ancho_labor);

        txtDistanciaHerramienta.addTextChangedListener(this);
        txtDistanciaSuelo.addTextChangedListener(this);
        txtAnchoLabor.addTextChangedListener(this);

        btnParametrosRegistro = (RelativeLayout)view.findViewById(R.id.layoutRegistro_ParamMaquina);
        btnParametrosRegistro.setOnClickListener(this);

        btnParametrosParamRegistro = (RelativeLayout)view.findViewById(R.id.layoutParametrosRegistro);
        btnParametrosParamRegistro.setOnClickListener(this);

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (txtDistanciaHerramienta.getText().hashCode() == s.hashCode())
        {
            try
            {
                double distanceTool = Double.parseDouble(s.toString());
                Uri uri = Uri.parse(SET_DISTANCIA_HERRAMIENTA+":"+distanceTool);
                mListener.onFragmentInteraction(uri);
                //display.setText("New Distance 2: "+distanceGrownd+"\n"+display.getText());
            }
            catch (Exception e)
            {
                //display.setText(e.getMessage()+"\n"+display.getText());
            }
        }
        else if (txtDistanciaSuelo.getText().hashCode() == s.hashCode())
        {
            try
            {
                double distanceTool = Double.parseDouble(s.toString());
                Uri uri = Uri.parse(SET_DISTANCIA_EJE_SUELO+":"+distanceTool);
                mListener.onFragmentInteraction(uri);
                //display.setText("New Distance 2: "+distanceGrownd+"\n"+display.getText());
            }
            catch (Exception e)
            {
                //display.setText(e.getMessage()+"\n"+display.getText());
            }
        }
        else if (txtAnchoLabor.getText().hashCode() == s.hashCode())
        {
            try
            {
                double distanceTool = Double.parseDouble(s.toString());
                Uri uri = Uri.parse(SET_ANCHO_LABOR+":"+distanceTool);
                mListener.onFragmentInteraction(uri);
                //display.setText("New Distance 2: "+distanceGrownd+"\n"+display.getText());
            }
            catch (Exception e)
            {
                //display.setText(e.getMessage()+"\n"+display.getText());
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        Uri uri = Uri.parse("");
        switch (v.getId()) {
            case R.id.layoutRegistro_ParamMaquina:
                System.out.println("Btn Registro");
                uri = Uri.parse(BTN_REGISTRO_MAQUINA+":");
                mListener.onFragmentInteraction(uri);
                break;
            case R.id.layoutParametrosRegistro:
                System.out.println("Btn Maquiba");
                uri = Uri.parse(BTN_PARAMETROS_REGISTRO+":");
                mListener.onFragmentInteraction(uri);
                break;
        }
    }
}
