package com.dinoxindustrial.app.agro_central.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dinoxindustrial.app.agro_central.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GPSFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GPSFragment extends Fragment implements View.OnClickListener{

    public final static String BTN_GPS_EXTERNO = "btn gps externo";
    public final static String BTN_GPS_INTERNO = "btn gps interno";

    private TextView txtGPSLatitud;
    private TextView txtGPSLongitud;
    private TextView txtGPSAltitud;
    private TextView txtGPSVelocidad;
    private TextView txtGPSPrecision;
    private TextView txtGPSDisplay;
    private TextView txtGPSSerial;
    private TextView txtGPSExterno;

    private RelativeLayout btnGPSExterno;

    private int GPS_selector_state;

    private int contadorDisplay;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GPSFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GPSFragment newInstance(String param1, String param2) {
        GPSFragment fragment = new GPSFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public GPSFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Creando Framgment GPS");
        GPS_selector_state = 0;
        contadorDisplay = 0;
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gps_2, container, false);

        txtGPSLatitud = (TextView)view.findViewById(R.id.txtGPSLatitud);
        txtGPSLongitud = (TextView)view.findViewById(R.id.txtGPSLongitud);
        txtGPSAltitud = (TextView)view.findViewById(R.id.txtGPSAltitud);
        txtGPSVelocidad = (TextView)view.findViewById(R.id.txtGPSVelocidad);
        txtGPSPrecision = (TextView)view.findViewById(R.id.txtGPSPrecision);
        txtGPSDisplay = (TextView)view.findViewById(R.id.txtGPSdisplay);
        txtGPSSerial = (TextView)view.findViewById(R.id.txtSerial);
        txtGPSExterno = (TextView)view.findViewById(R.id.txtGPSExterno);

        btnGPSExterno = (RelativeLayout)view.findViewById(R.id.layoutGPSExterno);
        btnGPSExterno.setOnClickListener(this);

        switch (GPS_selector_state){
            case 0:
                txtGPSExterno.setText("GPS Externo");
                break;
            case 1:
                txtGPSExterno.setText("GPS Interno");
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

    public void update(String nVelocidad)
    {
        txtGPSVelocidad.setText(nVelocidad);

    }

    public void update(String nLatitud,String nLongitud,String nAltitud,String nPrecision)
    {
        txtGPSLatitud.setText(nLatitud);
        txtGPSLongitud.setText(nLongitud);
        txtGPSAltitud.setText(nAltitud);
        txtGPSPrecision.setText(nPrecision);

    }

    public void update(String nLatitud,String nLongitud,String nVelocidad,String nAltitud,String nPrecision)
    {
        txtGPSLatitud.setText(nLatitud);
        txtGPSLongitud.setText(nLongitud);
        txtGPSVelocidad.setText(nVelocidad);
        txtGPSAltitud.setText(nAltitud);
        txtGPSPrecision.setText(nPrecision);

    }

    public void updateMessage(String message)
    {
        if(contadorDisplay == 20) {
            txtGPSDisplay.setText("");
            contadorDisplay = 0;
        }
        txtGPSDisplay.setText(message+txtGPSDisplay.getText());
        contadorDisplay++;
    }

    public void updateSerial(String msg)
    {
        txtGPSSerial.setText(msg);
    }

    @Override
    public void onClick(View v) {
        Uri uri = Uri.parse("");
        switch (v.getId())
        {
            case R.id.btnGPSExterno:
                System.out.println("Btn Change Gps Location");
                switch (GPS_selector_state)
                {
                    case 0:
                        uri = Uri.parse(BTN_GPS_EXTERNO+":");
                        txtGPSExterno.setText("GPS Interno");
                        GPS_selector_state = 1;
                        break;
                    case 1:
                        uri = Uri.parse(BTN_GPS_INTERNO+":");
                        txtGPSExterno.setText("GPS Externo");
                        GPS_selector_state = 0;
                        break;
                }
                mListener.onFragmentInteraction(uri);
                break;
        }
    }
}
