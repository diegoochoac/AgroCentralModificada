package com.dinoxindustrial.app.agro_central;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.Set;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dinoxindustrial.app.agro_central.UsbService;
import com.dinoxindustrial.app.agro_central.fragments.AgroCentralFragmentPagerAdapter;
import com.dinoxindustrial.app.agro_central.fragments.GPSFragment;
import com.dinoxindustrial.app.agro_central.fragments.NodosFragment;
import com.dinoxindustrial.app.agro_central.fragments.OnFragmentInteractionListener;
import com.dinoxindustrial.app.agro_central.fragments.ParametrosFragment;
import com.dinoxindustrial.app.agro_central.fragments.ParametrosMaquinaFragment;
import com.dinoxindustrial.app.agro_central.fragments.RegistroFragment;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends FragmentActivity implements View.OnClickListener,OnFragmentInteractionListener
{
    public static final String PREFS_NAME = "AGRO_REPORTES_PREFS";
    /***
     * Service for usb communication
     */
    private UsbService usbService;

    private AgroCentralFragmentPagerAdapter pagerAdapter;

    /**
     * Labels UI
     */

    /**
     * Buttons UI
     */

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions | 8);

        setContentView(R.layout.agrocentral_layout);

        /*
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        */
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        pagerAdapter = new AgroCentralFragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(4);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        // Get the ViewPager and set it's PagerAdapter so that it can display items
        medicionProfundidad = new DepthMeasure();


        mHandler = new MyHandler(this);

        gpsTablet = true;

        stateRegistro = 0;
        stateComSerial = 0;
        stateNodos = 0;

        enviar = false;
        registrando = false;

        updateData.run();
        guardarReporte.run();
        revisarUSB.run();

        fop = new FileOperations(this);

        reporte = new ReporteProfundidad();


        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

// Define a listener that responds to location updates
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

        loadPreferences();

        boolean isPresent = fop.isSDPresent();

        if(isPresent)
        {
            Toast.makeText(this, "Si sd", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "No sd", Toast.LENGTH_SHORT).show();
        }

        boolean isWritable = fop.isExternalStorageWritable();
        if(isWritable)
        {
            Toast.makeText(this, "Si es escribible", Toast.LENGTH_SHORT).show();
            try {
                Toast.makeText(this, fop.getAlbumStorageDir("Prueba"), Toast.LENGTH_SHORT).show();
            }
            catch (Exception e)
            {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "No es escribible", Toast.LENGTH_SHORT).show();
        }

        //Prueba transmision



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
        super.onResume();
        setFilters();  // Start listening notifications from UsbService
        startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it
    }

    @Override
    public void onPause()
    {
        super.onPause();
        unregisterReceiver(mUsbReceiver);
        unbindService(usbConnection);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void showToast(String msj)
    {
        Toast.makeText(this,msj,Toast.LENGTH_SHORT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnClear:
                //display.setText("");
                break;
            case R.id.btnEnviar:
                enviar = !enviar;
                break;
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
                usbService.findSerialPortDevice();
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
                usbService.write(("" + ini + command + fin).getBytes());
            }
            handler.postDelayed(updateData, delay);
        }
    };

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
                System.out.println(spl[0]);
                registrando = true;
                fop.empezarRegistro(reporte.getHacienda(), reporte.getLote());
                fop.writeHeaders(reporte.darEncabezado());
                //fop.writeAngles("Distance herramienta = " + medicionProfundidad.getDistanceHerramienta());
                //fop.writeAngles("Distance suelo = " + medicionProfundidad.getDistanceGrownd());
                /*
                SharedPreferences sharedPrefs = this.getPreferences(Context.MODE_PRIVATE);
                long highScore = sharedPrefs.getInt("Prueba", 0);
                System.out.println(highScore);
                */
                parameters.getParametrosRegister();
                break;
            case RegistroFragment.BTN_END_REGISTRO:
                /*
                SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("Prueba", 12);
                editor.commit();
                */
                registrando = false;
                break;
            case RegistroFragment.BTN_PARAMETROS:
                viewPager.setCurrentItem(0);
                break;

            default:
                System.out.println("Another command: "+spl[0]);
        }

    }
}