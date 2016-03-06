package com.dinoxindustrial.app.agro_central.fragments;

import android.app.Activity;
import android.graphics.Color;
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

import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ParametrosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParametrosFragment extends Fragment implements TextWatcher,View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public final static String SET_HACIENDA = "Distacia hacienda";
    public final static String SET_LOTE = "Distacia lote";
    public final static String SET_CONTRATISTA = "Distacia contratista";
    public final static String SET_OPERADOR = "Distacia opeador";
    public final static String SET_PROFUNDIDAD_DESEADA = "Profundidad deseada";
    public final static String SET_EQ_AGRICOLA = "Profundidad eq Agricola";

    public static final String BTN_REGISTRO = "regiistro";
    public static final String BTN_PARAMETROS_MAQUINA = "PArametros maquibna";


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public final static String ARG_HACIENDA = "Arg hacienda";
    public final static String ARG_LOTE = "Arg lote";
    public final static String ARG_CONTRATISTA = "Arg contratista";
    public final static String ARG_OPERADOR = "Arg opeador";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String hacienda;
    private String lote;
    private String contratista;
    private String operador;

    private TextView txtHacienda;
    private TextView txtLote;
    private TextView txtContratista;
    private TextView txtOperador;

    private RelativeLayout btnParametrosRegistro;
    private RelativeLayout btnParametrosMaquina;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ParametrosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ParametrosFragment newInstance(String mHacienda, String mLote,String mContratista,String mOperador) {
        ParametrosFragment fragment = new ParametrosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_HACIENDA, mHacienda);
        args.putString(ARG_LOTE, mLote);
        args.putString(ARG_OPERADOR, mOperador);
        args.putString(ARG_CONTRATISTA, mContratista);
        fragment.setArguments(args);
        return fragment;
    }

    public ParametrosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Creando Framgment Parametros");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            hacienda = getArguments().getString(ARG_HACIENDA);
            lote = getArguments().getString(ARG_LOTE);
            contratista = getArguments().getString(ARG_CONTRATISTA);
            operador = getArguments().getString(ARG_OPERADOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parametros_2, container, false);

        txtHacienda = (TextView)view.findViewById(R.id.txtParametrosHacienda);
        txtLote = (TextView)view.findViewById(R.id.txtParametrosLote);
        txtContratista = (TextView)view.findViewById(R.id.txtParametrosContratista);
        txtOperador = (TextView)view.findViewById(R.id.txtParametrosOperador);

        txtHacienda.setText(hacienda);
        txtLote.setText(lote);
        txtContratista.setText(contratista);
        txtOperador.setText(operador);

        txtHacienda.addTextChangedListener(this);
        txtLote.addTextChangedListener(this);
        txtContratista.addTextChangedListener(this);
        txtOperador.addTextChangedListener(this);

        btnParametrosRegistro = (RelativeLayout)view.findViewById(R.id.layoutRegistro);
        btnParametrosRegistro.setOnClickListener(this);

        btnParametrosMaquina = (RelativeLayout)view.findViewById(R.id.layoutParametrosMaquina);
        btnParametrosMaquina.setOnClickListener(this);

        ArrayList<String> lista = new ArrayList<String>();
        Spinner spinner1 = (Spinner) view.findViewById(R.id.spinnerProfundidad);
        lista.add("Sencillo");
        lista.add("Profundo");
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_item, lista);

        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adaptador);

        spinner1.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        System.out.println("Spinner1: position=" + position + " id=" + id);
                        String profundidadDeseada = "None";
                        switch (position) {
                            case 0:
                                profundidadDeseada = "Sencillo";
                                break;
                            case 1:
                                profundidadDeseada = "Profundo";
                                break;
                        }
                        Uri uri = Uri.parse(SET_PROFUNDIDAD_DESEADA + ":" + profundidadDeseada);
                        mListener.onFragmentInteraction(uri);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        System.out.println("Spinner1: unselected");
                    }
                });

        ArrayList<String> lista2 = new ArrayList<String>();
        Spinner spinner2 = (Spinner) view.findViewById(R.id.spinnerEqAgricola);
        lista2.add("Tandem");
        lista2.add("Doble");
        lista2.add("Triple");
        ArrayAdapter<String> adaptador2 = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_item, lista2);

        adaptador2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adaptador2);

        spinner2.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        System.out.println("Spinner2: position=" + position + " id=" + id);
                        String profundidadDeseada = "None";
                        switch (position) {
                            case 0:
                                profundidadDeseada = "Tandem";
                                break;
                            case 1:
                                profundidadDeseada = "Doble";
                                break;
                            case 2:
                                profundidadDeseada = "Triple";
                                break;
                        }
                        Uri uri = Uri.parse(SET_EQ_AGRICOLA + ":" + profundidadDeseada);
                        mListener.onFragmentInteraction(uri);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        System.out.println("Spinner1: unselected");
                    }
                });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
        if (txtHacienda.getText().hashCode() == s.hashCode())
        {
            Uri uri = Uri.parse(SET_HACIENDA +":"+ s.toString());
            mListener.onFragmentInteraction(uri);
        }
        else if (txtLote.getText().hashCode() == s.hashCode())
        {
            Uri uri = Uri.parse(SET_LOTE +":"+ s.toString());
            mListener.onFragmentInteraction(uri);
        }
        else if (txtContratista.getText().hashCode() == s.hashCode())
        {
            Uri uri = Uri.parse(SET_CONTRATISTA +":"+ s.toString());
            mListener.onFragmentInteraction(uri);
        }
        else if (txtOperador.getText().hashCode() == s.hashCode())
        {
            Uri uri = Uri.parse(SET_OPERADOR +":"+ s.toString());
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void setOperador(String operador)
    {
        if(txtOperador != null)
        {
            txtOperador.setText(operador);
        }
    }

    @Override
    public void onClick(View v) {
        Uri uri = Uri.parse("");
        switch (v.getId()) {
            case R.id.layoutRegistro:
                System.out.println("Btn Registro");
                uri = Uri.parse(BTN_REGISTRO+":");
                mListener.onFragmentInteraction(uri);
                break;
            case R.id.layoutParametrosMaquina:
                System.out.println("Btn Maquiba");
                uri = Uri.parse(BTN_PARAMETROS_MAQUINA+":");
                mListener.onFragmentInteraction(uri);
                break;
        }
    }
}
