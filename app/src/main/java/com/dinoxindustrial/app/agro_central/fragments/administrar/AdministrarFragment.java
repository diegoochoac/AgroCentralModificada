package com.dinoxindustrial.app.agro_central.fragments.administrar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.dinoxindustrial.app.agro_central.R;
import com.dinoxindustrial.app.agro_central.fragments.OnFragmentInteractionListener;

/**
 * Created by diego on 14/09/16.
 */
public class AdministrarFragment extends Fragment implements OnClickListener {

    private String tabTitles[] = new String[] { "Crear Contratista","Crear Usuario", "Crear Terreno", "Administrar" };

    Context thiscontext;
    private OnFragmentInteractionListener mListener;

    public AdministrarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_administrar, container, false);
        inicializarComponentes(rootview);
        thiscontext = container.getContext();
        return rootview;
    }


    private void inicializarComponentes(final View view) {
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewpagerAdministrar);
        mViewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs_administrar);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    CrearContratista contratistatab = new CrearContratista();
                    return contratistatab;

                case 1:
                    CrearUsuario usuariotab = new CrearUsuario();
                    return usuariotab;

                case 2:
                    CrearTerreno terrenotab = new CrearTerreno();
                    return terrenotab;

                case 3:
                    MenuAdministrar administrartab = new MenuAdministrar();
                    return administrartab;

                default:
                    return null;
            }
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
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i("AdministrarFragment","onPause");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.i("AdministrarFragment","onDestroy");
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {



    }
}
