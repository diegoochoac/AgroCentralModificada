package com.dinoxindustrial.app.agro_central;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dinoxindustrial.app.agro_central.basedatos.DatabaseCrud;
import com.dinoxindustrial.app.agro_central.basedatos.contratista.Contratista;
import com.dinoxindustrial.app.agro_central.basedatos.terreno.Hacienda;
import com.dinoxindustrial.app.agro_central.basedatos.terreno.Suerte;
import com.dinoxindustrial.app.agro_central.basedatos.terreno.Variedad;
import com.dinoxindustrial.app.agro_central.basedatos.terreno.Zona;
import com.dinoxindustrial.app.agro_central.fragments.AgroCentralFragmentPagerAdapter;
import com.dinoxindustrial.app.agro_central.fragments.EventoFragment;
import com.dinoxindustrial.app.agro_central.fragments.GPSFragment;
import com.dinoxindustrial.app.agro_central.fragments.LaborFragment;
import com.dinoxindustrial.app.agro_central.fragments.MenuFragment;
import com.dinoxindustrial.app.agro_central.fragments.NodosFragment;
import com.dinoxindustrial.app.agro_central.fragments.OnFragmentInteractionListener;
import com.dinoxindustrial.app.agro_central.fragments.ParametrosFragment;
import com.dinoxindustrial.app.agro_central.fragments.ParametrosMaquinaFragment;
import com.dinoxindustrial.app.agro_central.fragments.RegistroFragment;
import com.dinoxindustrial.app.agro_central.fragments.administrar.AdministrarFragment;
import com.dinoxindustrial.app.agro_central.fragments.administrar.CrearTerreno;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Set;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends FragmentActivity implements View.OnClickListener,OnFragmentInteractionListener
{

    private static DatabaseCrud database;

    public static final String PREFS_NAME = "AGRO_REPORTES_PREFS";
    /***
     * Service for usb communication
     */
    private UsbService usbService;

    private AgroCentralFragmentPagerAdapter pagerAdapter;

    private boolean enviar;
    private boolean registrando;
    private boolean gpsTablet;

    private int stateRegistro;
    private int stateComSerial;
    private int stateNodos;

    private MyHandler mHandler;
    private FileOperations fop;
    private ReporteProfundidad reporte;

    private RegistrationParameters parameters;
    private DepthMeasure medicionProfundidad;

    private ViewPager viewPager;

    private static final int PICKFILE_RESULT_CODE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        setContentView(R.layout.agrocentral_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.i("MainActivity","Pantalla");
            hideVirtualButtons();
        }
        inicializarMenu();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        inicializarGps();
        database = new DatabaseCrud(this);
    }

    @TargetApi(19)
    private void hideVirtualButtons() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // In KITKAT (4.4) and next releases, hide the virtual buttons
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                hideVirtualButtons();
            }
        }
    }

    public void inicializarMenu(){
        Fragment fragment = null;
        fragment = new MenuFragment();
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.fragmentMain, fragment);
        trans.addToBackStack(null);
        trans.commit();
    }

    public void inicializarLabor(){

        Fragment fragment = null;
        fragment = new LaborFragment();
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.fragmentMain, fragment);
        trans.addToBackStack(null);
        trans.commit();
        Log.i("MainActivity","Se coloca el fragment");

        medicionProfundidad = new DepthMeasure();
        mHandler = new MyHandler(this);
        gpsTablet = true;

        stateRegistro = 0;
        stateComSerial = 0;
        stateNodos = 0;

        enviar = false;
        registrando = false;

        //updateData.run();
        //guardarReporte.run();
        //revisarUSB.run();

        fop = new FileOperations(this);
        reporte = new ReporteProfundidad();
        loadPreferences();


        String[] param = getRegistrationParameters().getParametrosRegister();
        String[] paramMachine = getRegistrationParameters().getParametrosMachine();
        String[] paramNodos = getRegistrationParameters().getParametrosNodos();
        Bundle args = new Bundle();
        args.putStringArray("parametro",param);
        args.putStringArray("maquina",paramMachine);
        args.putStringArray("nodo",paramNodos);
        fragment.setArguments(args);
        Log.i("MainActivity","Se envia info a el fragment");

        memoriaSd();
    }

    public void inicializarEvento(){
        Fragment fragment = null;
        fragment = new EventoFragment();
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.fragmentMain, fragment);
        trans.addToBackStack(null);
        trans.commit();
    }

    public void inicializarAdministrar(){
        Fragment fragment = null;
        fragment = new AdministrarFragment();
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.fragmentMain, fragment);
        trans.addToBackStack("menu");
        trans.commit();
    }

    public void inicializarGps(){

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {

            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                //System.out.println(location.getAccuracy()+"  -  "+location.getLatitude()+"  -  "+location.getLongitude()+" - "+location.getAltitude());
                //display.append(location.getAccuracy() + "-" + location.getLatitude() + "-" + location.getLongitude() + "-" + location.getSpeed() + "\n");

                double speed = location.getSpeed()*3.6;

                if(gpsTablet)
                    updateGPS(location.getLatitude(), location.getLongitude(), speed, location.getAccuracy(), location.getAltitude());

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    public void memoriaSd(){

        boolean isPresent = fop.isSDPresent();

        if(isPresent){
            Toast.makeText(this, "Si sd", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "No sd", Toast.LENGTH_SHORT).show();
        }

        boolean isWritable = fop.isExternalStorageWritable();
        if(isWritable){
            Toast.makeText(this, "Si es escribible", Toast.LENGTH_SHORT).show();
            try {
                Toast.makeText(this, fop.getAlbumStorageDir("Prueba"), Toast.LENGTH_SHORT).show();
            }
            catch (Exception e)
            {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "No es escribible", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadPreferences()
    {
        parameters = new RegistrationParameters();
        parameters.loadRegistrationParameters(this);

        medicionProfundidad.setAngleCero(Double.parseDouble(parameters.getParametrosNodos()[0]));
        medicionProfundidad.setOffsetProfundidad(Double.parseDouble(parameters.getParametrosNodos()[1]));

        medicionProfundidad.setDistanceHerramienta(Double.parseDouble(parameters.getParametrosMachine()[0]));
        medicionProfundidad.setDistanceGrownd(Double.parseDouble(parameters.getParametrosMachine()[1]));

        medicionProfundidad.setDistanciaHerramientaTandem(Double.parseDouble(parameters.getParametrosMachine()[4]));
        medicionProfundidad.setDistanciaHerramientaDoble(Double.parseDouble(parameters.getParametrosMachine()[5]));
        medicionProfundidad.setDistanciaHerramientaTriple(Double.parseDouble(parameters.getParametrosMachine()[6]));

        reporte.setParametrosRegistro(parameters.getParametrosRegister());
        reporte.setParametrosMachine(parameters.getParametrosMachine());
        /*
        reporte.

        reporte.set
        */
    }

    public void updateGPS(double latitude,double longitude,double speed,double accuracy,double altitude)
    {
        DecimalFormat df = new DecimalFormat("0.00000");
        DecimalFormat df1 = new DecimalFormat("0.0");

        reporte.setLatitud(latitude);
        reporte.setLongitud(longitude);
        reporte.setVelocidad(speed);
        reporte.setPrecision(accuracy);
        reporte.setAltitud(altitude);

        pagerAdapter.updateGPS(df.format(latitude), df.format(longitude), df1.format(speed), df1.format(altitude), df1.format(accuracy));
    }

    public void updateGPS(double speed)
    {
        DecimalFormat df1 = new DecimalFormat("0.0");

        reporte.setVelocidad(speed);
    }

    public void updateGPS(double latitude,double longitude,double accuracy,double altitude)
    {
        DecimalFormat df = new DecimalFormat("0.00000");
        DecimalFormat df1 = new DecimalFormat("0.0");

        reporte.setLatitud(latitude);
        reporte.setLongitud(longitude);
        reporte.setPrecision(accuracy);
        reporte.setAltitud(altitude);

        pagerAdapter.updateGPS(df.format(latitude), df.format(longitude), df1.format(altitude), df1.format(accuracy));
    }

    public RegistrationParameters getRegistrationParameters()
    {
        return parameters;
    }

    @Override
    public void onResume()
    {
        Log.i("MainActivity","onResume");
        super.onResume();
        setFilters();  // Start listening notifications from UsbService
        startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it
    }

    @Override
    public void onPause()
    {
        Log.i("MainActivity","onPause");
        super.onPause();
        unregisterReceiver(mUsbReceiver);
        unbindService(usbConnection);
    }

    @Override
    protected void onDestroy() {
        Log.i("MainActivity","onDestroy");
        super.onDestroy();
    }

    public void showToast(String msj)
    {
        Toast.makeText(this,msj,Toast.LENGTH_SHORT);
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnRegistro:
                switch (stateRegistro)
                {
                    case 0:
                        //btnRegistro.setText("Terminar Registro");
                        stateRegistro = 1;
                        registrando = true;
                        fop.empezarRegistro(gpsTablet);
                        fop.writeHeaders(reporte.darEncabezado());
                        fop.writeAngles("Distance herramienta = " + medicionProfundidad.getDistanceHerramienta());
                        fop.writeAngles("Distance suelo = "+medicionProfundidad.getDistanceGrownd());
                        break;
                    case 1:
                        //btnRegistro.setText("Iniciar Registro");
                        stateRegistro = 0;
                        registrando = false;
                        break;
                }
                break;
            case R.id.btnPrueba1:
                System.out.println("Btn Prueba 1");
                if(gpsTablet) {
                    gpsTablet = false;
                }
                else {
                    gpsTablet = true;
                }
                //display.setText("GPS: "+gpsTablet+"\n"+display.getText());
                break;
            case R.id.btnPrueba2:
                //display.setText("");
                System.out.println(reporte.darMedicion());
                /*
                FragmentManager FM = getSupportFragmentManager();
                FragmentTransaction FT = FM.beginTransaction();

                Fragment fragment = new ItemFragment();
                FT.add(R.id.container,fragment);
                FT.commit();
                */
                break;
            case R.id.btnComSerial:
                switch (stateComSerial)
                {
                    case 0:
                        //btnComSerial.setText("TerminarCom");
                        enviar = true;
                        stateComSerial = 1;
                        System.out.println("Iniciando com serial");
                        break;
                    case 1:
                        //btnComSerial.setText("IniciarCom");
                        enviar = false;
                        stateComSerial = 0;
                        System.out.println("Terminando com serial");
                        break;
                }
                System.out.println(reporte.darMedicion());
                break;
        }
    }

    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras)
    {
        if(UsbService.SERVICE_CONNECTED == false)
        {
            Intent startService = new Intent(this, service);
            if(extras != null && !extras.isEmpty())
            {
                Set<String> keys = extras.keySet();
                for(String key: keys)
                {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
            startService(startService);
        }
        Intent bindingIntent = new Intent(this, service);
        bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void setFilters()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED);
        filter.addAction(UsbService.ACTION_NO_USB);
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED);
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED);
        filter.addAction(UsbService.ACTION_USB_READY);

        registerReceiver(mUsbReceiver, filter);
    }

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    /*
     * This handler will be passed to UsbService. Dara received from serial port is displayed through this handler
     */
    private static class MyHandler extends Handler
    {
        private final WeakReference<MainActivity> mActivity;

        public MyHandler(MainActivity activity)
        {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case UsbService.MESSAGE_FROM_SERIAL_PORT:

                    String data = (String) msg.obj;
                    //String text = mActivity.get().display.getText().toString();
                    if(data.contains("Encontro"))
                    {
                        mActivity.get().pagerAdapter.updateGPSConsoleUSBSerial(data);
                    }

                    String[] medicion = data.split("Angulo:");

                    char start = 0x02;
                    char end = 0x03;
                    String msg_debug = "No contiene: "+data+"\n";
                    if(data.contains("" + start) && data.contains("" + end))
                    {
                        msg_debug = "Contiene ";
                        String command = data.split(""+start)[1];
                        command = command.split(""+end)[0];
                        command = command.replace(""+start,"");
                        command = command.replace(""+end,"");

                        if(command.startsWith("N0"))
                        {
                            msg_debug+="N0 "+command+"\n";
                            String angle = command.split(":")[1].split(",")[0];
                            if (isNumeric(angle))
                            {
                                msg_debug+="Is numeric\n";
                                double angleTruck = Double.parseDouble(angle) / 100;
                                mActivity.get().medicionProfundidad.addAnguloTractor(angleTruck);
                                mActivity.get().medicionProfundidad.setAngleTruck(mActivity.get().medicionProfundidad.getAnguloTractor());
                                mActivity.get().calcularProfundidad();
                                //mActivity.get().txtAngulo1.setText(""+angleTruck);
                            }
                            else {
                                msg_debug+="Is not numeric\n";
                            }
                        }
                        else if(command.startsWith("N1"))
                        {
                            msg_debug+="N1 "+command+"\n";
                            String angle = command.split(":")[1].split(",")[0];
                            if (isNumeric(angle))
                            {
                                msg_debug+="Is numeric\n";
                                double angleImplement = Double.parseDouble(angle) / 100;
                                mActivity.get().medicionProfundidad.addAnguloImplemento(angleImplement);
                                mActivity.get().medicionProfundidad.setAngleImplement(mActivity.get().medicionProfundidad.getAnguloImplemeneto());
                                mActivity.get().calcularProfundidad();
                                //mActivity.get().txtAngulo2.setText(""+angleImplement);
                            }
                            else {
                                msg_debug+="Is not numeric\n";
                            }
                        }
                        else if(command.startsWith("G0"))
                        {
                            msg_debug+="GPS ";
                            command = command.split(":")[1];
                            String[] spl = command.split("\r\n");
                            msg_debug+="spl "+spl.length+"\r\n";
                            for (int i = 0 ; i < spl.length;i++)
                            {
                                String lat = spl[i];
                                if(lat.startsWith("$GPGGA"))
                                {
                                    msg_debug+=lat+"\r\n";
                                    String[] nmea = lat.split(",");
                                    double latitude =
                                            (isNumeric(nmea[2]))?Double.parseDouble(nmea[2]):0;
                                    latitude = (latitude%100)/60+(int)(latitude/100);
                                    double longitude = (isNumeric(nmea[2]))?Double.parseDouble(nmea[4]):0;
                                    longitude = -((longitude%100)/60+(int)(longitude/100));
                                    double accuracy = (isNumeric(nmea[2]))?Double.parseDouble(nmea[8]):0;
                                    double altitude = (isNumeric(nmea[2]))?Double.parseDouble(nmea[9]):0;
                                    mActivity.get().updateGPS(latitude,longitude,accuracy,altitude);
                                }
                                if(lat.startsWith("$GPRMC"))
                                {
                                    String[] nmea = lat.split(",");
                                    double speed = (isNumeric(nmea[2]))?Double.parseDouble(nmea[7])* 1.85200:0;
                                    mActivity.get().updateGPS(speed);
                                }
                            }
                        }
                    }

                    mActivity.get().pagerAdapter.updateGPSConsole(msg_debug);
                    break;
            }
        }
    }

    /*
     * Notifications from UsbService will be received here.
     */
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context arg0, Intent arg1)
        {
            if(arg1.getAction().equals(UsbService.ACTION_USB_PERMISSION_GRANTED)) // USB PERMISSION GRANTED
            {
                Toast.makeText(arg0, "USB Ready", Toast.LENGTH_SHORT).show();
            }else if(arg1.getAction().equals(UsbService.ACTION_USB_READY)) // USB PERMISSION NOT GRANTED
            {
                Toast.makeText(arg0, "USB reeeady", Toast.LENGTH_SHORT).show();
                enviar = true;
            }

            else if(arg1.getAction().equals(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED)) // USB PERMISSION NOT GRANTED
            {
                Toast.makeText(arg0, "USB Permission not granted", Toast.LENGTH_SHORT).show();
            }else if(arg1.getAction().equals(UsbService.ACTION_NO_USB)) // NO USB CONNECTED
            {
                //TODO Revisar para el cambio de file
                //Toast.makeText(arg0, "No USB connected", Toast.LENGTH_SHORT).show();
            }else if(arg1.getAction().equals(UsbService.ACTION_USB_DISCONNECTED)) // USB DISCONNECTED
            {
                Toast.makeText(arg0, "USB disconnected", Toast.LENGTH_SHORT).show();
            }else if(arg1.getAction().equals(UsbService.ACTION_USB_NOT_SUPPORTED)) // USB NOT SUPPORTED
            {
                Toast.makeText(arg0, "USB device not supported", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private final ServiceConnection usbConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1)
        {
            usbService = ((UsbService.UsbBinder) arg1).getService();
            usbService.setHandler(mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0)
        {
            usbService = null;
        }
    };

    Handler handler = new Handler();
    Handler handlerGuardar = new Handler();
    Handler handlerUsb = new Handler();
    public int contador = 0;
    private Runnable updateData = new Runnable(){
        public void run(){
            //System.out.println("Enviando dato: "+enviar+"  - "+gpsTablet);
            int delay = 500;
            pagerAdapter.updateGPSConsole("usbService: " + (usbService != null) + "\n");

            if(usbService != null)
            {
                pagerAdapter.updateGPSConsole("usbServiceDevice: "+(usbService.isDeviceConnected())+"\n");
                pagerAdapter.updateGPSConsole("usbSerialPort: "+(usbService.isSerialPortConnected())+"\n");
                if(enviar == false)
                {
                    enviar = true;
                }
            }
            if(usbService != null && enviar && !usbService.isSerialPortConnected())
            {
                //usbService.findSerialPortDevice();    //TODO descomentar
            }
            else if(usbService != null && enviar && usbService.isSerialPortConnected()) // if UsbService was correctly binded, Send data{
            {
                pagerAdapter.updateGPSConsole("Enviando \n");
                usbService.printCharacteresReceived();
                String command = "";
                //usbService.write(data.getBytes());
                switch (stateNodos)
                {
                    case 0:
                        stateNodos = 1;
                        command = "N0";
                        delay = 100;
                        break;
                    case 1:
                        if(gpsTablet)
                            stateNodos = 0;
                        else
                        {
                            stateNodos = 2;
                        }
                        command = "N1";
                        break;
                    case 2:
                        stateNodos = 0;
                        command = "G0";
                        delay = 500;
                        break;
                }
                char ini = 0x02;
                char fin = 0x03;
                //usbService.write(("" + ini + command + fin).getBytes());
            }
            handler.postDelayed(updateData, delay);
        }
    };

    public void sendSerial(String command){
        if(usbService != null && enviar && usbService.isSerialPortConnected()) // if UsbService was correctly binded, Send data{
        {
            char ini = 0x02;
            char fin = 0x03;
            usbService.write(("" + ini + command + fin).getBytes());
        }
    }

    private Runnable guardarReporte = new Runnable(){
        public void run(){

            if(registrando)
            {
                System.out.println("Guardando Reporte");
                boolean guardo = fop.write(reporte.darMedicion());
                Toast.makeText(MainActivity.this,"Guardo: "+guardo,Toast.LENGTH_SHORT);

                //DecimalFormat df1 = new DecimalFormat("0.00");
                //String angles = df1.format(angleCero) +","+df1.format(angleImplement)+","+df1.format(angleTruck);
                //fop.writeAngles(angles);
                if(false) {
                    RegistroRestClient.post("trabajo/create", reporte.getRequestParamsRegister(), new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            System.out.println("***************************Success");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            System.out.println("Error");
                        }
                    });
                }
            }
            if(reporte != null)
                pagerAdapter.addPointToMap(reporte.getLatitud(),reporte.getLongitud(),10);
            handlerGuardar.postDelayed(guardarReporte, 3000);
        }
    };

    private Runnable revisarUSB = new Runnable(){
        public void run(){

            System.out.println("Revisando USB");
            pagerAdapter.updateGPSConsole("Revisando USB"+(usbService != null)+"\n");
            handlerUsb.postDelayed(revisarUSB, 3000);
        }
    };

    public void calcularProfundidad()
    {
        medicionProfundidad.imprimierAngulos(fop);
        medicionProfundidad.guardarProfundidad(pagerAdapter);
        reporte.setProfundidad(medicionProfundidad.getProfundidad());
        pagerAdapter.updateEstado(reporte.getEstado());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //backButtonHandler();
    }
    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        // Setting Dialog Title
        alertDialog.setTitle("Salir Aplicacion");
        // Setting Dialog Message
        alertDialog.setMessage("Esta Seguro de Salir?");
        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.dialog_icon);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("SI",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        dialog.cancel();
                    }
                });
        // Showing Alert Message
        alertDialog.show();
    }

    //<editor-fold desc="Leer archivos excel">
    //Leer archivos excel para cargar contratista, terrenos, usuarios
    private void readExcelFile(Context context, String filename) {

        if (!isExternalStorageAvailable() || isExternalStorageReadOnly())
        {
            Log.i("Fallo","Storage not available or read only");
            return;
        }

        try{
            File file = new File(Environment.getExternalStorageDirectory()+"/"+filename);
            FileInputStream myInput = new FileInputStream(file);
            String extension = filename.substring(filename.lastIndexOf(".")+1).trim();
            Log.i("Bien","extension:"+ extension);

            if(extension.equals("xls") || extension.equals("ods")) {
                POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
                HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
                HSSFSheet mySheet = myWorkBook.getSheetAt(0);
                Iterator rowIter = mySheet.rowIterator();
                int fila = 0;

                while (rowIter.hasNext()) {
                    HSSFRow myRow = (HSSFRow) rowIter.next();
                    Iterator cellIter = myRow.cellIterator();
                    String Codigo = "";
                    String Hacienda = "";
                    String Suerte = "";
                    String Variedad = "";
                    String Zona = "";
                    String Area = "";
                    int columna = 0;
                    while (cellIter.hasNext()) {
                        HSSFCell myCell = (HSSFCell) cellIter.next();
                        if (columna == 0 && fila>0) {
                            Codigo = myCell.toString();
                            Log.i("Codigo: ", "" + myCell.toString());
                        }
                        if (columna == 1 && fila>0) {
                            Hacienda = myCell.toString();
                            Log.i("Hacienda: ", "" + myCell.toString());
                            agregarHacienda(Codigo,Hacienda);
                        }
                        if (columna == 2 && fila>0) {
                            Suerte = myCell.toString();
                            Log.i("Suerte: ", "" + myCell.toString());
                        }
                        if (columna == 3 && fila>0) {
                            Variedad = myCell.toString();
                            agregarVariedad(Variedad);
                            Log.i("Variedad: ", "" + myCell.toString());
                        }
                        if (columna == 4 && fila>0) {
                            Zona = myCell.toString();
                            agregarZona(Zona);
                            Log.i("Zona: ", "" + myCell.toString());
                        }
                        if (columna == 5 && fila>0) {
                            Area = myCell.toString();
                            Log.i("Area: ", "" + myCell.toString());
                        }
                        columna= columna+1;
                    }
                    if( fila>0) {
                        agregarSuerte(Suerte,Area,Hacienda,Variedad,Zona);
                    }
                    fila= fila+1;
                }
                Toast.makeText(context, "Finalizo carga desde archivo ", Toast.LENGTH_SHORT).show();

            }else if(extension.equals("xlsx")){
                Log.i("Bien","readExcelFile xlsx");
                XSSFWorkbook workbook = new XSSFWorkbook(myInput);      //TODO: el problema puede estar aca
                XSSFSheet sheet = workbook.getSheetAt(0);
                int rowsCount = sheet.getPhysicalNumberOfRows();
                FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
                for (int r = 0; r<rowsCount; r++) {
                    Row row = sheet.getRow(r);
                    int cellsCount = row.getPhysicalNumberOfCells();
                    for (int c = 0; c<cellsCount; c++) {

                    }
                }

            }

        }catch (Exception e){e.printStackTrace();Log.i("Fallo","readExcelFile"); }

        return;
    }

    public  boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public  boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public  void agregarHacienda(String codigo, String hacienda){
        Hacienda nuevo = new Hacienda(codigo,hacienda);
        database.crearHacienda(nuevo);
    }

    public  Suerte agregarSuerte(String nombre, String area,String hacienda, String variedad, String zona){

        Hacienda haciendaQuery = database.obtenerHacienda(hacienda);
        Variedad variedadQuery = database.obtenerVariedad(variedad);
        Zona zonaQuery = database.obtenerZona(zona);

        Suerte nuevo = new Suerte(nombre,area,haciendaQuery,variedadQuery,zonaQuery);
        database.crearSuerte(nuevo);
        return nuevo;
    }

    public  Zona agregarZona(String valor){
        Zona nuevo = new Zona(valor);
        database.crearZona(nuevo);
        return nuevo;
    }

    public  Variedad agregarVariedad(String valor){
        Variedad nuevo = new Variedad(valor);
        database.crearVariedad(nuevo);
        return nuevo;
    }

    //</editor-fold>

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        Log.i("CrearTerreno", "onActivityResult resultCode:"+ resultCode + "requestCode:"+ requestCode);
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    String FilePath = data.getData().getPath();
                    String FileWind = FilePath.substring(FilePath.lastIndexOf(":")+1);
                    String NameFile = FilePath.substring(FilePath.lastIndexOf("/")+1);
                    Log.i("CrearTerreno", "onActivityResult FilePath: "+FilePath+ " NameFile: "+ FileWind);
                    readExcelFile(this.getApplicationContext(),FileWind);
                }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        System.out.println(uri.toString());
        String[] spl = uri.toString().split(":");
        System.out.println("Command lenght:  "+spl.length);
        switch (spl[0]){
            case "SetCero":
                System.out.println("Set cero command:");
                pagerAdapter.pruebaBoton();
                break;
            /***
             * Parametros Fragment setters
             * public final static String SET_HACIENDA = "Distacia hacienda:";
             * public final static String SET_LOTE = "Distacia lote:";
             * public final static String SET_CONTRATISTA = "Distacia contratista:";
             * public final static String SET_OPERADOR = "Distacia opeador:";
             */
            case ParametrosFragment.SET_HACIENDA:
                if(spl.length > 1) {
                    System.out.println(spl[0] + spl[1]);
                    parameters.saveRegistrationParameter(this,RegistrationParameters.PARAM_HACIENDA,spl[1]);
                    reporte.setHacienda(spl[1]);
                }
                break;
            case ParametrosFragment.SET_LOTE:
                if(spl.length > 1) {
                    System.out.println(spl[0] + spl[1]);
                    parameters.saveRegistrationParameter(this, RegistrationParameters.PARAM_LOTE, spl[1]);
                    reporte.setLote(spl[1]);
                }
                break;
            case ParametrosFragment.SET_CONTRATISTA:
                if(spl.length > 1) {
                    System.out.println(spl[0] + spl[1]);
                    parameters.saveRegistrationParameter(this, RegistrationParameters.PARAM_CONTRATISTA, spl[1]);
                    reporte.setContratista(spl[1]);
                }
                break;
            case ParametrosFragment.SET_OPERADOR:
                if(spl.length > 1) {
                    System.out.println(spl[0] + spl[1]);
                    parameters.saveRegistrationParameter(this, RegistrationParameters.PARAM_OPERADOR, spl[1]);
                    reporte.setOperador(spl[1]);
                }
                break;
            case ParametrosFragment.SET_EQ_AGRICOLA:
            if(spl.length > 1) {
                System.out.println(spl[0] + spl[1]);
                //parameters.saveRegistrationParameter(this, RegistrationParameters.PARAM_OPERADOR, spl[1]);
                //reporte.setOperador(spl[1]);
                parameters.saveRegistrationParameter(this, RegistrationParameters.PARAM_MAQUINA, spl[1]);
                reporte.setMaquina(spl[1]);

                if(spl[1].equals("Tandem"))
                {

                }
                else if(spl[1].equals("Doble"))
                {

                }
                else if(spl[1].equals("Triple"))
                {

                }
            }
            break;
            case ParametrosFragment.BTN_REGISTRO:
                viewPager.setCurrentItem(4);
                break;
            case ParametrosFragment.BTN_PARAMETROS_MAQUINA:
                viewPager.setCurrentItem(1);
                break;
            /***
             * Parametros Fragment setters
             * public final static String SET_DISTANCIA_HERRAMIENTA = "Distacia herramienta:";
             * public final static String SET_DISTANCIA_EJE_SUELO = "Distacia suelo:";
             * public final static String SET_MAQUINARIA = "Distacia suerte:";
             * public final static String SET_ANCHO_LABOR = "Distacia operador:";
             */
            case ParametrosMaquinaFragment.SET_DISTANCIA_HERRAMIENTA:
                if(spl.length > 1) {
                    System.out.println(spl[0] + spl[1]);
                    parameters.saveRegistrationParameter(this, RegistrationParameters.PARAM_DISTANCIA_HERRAMIENTA, spl[1]);
                    medicionProfundidad.setDistanceHerramienta(Double.parseDouble(spl[1]));

                    String eqAgricola = reporte.getMaquina();
                    if(eqAgricola.equals("Tandem"))
                    {
                        System.out.println("Modificando L tandem: "+spl[1]);
                    }
                    else if(eqAgricola.equals("Doble"))
                    {
                        System.out.println("Modificando L doble: "+spl[1]);
                    }
                    else if(eqAgricola.equals("Triple"))
                    {
                        System.out.println("Modificando L triple: "+spl[1]);
                    }
                }
                break;
            case ParametrosMaquinaFragment.SET_DISTANCIA_EJE_SUELO:
                if(spl.length > 1) {
                    System.out.println(spl[0] + spl[1]);
                    parameters.saveRegistrationParameter(this, RegistrationParameters.PARAM_DISTANCIA_SUELO, spl[1]);
                    medicionProfundidad.setDistanceGrownd(Double.parseDouble(spl[1]));
                }
                break;
            case ParametrosMaquinaFragment.SET_MAQUINARIA:
                if(spl.length > 1) {
                    System.out.println(spl[0] + spl[1]);
                    parameters.saveRegistrationParameter(this, RegistrationParameters.PARAM_MAQUINA, spl[1]);
                    //reporte.setMaquina(spl[1]);
                }
                break;
            case ParametrosMaquinaFragment.SET_ANCHO_LABOR:
                if(spl.length > 1) {
                    System.out.println(spl[0] + spl[1]);
                    parameters.saveRegistrationParameter(this, RegistrationParameters.PARAM_ANCHO_LABOR, spl[1]);
                    double anchoLabor = Double.parseDouble(spl[1]);
                    reporte.setAnchoLabor(anchoLabor);
                }
                break;
            case ParametrosMaquinaFragment.SET_PROFUNDIDAD_DESEADA:
                if(spl.length > 1) {
                    System.out.println(spl[0] + spl[1]);
                    parameters.saveRegistrationParameter(this, RegistrationParameters.PARAM_PROFUNDIDAD_DESEADA, spl[1]);
                    reporte.setProfundidadDeseada(spl[1]);
                }
                break;
            case ParametrosMaquinaFragment.BTN_REGISTRO_MAQUINA:
                viewPager.setCurrentItem(4);
                break;
            case ParametrosMaquinaFragment.BTN_PARAMETROS_REGISTRO:
                viewPager.setCurrentItem(0);
                break;

            case NodosFragment.BTN_INIT_COM:
                enviar = true;
                break;
            case NodosFragment.BTN_END_COM:
                enviar = false;
                break;
            case NodosFragment.BTN_SET_CERO:
                medicionProfundidad.setAngleCero();
                parameters.saveRegistrationParameter(this, RegistrationParameters.PARAM_ANG_CERO,""+medicionProfundidad.getAngleCero());
                break;
            case NodosFragment.SET_OFFSET_PROFUNDIDAD:
                if(spl.length > 1) {
                    System.out.println(spl[0] + spl[1]);
                    parameters.saveRegistrationParameter(this, RegistrationParameters.PARAM_OFFSET_PROFUNDIDAD, spl[1]);
                    medicionProfundidad.setOffsetProfundidad(Double.parseDouble(spl[1]));
                    //double anchoLabor = Double.parseDouble(spl[1]);
                    //reporte.setAnchoLabor(anchoLabor);
                }
                break;
            case GPSFragment.BTN_GPS_EXTERNO:
                System.out.println(spl[0]);
                gpsTablet = false;
                String device = usbService.getPID_VID_device();
                pagerAdapter.updateGPSConsoleUSBSerial(device);
                break;
            case GPSFragment.BTN_GPS_INTERNO:
                System.out.println(spl[0]);
                gpsTablet = true;
                break;
            case RegistroFragment.BTN_INIT_REGISTRO:
                Log.i("MainActivity","onFragmentInteraction BTN_INIT_REGISTRO");
                registrando = true;
                fop.empezarRegistro(reporte.getHacienda(), reporte.getLote());
                fop.writeHeaders(reporte.darEncabezado());
                parameters.getParametrosRegister();

                this.sendSerial("T0:START,"+parameters.getParametrosRegisterJSON());
                break;

            case RegistroFragment.BTN_END_REGISTRO:
                Log.i("MainActivity","onFragmentInteraction BTN_END_REGISTRO");
                registrando = false;
                this.sendSerial("T0:END");
                break;

            case RegistroFragment.BTN_PARAMETROS:
                Log.i("MainActivity","onFragmentInteraction BTN_PARAMETROS");
                viewPager.setCurrentItem(0);
                break;

            case MenuFragment.BTN_LABOR:
                Log.i("MainActivity","onFragmentInteraction BTN_LABOR");
                inicializarLabor();
                break;

            case MenuFragment.BTN_EVENTO:
                Log.i("MainActivity","onFragmentInteraction BTN_EVENTO");
                inicializarEvento();
                break;

            case MenuFragment.BTN_ADMINISTRAR:
                Log.i("MainActivity","onFragmentInteraction BTN_ADMINISTRAR");
                inicializarAdministrar();
                break;

            case CrearTerreno.BTN_CREARTERRENOARCHIVO:
                Log.i("CrearTerreno", "btnCargarArchivo"); //TODO no funciona cargar archivos xlsx
                Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
                fileintent.setType("*/*");
                try {
                    startActivityForResult(fileintent, PICKFILE_RESULT_CODE);
                } catch (ActivityNotFoundException e) {        }
                break;

            default:
                System.out.println("Another command: "+spl[0]);
        }

    }
}