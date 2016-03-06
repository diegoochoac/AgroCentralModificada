package com.dinoxindustrial.app.agro_central;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jurado on 25/05/15.
 */
public class FileOperations {

    public String path;
    public String folderPath;
    public MainActivity principal;

    public FileOperations(MainActivity papa) {
        path = "";
        folderPath = "";
        principal = papa;
    }


    public boolean isSDPresent()
    {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }


    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public String getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/../external_sd");
        //String fpath = Environment.getExternalStorageDirectory()+"/data/"+folderPath+"/"+path+".txt";
        /*if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }*/
        return "Exist: "+file.getAbsolutePath()+" : "+file.exists()+" : "+file.getFreeSpace();
        //return fpath;
    }

    public void empezarRegistro()
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        SimpleDateFormat folderFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date();
        System.out.println(format.format(d));
        path = format.format(d);
        folderPath = folderFormat.format(d);
    }

    public void empezarRegistro(boolean GPS)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        SimpleDateFormat folderFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date();
        System.out.println(format.format(d));
        String gps = (GPS)?"":"-gps-nodo";
        path = format.format(d)+gps;
        System.out.println("Path: "+path);
        folderPath = folderFormat.format(d);
    }

    public void empezarRegistro(String hacienda, String lote)
    {
        path = hacienda.toUpperCase()+"-"+lote.toUpperCase();
        System.out.println("Path: "+path);
        folderPath = hacienda.toUpperCase()+"-"+lote.toUpperCase();
    }

    public boolean write(String fname, String fcontent){
        try {
            String fpath = Environment.getExternalStorageDirectory().getPath()+"/../external_sd";

            File file = new File(fpath);

            if(file.getFreeSpace() > 0)
            {
                fpath += "/data/"+folderPath+"/"+path+".txt";
            }

            else {
                fpath = Environment.getExternalStorageDirectory() + "/data/" + folderPath + "/" + path + ".txt";
            }

            System.out.println(fpath);

            principal.showToast(fpath);

            file = new File(fpath);

            // If file does not exists, then create it
            if (!file.exists()) {
                String folderpath = "/sdcard/data/"+folderPath+"/";
                File folder = new File(folderpath);
                folder.mkdirs();
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            pw.println("1"+fcontent);
            pw.close();

            Log.d("Suceess", "Sucess");
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     *
     * @return Funcion que retorna el path del registro si existe sd card
     * @throws Exception
     */
    public String getPathFileRegister()throws Exception
    {
        String fpath = Environment.getExternalStorageDirectory().getPath()+"/../external_sd";

        File file = new File(fpath);
        if(file.getFreeSpace() > 0)
        {
            fpath += "/data/"+folderPath+"/"+path+".txt";
        }
        else {
            fpath = Environment.getExternalStorageDirectory() + "/data/" + folderPath + "/" + path + ".txt";
        }
        return fpath;
    }

    public boolean writeHeaders(String fcontent)
    {
        try {
            String fpath = getPathFileRegister();

            File file = new File(fpath);

            // If file does not exists, then create it
            if (!file.exists()) {
                System.out.println("************************File no exist!!!");
                String folderpath = "/sdcard/data/"+folderPath+"/";
                File folder = new File(file.getParent());
                folder.mkdirs();
                file.createNewFile();

                FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);
                pw.println(fcontent);
                pw.close();
                fw.close();
            }



            Log.d("Suceess", "Sucess "+fcontent);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean write( String fcontent){
        try {

            String fpath = getPathFileRegister();

            System.out.println(fpath);
            principal.showToast(fpath);

            System.out.println(fpath);

            File file = new File(fpath);
            System.out.println(file.getParent());

            // If file does not exists, then create it
            if (!file.exists()) {

                System.out.println("************************File no exist!!!");
                String folderpath = "/sdcard/data/"+folderPath+"/";
                File folder = new File(file.getParent());
                folder.mkdirs();
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            pw.println(fcontent);
            pw.close();

            Log.d("Suceess", "Sucess "+fcontent);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean writeAngles( String fcontent){
        try {

            String fpath = "/sdcard/data/"+folderPath+"/"+path+"_angle.txt";

            System.out.println(fpath);

            File file = new File(fpath);

            // If file does not exists, then create it
            if (!file.exists()) {
                String folderpath = "/sdcard/data/"+folderPath+"/";
                File folder = new File(folderpath);
                folder.mkdirs();
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            pw.println(fcontent);
            pw.close();

            Log.d("Suceess", "Sucess "+fcontent);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public String read(String fname){

        BufferedReader br = null;
        String response = null;

        try {

            StringBuffer output = new StringBuffer();
            String fpath = "/sdcard/"+fname+".txt";

            br = new BufferedReader(new FileReader(fpath));
            String line = "";
            while ((line = br.readLine()) != null) {
                output.append(line +"\n");
            }
            response = output.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;

        }
        return response;

    }
}
