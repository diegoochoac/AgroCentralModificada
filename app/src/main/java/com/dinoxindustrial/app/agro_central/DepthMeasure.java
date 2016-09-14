package com.dinoxindustrial.app.agro_central;

import com.dinoxindustrial.app.agro_central.fragments.AgroCentralFragmentPagerAdapter;

import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * Created by jjurado on 30/06/15.
 */
public class DepthMeasure
{
    public final static int SIZE_PROFUNDIDADES = 3;
    public final static int SIZE_ANGLES = 3;

    private double angleTruck;
    private double angleImplement;
    private double angleCero;

    private double lastProfundidad;

    private double distanceHerramienta;
    private double distanceGrownd;

    private double distanciaHerramientaTandem;
    private double distanciaHerramientaDoble;



    private double distanciaHerramientaTriple;

    private double offsetProfundidad;

    int currentPos;
    int currentPosAngulosTractor;
    int currentPosAngulosImplemnto;

    private double[] profundidades;
    private double[] angulosTractor;
    private double[] angulosImplemnto;

    public DepthMeasure()
    {
        profundidades = new double[SIZE_PROFUNDIDADES];
        angulosTractor = new double[SIZE_ANGLES];
        angulosImplemnto = new double[SIZE_ANGLES];

        currentPos = 0;
        currentPosAngulosTractor = 0;
        currentPosAngulosImplemnto = 0;

        lastProfundidad = 0;

        distanceHerramienta = 150;
        distanceGrownd = 40;

        angleTruck = 0;
        angleImplement = 0;
        angleCero = 0;

        offsetProfundidad = 0;
    }

    void addMeasure(double profundidad)
    {
        profundidades[currentPos] = profundidad;
        currentPos++;
        if(currentPos == SIZE_PROFUNDIDADES)
            currentPos = 0;
    }

    void addAnguloTractor(double ang)
    {
        int lastPosition = (currentPosAngulosTractor != 0)?currentPosAngulosTractor-1:SIZE_ANGLES-1;
        double angle_new = ang;
        if(Math.abs(angulosTractor[lastPosition] - ang) <= 3)
            angle_new = angle_new - 0.5*(angle_new - angulosTractor[lastPosition]);
        angulosTractor[currentPosAngulosTractor] = angle_new;
        currentPosAngulosTractor++;
        if(currentPosAngulosTractor == SIZE_ANGLES)
            currentPosAngulosTractor = 0;
    }

    void addAnguloImplemento(double ang)
    {
        int lastPosition = (currentPosAngulosImplemnto != 0)?currentPosAngulosImplemnto-1:SIZE_ANGLES-1;
        double angle_new = ang;
        if(Math.abs(angulosImplemnto[lastPosition] - ang) <= 3)
            angle_new = angle_new - 0.5*(angle_new - angulosImplemnto[lastPosition]);
        angulosImplemnto[currentPosAngulosImplemnto] = ang;
        currentPosAngulosImplemnto++;
        if(currentPosAngulosImplemnto == SIZE_ANGLES)
            currentPosAngulosImplemnto = 0;
    }

    double getAnguloTractor()
    {
        double[] nAngulos = new double[SIZE_ANGLES];
        for (int i = 0; i < SIZE_ANGLES ; i++)
        {
            nAngulos[i] = angulosTractor[i];
        }
        Arrays.sort(nAngulos);
        return nAngulos[(SIZE_ANGLES)/2];
    }

    double getAnguloImplemeneto()
    {
        double[] nAngulos = new double[SIZE_ANGLES];
        for (int i = 0; i < SIZE_ANGLES ; i++)
        {
            nAngulos[i] = angulosImplemnto[i];
        }
        Arrays.sort(nAngulos);
        return nAngulos[(SIZE_ANGLES)/2];
    }

    double getProfundidad()
    {
        double[] nProfundidades = new double[SIZE_PROFUNDIDADES];
        for (int i = 0; i < SIZE_PROFUNDIDADES ; i++)
        {
            nProfundidades[i] = profundidades[i];
        }
        Arrays.sort(nProfundidades);
        return nProfundidades[(SIZE_PROFUNDIDADES)/2];
    }

    public double calcularProfundidadIterpolacion()
    {
        double angTractor = getAnguloTractor();
        double angImplemento = getAnguloImplemeneto();
        double dif = angTractor - angImplemento -angleCero;
        dif = angTractor - angImplemento;
        double a = -0.0002;
        double b = -0.0070;
        double c = 0.1453;
        double d = 3.9712;
        double e = 0;

        double profundidad = a*Math.pow(dif,4)+b*Math.pow(dif,3)+c*Math.pow(dif,2)+d*Math.pow(dif,1)+e;
        double L = 155;
        L = distanceHerramienta;
        double L2 = distanceGrownd;
        profundidad = L*(Math.sin(Math.toRadians(dif)) - Math.sin(Math.toRadians(angleCero)));

        double ang_inicial = Math.asin((double)L2/L);

        double ang_sistema = dif - angleCero;
        if(ang_sistema < 0){
            ang_sistema += 6;
            if(ang_sistema>0)
                ang_sistema = 0;
        }

        double ang_medicion = Math.toRadians(ang_sistema) + ang_inicial;



        profundidad = L*Math.sin(ang_medicion) - L2;
        if(profundidad >= 0)
            profundidad *= 1.33;

        double nProfundidad = profundidad - 0.3*(profundidad - lastProfundidad);
        lastProfundidad = profundidad;

        return profundidad;
    }

    public double calculateAngleDiference()
    {
        double dif = angleImplement - angleTruck;
        return -dif;
    }

    public double calcularProfundidad()
    {
        double angInicial = Math.acos(distanceGrownd/distanceHerramienta);
        double angDelta = Math.toRadians(calculateAngleDiference() - angleCero);
        double depthTotal = distanceHerramienta*Math.cos(angInicial-angDelta);

        double profundidad = depthTotal-distanceGrownd + offsetProfundidad;

        profundidad = calcularProfundidadIterpolacion() + offsetProfundidad;

        addMeasure(profundidad);

        return profundidad;
    }

    public void guardarProfundidad(AgroCentralFragmentPagerAdapter pagerAdapter)
    {
        calcularProfundidad();

        double profundidad = getProfundidad();

        pagerAdapter.updateGPSConsole("Profundidad: "+profundidad+"\n");

        double angDelta = Math.toRadians(calculateAngleDiference() - angleCero);

        pagerAdapter.updateProfundidad(profundidad, angleTruck, angleImplement, calculateAngleDiference(), angleCero, 180 * angDelta / Math.PI);
    }

    public double getAngleTruck() {
        return angleTruck;
    }

    public void setAngleTruck(double angleTruck) {
        this.angleTruck = angleTruck;
    }

    public double getAngleImplement() {
        return angleImplement;
    }

    public void setAngleImplement(double angleImplement) {
        this.angleImplement = angleImplement;
    }

    public double getAngleCero() {
        return angleCero;
    }

    public void setAngleCero(double angleCero) {
        this.angleCero = angleCero;
    }

    public void setAngleCero() {
        this.angleCero = calculateAngleDiference();
    }

    public double getDistanceHerramienta() {
        return distanceHerramienta;
    }

    public void setDistanceHerramienta(double distanceHerramienta) {
        this.distanceHerramienta = distanceHerramienta;
    }

    public double getDistanceGrownd() {
        return distanceGrownd;
    }

    public void setDistanceGrownd(double distanceGrownd) {
        this.distanceGrownd = distanceGrownd;
    }

    public double getOffsetProfundidad() {
        return offsetProfundidad;
    }

    public void setOffsetProfundidad(double offsetProfundidad) {
        this.offsetProfundidad = offsetProfundidad;
    }

    public double getDistanciaHerramientaTriple() {
        return distanciaHerramientaTriple;
    }

    public void setDistanciaHerramientaTriple(double distanciaHerramientaTriple) {
        this.distanciaHerramientaTriple = distanciaHerramientaTriple;
    }

    public double getDistanciaHerramientaDoble() {
        return distanciaHerramientaDoble;
    }

    public void setDistanciaHerramientaDoble(double distanciaHerramientaDoble) {
        this.distanciaHerramientaDoble = distanciaHerramientaDoble;
    }

    public double getDistanciaHerramientaTandem() {
        return distanciaHerramientaTandem;
    }

    public void setDistanciaHerramientaTandem(double distanciaHerramientaTandem) {
        this.distanciaHerramientaTandem = distanciaHerramientaTandem;
    }

    public void imprimierAngulos(FileOperations fop)
    {
        String angles = ""+angleTruck+","+angleImplement+","+calculateAngleDiference()+","+angleCero+"\n";
        fop.writeAngles(angles);
    }
}
