package com.dinoxindustrial.app.agro_central.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.dinoxindustrial.app.agro_central.MainActivity;
import com.dinoxindustrial.app.agro_central.R;

/**
 * Created by diego on 14/09/16.
 */
public class LaborFragment extends Fragment  implements OnClickListener {

    private String tabTitles[] = new String[] { "Parametros","Maquina", "Nodos", "GPS", "Registro","Mapa" };

    String[] parametro;
    String[] maquina;
    String[] nodo;

    public ViewPager mViewPager;

    Context thiscontext;

    private OnFragmentInteractionListener mListener;

    public LaborFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parametro = getArguments().getStringArray("parametro");
        maquina = getArguments().getStringArray("maquina");
        nodo = getArguments().getStringArray("nodo");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_labor, container, false);
        inicializarComponentes(rootview);
        thiscontext = container.getContext();

        return rootview;
    }

    private void inicializarComponentes(final View view) {

        mViewPager = (ViewPager) view.findViewById(R.id.viewpagerLabor);
        mViewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs_labor);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(mViewPager);

    }

    public class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0:
                    String[] param = parametro;
                    ParametrosFragment parametrosFragment = ParametrosFragment.newInstance(param[0],param[1],param[2],param[3]);
                    return parametrosFragment;
                case 1:
                    String[] paramMachine = maquina;
                    ParametrosMaquinaFragment parametrosMaquinaFragment = ParametrosMaquinaFragment.newInstance(paramMachine[0],paramMachine[1],paramMachine[2],paramMachine[3]);
                    return parametrosMaquinaFragment;
                case 2:
                    String[] paramNodos = nodo;
                    NodosFragment nodosFragment = NodosFragment.newInstance(paramNodos[0],paramNodos[1]);
                    return nodosFragment;
                case 3:
                    GPSFragment gpsFragment = GPSFragment.newInstance("GPS","Fragment");
                    return gpsFragment;
                case 4:
                    RegistroFragment registroFragment = RegistroFragment.newInstance("Registro","Fragment");
                    return registroFragment;
                case 5:
                    MapaFragment mapaFragment = MapaFragment.newInstance("Registro","Fragment");
                    return mapaFragment;
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
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
    public void onResume() {
        Log.i("LaborFragment","onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i("LaborFragment","onPause");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.i("LaborFragment","onDestroy");
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutParametros:
                break;
        }

    }

}
