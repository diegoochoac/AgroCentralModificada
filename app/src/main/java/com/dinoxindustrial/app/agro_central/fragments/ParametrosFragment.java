package com.dinoxindustrial.app.agro_central.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dinoxindustrial.app.agro_central.MainActivity;
import com.dinoxindustrial.app.agro_central.R;
import com.dinoxindustrial.app.agro_central.basedatos.DatabaseCrud;
import com.dinoxindustrial.app.agro_central.basedatos.contratista.Contratista;
import com.dinoxindustrial.app.agro_central.basedatos.contratista.Usuario;
import com.dinoxindustrial.app.agro_central.basedatos.terreno.Hacienda;
import com.dinoxindustrial.app.agro_central.basedatos.terreno.Suerte;

import java.util.ArrayList;
import java.util.List;


public class ParametrosFragment extends Fragment implements TextWatcher,View.OnClickListener{

    //BASE DE DATOS
    private DatabaseCrud database;
    private List<Usuario> usuarioList;
    private List<String> usuarioListName = new ArrayList<>();
    private List<Contratista> contratistaList;
    private List<String> contratistaListName = new ArrayList<>();
    private List<Hacienda> terrenoListHacienda;
    private List<String> terrenoListHaciendaName = new ArrayList<>();
    private List<Suerte> terrenoListSuerte;
    private List<String> terrenoListSuerteName = new ArrayList<>();

    private ArrayAdapter<String> adapterUsuario;
    private ArrayAdapter<String> adapterContratista;
    private ArrayAdapter<String> adapterTerreno;

    //Variables que se van hacia el MAINACTIVITY
    public final static String SET_HACIENDA = "Distacia hacienda";
    public final static String SET_LOTE = "Distacia lote";
    public final static String SET_CONTRATISTA = "Distacia contratista";
    public final static String SET_OPERADOR = "Distacia opeador";
    public final static String SET_PROFUNDIDAD_DESEADA = "Profundidad deseada";
    public final static String SET_EQ_AGRICOLA = "Profundidad eq Agricola";
    public static final String BTN_REGISTRO = "regiistro";
    public static final String BTN_PARAMETROS_MAQUINA = "PArametros maquibna";

    //Variables que se traen de el MAINACTIVITY
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public final static String ARG_HACIENDA = "Arg hacienda";
    public final static String ARG_LOTE = "Arg lote";
    public final static String ARG_CONTRATISTA = "Arg contratista";
    public final static String ARG_OPERADOR = "Arg opeador";

    //Variables locales
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

    private int stateClick = -1;
    private OnFragmentInteractionListener mListener;
    Context thiscontext;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_parametros_2, container, false);
        database = new DatabaseCrud(container.getContext());
        inicializarComponentes(rootview);
        thiscontext = container.getContext();
        return rootview;
    }

    private void inicializarComponentes(final View view) {
        txtHacienda = (TextView)view.findViewById(R.id.txtParametrosHacienda);
        txtLote = (TextView)view.findViewById(R.id.txtParametrosLote);
        txtContratista = (TextView)view.findViewById(R.id.txtParametrosContratista);
        txtOperador = (TextView)view.findViewById(R.id.txtParametrosOperador);

        txtHacienda.setText(hacienda);
        txtLote.setText(lote);
        txtContratista.setText(contratista);
        txtOperador.setText(operador);

        txtHacienda.setOnClickListener(this);
        txtLote.setOnClickListener(this);
        txtContratista.setOnClickListener(this);
        txtOperador.setOnClickListener(this);

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

    }

    void AlerDialogListUsuario(){
        final AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(thiscontext);

        LinearLayout layout= new LinearLayout(thiscontext);
        final TextView Message = new TextView(thiscontext);
        final EditText editText = new EditText(thiscontext);
        final ListView listview = new ListView(thiscontext);

        Message.setText("Ingrese busqueda:");
        editText.setSingleLine();
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(Message);
        layout.addView(editText);
        layout.addView(listview);
        alertdialogbuilder.setTitle("Por favor seleccione");
        alertdialogbuilder.setView(layout);

        listview.setAdapter(adapterUsuario);
        final AlertDialog alert = alertdialogbuilder.create();

        editText.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s){

            }
            public void beforeTextChanged(CharSequence s,int start, int count, int after){

            }
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String text = editText.getText().toString().toLowerCase().trim();
                usuarioList = database.obtenerUsuarioAutocompletar(Usuario.NOMBRE,text);
                Log.i("ParametrosFragment", "AlerDialogListUsuario numero:"+usuarioList.size());
                if(usuarioList.size()>0 && usuarioList != null){
                    for(int i=0; i<usuarioList.size(); i++){
                        usuarioListName.add(usuarioList.get(i).getNombre());
                    }
                    adapterUsuario = new ArrayAdapter<String>(thiscontext,android.R.layout.simple_list_item_1,usuarioListName);
                    listview.setAdapter(adapterUsuario);
                }
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Object select= null;
                select= adapterUsuario.getItem(position);
                txtOperador.setText(select.toString());
                Uri uri = Uri.parse(SET_OPERADOR +":"+ select.toString());
                mListener.onFragmentInteraction(uri);
                alert.cancel();
            }
        });

        alertdialogbuilder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
    }

    void AlerDialogListContratista(){
        final AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(thiscontext);

        LinearLayout layout= new LinearLayout(thiscontext);
        final TextView Message = new TextView(thiscontext);
        final EditText editText = new EditText(thiscontext);
        final ListView listview = new ListView(thiscontext);

        Message.setText("Ingrese busqueda:");
        editText.setSingleLine();
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(Message);
        layout.addView(editText);
        layout.addView(listview);
        alertdialogbuilder.setTitle("Por favor seleccione");
        alertdialogbuilder.setView(layout);

        listview.setAdapter(adapterContratista);
        final AlertDialog alert = alertdialogbuilder.create();

        editText.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s){

            }
            public void beforeTextChanged(CharSequence s,int start, int count, int after){

            }
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String text = editText.getText().toString().toLowerCase().trim();
                contratistaList = database.obtenerContratistaAutocompletar(Contratista.NOMBRE,text);
                Log.i("ParametrosFragment", "AlerDialogListUsuario numero:"+usuarioList.size());
                if(contratistaList.size()>0 && contratistaList != null){
                    for(int i=0; i<contratistaList.size(); i++){
                        contratistaListName.add(contratistaList.get(i).getNombre());
                    }
                    adapterContratista = new ArrayAdapter<String>(thiscontext,android.R.layout.simple_list_item_1,contratistaListName);
                    listview.setAdapter(adapterContratista);
                }
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Object select= null;
                select= adapterContratista.getItem(position);
                txtContratista.setText(select.toString());
                Uri uri = Uri.parse(SET_CONTRATISTA +":"+ select.toString());
                mListener.onFragmentInteraction(uri);
                alert.cancel();
            }
        });

        alertdialogbuilder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
    }

    void AlerDialogListTerreno(){
        final AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(thiscontext);

        LinearLayout layout= new LinearLayout(thiscontext);
        final TextView Message = new TextView(thiscontext);
        final EditText editText = new EditText(thiscontext);
        final ListView listview = new ListView(thiscontext);

        Message.setText("Ingrese busqueda:");
        editText.setSingleLine();
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(Message);
        layout.addView(editText);
        layout.addView(listview);
        alertdialogbuilder.setTitle("Por favor seleccione");
        alertdialogbuilder.setView(layout);

        listview.setAdapter(adapterTerreno);
        Log.i("ParametrosFragment", "AlerDialogListTerreno numero:"+ adapterTerreno.getCount());
        final AlertDialog alert = alertdialogbuilder.create();

        editText.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s){
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                Log.i("ParametrosFragment", "onTextChanged");
                String text = editText.getText().toString().toLowerCase().trim();
                switch (stateClick){
                     case 1:
                         terrenoListHacienda = database.obtenerHaciendasAutocompletar(Hacienda.NOMBRE,text);
                         if(terrenoListHacienda.size()>0 && terrenoListHacienda != null){
                             for(int i=0; i<terrenoListHacienda.size(); i++){
                                 terrenoListHaciendaName.add(terrenoListHacienda.get(i).getNombre());
                             }
                            adapterTerreno = new ArrayAdapter<String>(thiscontext,android.R.layout.simple_list_item_1,terrenoListHaciendaName);
                            listview.setAdapter(adapterTerreno);
                         }
                         break;
                     case 2:
                         terrenoListSuerte = database.obtenerSuertesAutocompletar(Suerte.NOMBRE,text);//TODO arreglar
                         if(terrenoListHacienda.size()>0 && terrenoListHacienda != null){
                             for(int i=0; i<terrenoListSuerte.size(); i++){
                                 terrenoListSuerteName.add(terrenoListSuerte.get(i).getNombre());
                             }
                             adapterTerreno = new ArrayAdapter<String>(thiscontext,android.R.layout.simple_list_item_1,terrenoListSuerteName);
                             listview.setAdapter(adapterTerreno);
                         }
                         break;
                }
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Object select= null;
                Uri uri = Uri.parse("");
                switch (stateClick){//TODO arreglar
                    case 1:
                        select= adapterTerreno.getItem(position);
                        txtHacienda.setText(select.toString());
                        uri = Uri.parse(SET_HACIENDA +":"+ select.toString());
                        mListener.onFragmentInteraction(uri);
                        break;
                    case 2:
                        select= adapterTerreno.getItem(position);
                        txtLote.setText(select.toString());
                        uri = Uri.parse(SET_LOTE +":"+ select.toString() );
                        mListener.onFragmentInteraction(uri);
                        break;
                }
                alert.cancel();
            }
        });

        alertdialogbuilder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
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

            case R.id.txtParametrosHacienda:
                terrenoListHacienda = database.obtenerHaciendas();
                if(terrenoListHacienda.size()>0 && terrenoListHacienda != null){
                    Log.i("ParametrosFragment", "onClick cmpTextHacienda");
                    for(int i=0; i<terrenoListHacienda.size(); i++){
                        terrenoListHaciendaName.add(terrenoListHacienda.get(i).getNombre());
                    }
                    adapterTerreno = new ArrayAdapter<String>(thiscontext,android.R.layout.simple_list_item_1,terrenoListHaciendaName);
                    AlerDialogListTerreno();
                }
                stateClick = 1;
                break;

            case R.id.txtParametrosLote:
                terrenoListSuerte = database.obtenerSuertes();//TODO: Arreglar
                if(terrenoListSuerte.size()>0 && terrenoListSuerte != null){
                    Log.i("ParametrosFragment", "onClick cmpTextSuerte");
                    for(int i=0; i<terrenoListSuerte.size(); i++){
                        terrenoListSuerteName.add(terrenoListSuerte.get(i).getNombre());
                    }
                    adapterTerreno = new ArrayAdapter<String>(thiscontext,android.R.layout.simple_list_item_1,terrenoListSuerteName);
                    AlerDialogListTerreno();
                }
                stateClick = 2;
                break;

            case R.id.txtParametrosContratista:
                contratistaList = database.obtenerContratistas();
                if(contratistaList.size()>0 && contratistaList != null){
                    Log.i("ParametrosFragment", "onClick cmpTextOperador");
                    for(int i=0; i<contratistaList.size(); i++){
                        contratistaListName.add(contratistaList.get(i).getNombre());
                    }
                    adapterContratista = new ArrayAdapter<String>(thiscontext,android.R.layout.simple_list_item_1,contratistaListName);
                    AlerDialogListContratista();
                }
                break;

            case R.id.txtParametrosOperador:
                usuarioList = database.obtenerUsuarios();
                if(usuarioList.size()>0 && usuarioList != null){
                    Log.i("ParametrosFragment", "onClick cmpTextOperador");
                    for(int i=0; i<usuarioList.size(); i++){
                        usuarioListName.add(usuarioList.get(i).getNombre());
                    }
                    adapterUsuario = new ArrayAdapter<String>(thiscontext,android.R.layout.simple_list_item_1,usuarioListName);
                    AlerDialogListUsuario();
                }
                break;


        }
    }
}
