package com.dinoxindustrial.app.agro_central.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dinoxindustrial.app.agro_central.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistroFragment extends Fragment implements View.OnClickListener{

    public static final String BTN_INIT_REGISTRO = "init registro";
    public static final String BTN_END_REGISTRO = "end registro";
    public static final String BTN_PARAMETROS = "parametros";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView txtRegistroVelocidad;
    private TextView txtRegistroProfundidad;
    private TextView txtRegistroEstado;
    private TextView txtRegistroFecha;
    private TextView txtRegistroIniciarRegistro;
    private ImageView imgRegistro;

    private RelativeLayout btnRegistroIniciarRegistro;
    private RelativeLayout btnRegistroSetCero;
    private RelativeLayout btnRegistroParametros;

    private int stateRegistro;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistroFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistroFragment newInstance(String param1, String param2) {
        RegistroFragment fragment = new RegistroFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RegistroFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Creando Framgment Registro");
        stateRegistro = 0;
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registro_2, container, false);

        txtRegistroVelocidad = (TextView)view.findViewById(R.id.txtRegistroVelocidad);
        txtRegistroProfundidad = (TextView)view.findViewById(R.id.txtRegistroProfundidad);
        txtRegistroEstado = (TextView)view.findViewById(R.id.txtRegistroEstado);
        txtRegistroFecha = (TextView)view.findViewById(R.id.txtRegistroFecha);
        txtRegistroIniciarRegistro = (TextView)view.findViewById(R.id.txtRegistroIniciarRegistro);


        imgRegistro = (ImageView)view.findViewById(R.id.image_registro);

        btnRegistroIniciarRegistro = (RelativeLayout)view.findViewById(R.id.layoutIniciarRegistro);
        btnRegistroIniciarRegistro.setOnClickListener(this);

        btnRegistroSetCero= (RelativeLayout)view.findViewById(R.id.layoutSetCero);
        btnRegistroSetCero.setOnClickListener(this);

        btnRegistroParametros= (RelativeLayout)view.findViewById(R.id.layoutParametros);
        btnRegistroParametros.setOnClickListener(this);

        switch (stateRegistro){
            case 0:
                txtRegistroIniciarRegistro.setText("Pulsa para\nIniciar Registro");
                btnRegistroIniciarRegistro.setBackgroundColor(Color.parseColor("#F75B5B"));
                //imgRegistro.setImageResource(R.drawable.red);
                break;
            case 1:
                txtRegistroIniciarRegistro.setText("Pulsa para\nTerminar Registro");
                btnRegistroIniciarRegistro.setBackgroundColor(Color.parseColor("#65E892"));
                //imgRegistro.setImageResource(R.drawable.green);
                break;
        }
        return view;
    }

    public void update(String nVelocidad,String nProfundidad,String nEstado,String nFecha)
    {
        txtRegistroVelocidad.setText(nVelocidad);
        txtRegistroProfundidad.setText(nProfundidad);
        txtRegistroEstado.setText(nEstado);
        txtRegistroFecha.setText(nFecha);
    }

    public void updateProfundidad(String nProfundidad,String nFecha)
    {
        txtRegistroProfundidad.setText(nProfundidad);
        txtRegistroFecha.setText(nFecha);
    }

    public void updateVelocidad(String nVelocidad,String nFecha)
    {
        txtRegistroVelocidad.setText(nVelocidad);
        txtRegistroFecha.setText(nFecha);
    }

    public void updateEstado(String nEstado)
    {
        txtRegistroEstado.setText(nEstado);
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
    public void onClick(View v) {
        Uri uri = Uri.parse("");
        switch (v.getId()) {
            case R.id.layoutIniciarRegistro:
                switch (stateRegistro)
                {
                    case 0:
                        uri = Uri.parse(BTN_INIT_REGISTRO+":");
                        txtRegistroIniciarRegistro.setText("Pulsa para\nTerminar Registro");
                        btnRegistroIniciarRegistro.setBackgroundColor(Color.parseColor("#65E892"));
                        //imgRegistro.setImageResource(R.drawable.green);
                        stateRegistro = 1;
                        break;
                    case 1:
                        uri = Uri.parse(BTN_END_REGISTRO+":");
                        txtRegistroIniciarRegistro.setText("Pulsa para\nIniciar Registro");
                        btnRegistroIniciarRegistro.setBackgroundColor(Color.parseColor("#F75B5B"));
                        //imgRegistro.setImageResource(R.drawable.red);
                        stateRegistro = 0;
                        break;
                }
                mListener.onFragmentInteraction(uri);
                break;
            case R.id.layoutSetCero:
                System.out.println("Btn SetCero clicked");
                uri = Uri.parse(NodosFragment.BTN_SET_CERO+":");
                mListener.onFragmentInteraction(uri);
                break;
            case R.id.layoutParametros:
                System.out.println("Btn Parametros clicked");
                uri = Uri.parse(BTN_PARAMETROS+":");
                mListener.onFragmentInteraction(uri);
                break;
        }
    }
}
