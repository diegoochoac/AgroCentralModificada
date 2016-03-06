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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dinoxindustrial.app.agro_central.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NodosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NodosFragment extends Fragment implements View.OnClickListener,TextWatcher{

    public static final String BTN_SET_CERO = "Set cero";
    public static final String BTN_INIT_COM = "init Com";
    public static final String BTN_END_COM = "end Com";
    public final static String SET_OFFSET_PROFUNDIDAD = "Offset profundidad";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_OFFSET_PROFUNDIDAD = "Arg profundidad";
    private static final String ARG_ANGULO_CERO = "Para Ang cero";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String offset_profundidad;
    private String ang_Cero;


    private TextView txtNodosProfundidad;
    private TextView txtNodosAngTractor;
    private TextView txtNodosAngImplemento;
    private TextView txtNodosAngDiferencia;
    private TextView txtNodosAngCero;
    private TextView txtNodosAngMedicion;
    private TextView txtNodosOffset;

    private Button btnIniciarCom;

    private int stateComSerial;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param nOffsetProfundidad Parameter 1.
     * @return A new instance of fragment NodosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NodosFragment newInstance(String nAngularCero,String nOffsetProfundidad )
    {
        NodosFragment fragment = new NodosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_OFFSET_PROFUNDIDAD, nOffsetProfundidad);
        args.putString(ARG_ANGULO_CERO, nAngularCero);
        fragment.setArguments(args);
        return fragment;
    }

    public NodosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Creando Framgment Nodos");
        stateComSerial = 0;
        if (getArguments() != null) {
            offset_profundidad = getArguments().getString(ARG_OFFSET_PROFUNDIDAD);
            ang_Cero = getArguments().getString(ARG_ANGULO_CERO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("asdf");
        View view = inflater.inflate(R.layout.fragment_nodos, container, false);

        txtNodosProfundidad = (TextView)view.findViewById(R.id.txtNodosProfundidad);

        txtNodosAngTractor = (TextView)view.findViewById(R.id.txtNodosAngTractor);
        txtNodosAngImplemento = (TextView)view.findViewById(R.id.txtNodosAngImplemento);
        txtNodosAngDiferencia = (TextView)view.findViewById(R.id.txtNodosaAngDiferencia);
        txtNodosAngCero = (TextView)view.findViewById(R.id.txtNodosAngCero);
        txtNodosAngMedicion = (TextView)view.findViewById(R.id.txtNodosAngMedicion);

        txtNodosOffset = (EditText)view.findViewById(R.id.txtNodosOffetMedicion);

        txtNodosOffset.setText(offset_profundidad);
        txtNodosAngCero.setText(ang_Cero);

        txtNodosOffset.addTextChangedListener(this);

        Button btnSetCero = (Button)view.findViewById(R.id.btnNodosSetCero);
        btnIniciarCom = (Button)view.findViewById(R.id.btnNodosInitCom);

        btnSetCero.setOnClickListener(this);
        btnIniciarCom.setOnClickListener(this);

        switch (stateComSerial){
            case 0:
                btnIniciarCom.setText("Iniciar Com");
                break;
            case 1:
                btnIniciarCom.setText("Terminar Com");
                break;
        }

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
    public void onClick(View v) {
        Uri uri = Uri.parse("");
        switch (v.getId())
        {
            case R.id.btnNodosSetCero:
                System.out.println("Btn SetCero clicked");
                uri = Uri.parse(BTN_SET_CERO+":");
                mListener.onFragmentInteraction(uri);
                break;
            case R.id.btnNodosInitCom:
                System.out.println("Btn SetCero clicked");
                switch (stateComSerial)
                {
                    case 0:
                        btnIniciarCom.setText("Terminar Com");
                        uri = Uri.parse(BTN_INIT_COM+":");
                        stateComSerial = 1;
                        break;
                    case 1:
                        btnIniciarCom.setText("Iniciar Com");
                        uri = Uri.parse(BTN_END_COM+":");
                        stateComSerial = 0;
                        break;
                }
                mListener.onFragmentInteraction(uri);
                break;
        }

    }

    public void update(String nProfundidad,String nAngTractor,String nAngImplemento,String nDifAngulos,String nAngCero,String nAngMedicion)
    {
        txtNodosProfundidad.setText(nProfundidad);
        txtNodosAngTractor.setText(nAngTractor);
        txtNodosAngImplemento.setText(nAngImplemento);
        txtNodosAngDiferencia.setText(nDifAngulos);
        txtNodosAngCero.setText(nAngCero);
        txtNodosAngMedicion.setText(nAngMedicion);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (txtNodosOffset.getText().hashCode() == s.hashCode())
        {
            Uri uri = Uri.parse(SET_OFFSET_PROFUNDIDAD +":"+ s.toString());
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
