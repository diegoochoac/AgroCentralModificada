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
import android.widget.LinearLayout;

import com.dinoxindustrial.app.agro_central.R;

/**
 * Created by diego on 14/09/16.
 */
public class MenuFragment extends Fragment implements OnClickListener{

    LinearLayout labor, evento, administrar;

    //Variables que se van hacia el MAINACTIVITY
    public static final String BTN_LABOR = "labor";
    public static final String BTN_EVENTO = "evento";
    public static final String BTN_ADMINISTRAR = "administrar";

    Context thiscontext;
    private OnFragmentInteractionListener mListener;

    public MenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_menu, container, false);
        inicializarComponentes(rootview);
        thiscontext = container.getContext();
        return rootview;
    }


    private void inicializarComponentes(final View view) {
        labor = (LinearLayout)view.findViewById(R.id.LinearLabor);
        labor.setOnClickListener(this);
        evento = (LinearLayout)view.findViewById(R.id.LinearEvento);
        evento.setOnClickListener(this);
        administrar = (LinearLayout)view.findViewById(R.id.LinearAdministrar);
        administrar.setOnClickListener(this);
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
        Log.i("MenuFragment","onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i("MenuFragment","onPause");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.i("MenuFragment","onDestroy");
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        Uri uri = Uri.parse("");
        switch (view.getId()) {
            case R.id.LinearLabor:
                Log.i("MenuFragment","onClick LinearLabor");
                uri = Uri.parse(BTN_LABOR+":");
                mListener.onFragmentInteraction(uri);
                break;
            case R.id.LinearEvento:
                Log.i("MenuFragment","onClick LinearEvento");
                uri = Uri.parse(BTN_EVENTO+":");
                mListener.onFragmentInteraction(uri);
                break;
            case R.id.LinearAdministrar:
                Log.i("MenuFragment","onClick LinearAdministrar");
                uri = Uri.parse(BTN_ADMINISTRAR+":");
                mListener.onFragmentInteraction(uri);
                break;
        }
    }

}
