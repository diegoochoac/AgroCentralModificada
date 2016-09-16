package com.dinoxindustrial.app.agro_central.fragments.administrar;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dinoxindustrial.app.agro_central.R;
import com.dinoxindustrial.app.agro_central.basedatos.DatabaseCrud;
import com.dinoxindustrial.app.agro_central.basedatos.terreno.Hacienda;
import com.dinoxindustrial.app.agro_central.basedatos.terreno.Suerte;
import com.dinoxindustrial.app.agro_central.basedatos.terreno.Variedad;
import com.dinoxindustrial.app.agro_central.basedatos.terreno.Zona;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;


public class CrearTerreno extends Fragment implements OnClickListener {

    //Base de Datos
    private DatabaseCrud database;


    private EditText codigo, hacienda, suerte, variedad, zona, area;
    private Button Btnagregar,BtnCargar;

    private static final int PICKFILE_RESULT_CODE = 1;

    Context thiscontext;

    public CrearTerreno() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_crearterreno, container, false);
        database = new DatabaseCrud(container.getContext());
        inicializarComponentes(rootview);
        thiscontext = container.getContext();
        return rootview;
    }

    private void inicializarComponentes(final View view) {
        codigo = (EditText)view.findViewById(R.id.cmpCodigo);
        hacienda = (EditText)view.findViewById(R.id.cmpHacienda);
        suerte = (EditText)view.findViewById(R.id.cmpSuerte);
        variedad = (EditText)view.findViewById(R.id.cmpVariedad);
        zona = (EditText)view.findViewById(R.id.cmpZona);
        area = (EditText)view.findViewById(R.id.cmpArea);

        Btnagregar= (Button)view.findViewById(R.id.btnAgregarTerreno);
        Btnagregar.setOnClickListener(this);

        BtnCargar= (Button)view.findViewById(R.id.btnCargarArchivo);
        BtnCargar.setOnClickListener(this);
    }

    private static void readExcelFile(Context context, String filename) {

        if (!isExternalStorageAvailable() || isExternalStorageReadOnly())
        {
            //Log.e(TAG, "Storage not available or read only");
            return;
        }

        try{
            // Creating Input Stream
            File file = new File(context.getExternalFilesDir(null), filename);
            FileInputStream myInput = new FileInputStream(file);

            // Create a POIFSFileSystem object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);

            /** We now need something to iterate through the cells.**/
            Iterator rowIter = mySheet.rowIterator();

            while(rowIter.hasNext()){
                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator cellIter = myRow.cellIterator();
                while(cellIter.hasNext()){
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    //Log.d(TAG, "Cell Value: " +  myCell.toString());
                    Toast.makeText(context, "cell Value: " + myCell.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){e.printStackTrace(); }

        return;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public void agregarHacienda(String codigo, String hacienda, Suerte suerte){

        Hacienda nuevo = new Hacienda(codigo,hacienda,suerte);
        database.crearHacienda(nuevo);
    }

    public Suerte agregarSuerte(String nombre, String area, Variedad variedad, Zona zona){

        Suerte nuevo = new Suerte(nombre,area,variedad,zona);
        database.crearSuerte(nuevo);
        return nuevo;
    }

    public Zona agregarZona(String valor){
        Zona nuevo = new Zona(valor);
        database.crearZona(nuevo);
        return nuevo;
    }

    public Variedad agregarVariedad(String valor){
        Variedad nuevo = new Variedad(valor);
        database.crearVariedad(nuevo);
        return nuevo;
    }

    public void CrearTerreno(String codigo,String hacienda, String suerte,String variedad, String zona, String area){
        agregarHacienda(codigo,hacienda,agregarSuerte(suerte, area,agregarVariedad(variedad),agregarZona(zona)));
    }

    public void limpiarCampos(){
        codigo.getText().clear();
        hacienda.getText().clear();
        suerte.getText().clear();
        variedad.getText().clear();
        zona.getText().clear();
        area.getText().clear();
    }




    @Override
    public void onClick(View view) {
        Log.i("CrearTerreno", "onClick");
        switch (view.getId()) {
            case R.id.btnAgregarTerreno:
                if(codigo.getText().toString().length()>0 && hacienda.getText().toString().length()>0
                        && suerte.getText().toString().length()>0 && variedad.getText().toString().length()>0
                        &&  zona.getText().toString().length()>0 && area.getText().toString().length()>0 ){

                    CrearTerreno(
                            codigo.getText().toString(),
                            hacienda.getText().toString(),
                            suerte.getText().toString(),
                            variedad.getText().toString(),
                            zona.getText().toString(),
                            area.getText().toString()
                    );
                    Toast.makeText(view.getContext(),"Terreno Agregado", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                }else{
                    Toast.makeText(view.getContext(),"Complete la informacion", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnCargarArchivo:
                Log.i("CrearTerreno", "btnCargarArchivo"); //TODO no funciona aun la seleccion del archivo
                Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
                fileintent.setType("**/*//*");
                try {
                    startActivityForResult(fileintent, PICKFILE_RESULT_CODE);
                } catch (ActivityNotFoundException e) {        }

                break;

        }
    }

}