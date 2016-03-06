package com.dinoxindustrial.app.agro_central.fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by jurado on 18/06/15.
 */
public class DrawView extends View {

    Paint paint = new Paint();

    private ArrayList<double[]> datos;

    private double[][] minMax;

    private double relacion;

    public DrawView(Context context) {
        super(context);
        System.out.println("*************Iniciando View Map");
        datos = new ArrayList<double[]>();
        minMax = new double[2][2];

        minMax[0][0] = 3.4441823;
        minMax[0][1] = 3.4420092;
        minMax[1][0] = -76.2450758;
        minMax[1][1] = -76.2514122;

        minMax[0][1] = 3.38745712;
        minMax[0][0] = 3.38545712;
        minMax[1][0] = -76.31005829;
        minMax[1][1] = -76.30605829;


        relacion = (double)(minMax[0][1] - minMax[0][0])/(minMax[1][1] - minMax[1][0]);

        //datos = new double[latlon.length][3];
        //generarDatos(latlon);
    }

    @Override
    public void onDraw(Canvas canvas) {
        /*
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);
        canvas.drawRect(30, 30, 80, 80, paint);
        paint.setStrokeWidth(0);
        paint.setColor(Color.CYAN);
        canvas.drawRect(33, 60, 77, 77, paint);
        paint.setColor(Color.YELLOW);
        canvas.drawRect(33, 33, 77, 60, paint);

        drawPoly(canvas, 0xFF5555ee,
                new Point[]{
                        new Point(10, 10),
                        new Point(25, 10),
                        new Point(25, 20),
                        new Point(10, 20)
                });
        darPoligono(canvas, 30, 30, 130, 130, 40);
        darPoligono(canvas, 50+150,150,10+130,130,40);
        */
        if(datos != null) {
            for (int i = 0; i < datos.size() - 1; i++) {

                //double nColor = (int) (6 * (datos[i][2] - minDatos - 0.00000001) / (maxDatos - minDatos));

                int limiteY = 600;
                //g2d.setColor(colors[nColor]);
                //System.out.println(datos.get(i)[0]+"  -  "+datos.get(i)[1]+"  -  "+datos.get(i)[2]);
                //System.out.println(calculatePos(datos.get(i)[1], minMax, relacion, 1));
                darPoligono(canvas, calculatePos(datos.get(i)[1], minMax, relacion, 1) + 10, limiteY - calculatePos(datos.get(i)[0], minMax, relacion, 0), calculatePos(datos.get(i + 1)[1], minMax, relacion, 1) + 10, limiteY - calculatePos(datos.get(i + 1)[0], minMax, relacion, 0), 3);




            }
        }

    }

    public int calculatePos(double point,double[][] nMinMax,double relation,int type)
    {
        switch (type)
        {
            case 0:
                return (int)(((point - nMinMax[0][0])/(nMinMax[0][1] - nMinMax[0][0]))*600
                        *relacion);
            case 1:
                return (int)(((point - nMinMax[1][0])/(nMinMax[1][1] - nMinMax[1][0]))*600);
        }
        return 0;
    }

    void updateDatos(double[][] data,int num)
    {
        double[][] nDatos = new double[num][2];
        for (int i = 0 ; i < num ; i++)
        {
            nDatos[i][0] = data[i][0];
            nDatos[i][1] = data[i][1];
        }
        generarDatos(nDatos);
    }

    void agregarDatos(double lat, double lon , double meassure)
    {
        //System.out.println("Agregando: "+lat+"  "+lon+"  "+meassure);
        //System.out.println(calculatePos(lon, minMax, relacion, 1) + "   " + calculatePos(lat, minMax, relacion, 0));
        double nDatos[] = {lat,lon,meassure};
        datos.add(nDatos);
    }

    void generarDatos(double[][] nDatos)
    {
        //datos = new double[nDatos.length][3];
        double[][] minMax = new double[2][2];
        if(nDatos.length > 0)
        {
            minMax[0][0] = nDatos[0][0];
            minMax[0][1] = nDatos[0][0];
            minMax[1][0] = nDatos[0][1];
            minMax[1][1] = nDatos[0][1];
            for (int i = 0; i < nDatos.length; i++)
            {
                minMax[0][0] = Math.min(minMax[0][0], nDatos[i][0]);
                minMax[0][1] = Math.max(minMax[0][1], nDatos[i][0]);
                minMax[1][0] = Math.min(minMax[1][0], nDatos[i][1]);
                minMax[1][1] = Math.max(minMax[1][1], nDatos[i][1]);
            }

            double relacion = (double)(minMax[0][1] - minMax[0][0])/(minMax[1][1] - minMax[1][0]);

            for(int i=0;i < nDatos.length;i++){

                nDatos[i][0] = (int)(((nDatos[i][0] - minMax[0][0])/(minMax[0][1] - minMax[0][0]))*600
                        *relacion);
                nDatos[i][1] = (int)(((nDatos[i][1] - minMax[1][0])/(minMax[1][1] - minMax[1][0]))*600);
                nDatos[i][2] = 15;
                //System.out.println(datos[i][0]+"   -   "+datos[i][1]);

            }
        }

        System.out.println("ENTRÓ A ACTUALIZAR DATOS");

        //darProporcion = nDarProporcion;
        //this.repaint();
        //this.invalidate();
    }

    public void darPoligono(Canvas canvas, double x0,double y0,double x1,double y1,double ancho){

        double ang;
        double m = 0;
        if (x1 == x0){
            ang = Math.PI/2;
        }
        else
        {
            m = ((y1-y0)/(x1-x0));
            ang = Math.atan(m);
        }
        double longX = ancho*Math.cos(Math.PI/2-ang);
        double longY = ancho*Math.sin(Math.PI/2-ang);

        drawPoly(canvas, 0xFF5555ee,new Point[]{
                new Point((int) (x0), (int) (y0)),
                new Point((int) (x1), (int) (y1)),
                new Point((int) (x1 - longX), (int) (y1 + longY)),
                new Point((int) (x0 - longX), (int) (y0 + longY))
        });
    }

    private void drawPoly(Canvas canvas, int color, Point[] points) {
        // line at minimum...
        if (points.length < 2) {
            return;
        }

        // paint
        Paint polyPaint = new Paint();
        polyPaint.setColor(color);
        polyPaint.setStyle(Paint.Style.FILL);

        // path
        Path polyPath = new Path();
        polyPath.moveTo(points[0].x, points[0].y);
        int i, len;
        len = points.length;
        for (i = 0; i < len; i++) {
            polyPath.lineTo(points[i].x, points[i].y);
        }
        polyPath.lineTo(points[0].x, points[0].y);

        // draw
        canvas.drawPath(polyPath, polyPaint);
    }


}
