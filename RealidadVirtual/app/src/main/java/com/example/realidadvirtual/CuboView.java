package com.example.realidadvirtual;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class CuboView extends View {
    private Obj obj;
    private Paint paint;
    private float centerX, centerY;
    private float rho = 5 * (float) Math.sqrt(12);

    public CuboView(Context context) {
        super(context);
        obj = new Obj();
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);
        setBackgroundColor(Color.YELLOW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        centerX = getWidth() / 2f;
        centerY = getHeight() / 2f;
        float minMaxXY = Math.min(getWidth(), getHeight());
        obj.d = obj.rho * minMaxXY / obj.objSize;
        obj.eyeAndScreen();

        // Dibujar las aristas del cubo
        drawLine(canvas, 0, 1); drawLine(canvas, 1, 2); drawLine(canvas, 2, 3); drawLine(canvas, 3, 0);
        drawLine(canvas, 4, 5); drawLine(canvas, 5, 6); drawLine(canvas, 6, 7); drawLine(canvas, 7, 4);
        drawLine(canvas, 0, 4); drawLine(canvas, 1, 5); drawLine(canvas, 2, 6); drawLine(canvas, 3, 7);
    }

    private void drawLine(Canvas canvas, int i, int j) {
        Point2D p = obj.vScr[i], q = obj.vScr[j];
        canvas.drawLine(iX(p.x), iY(p.y), iX(q.x), iY(q.y), paint);
    }

    private int iX(float x) {
        return Math.round(centerX + x);
    }

    private int iY(float y) {
        return Math.round(centerY - y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            obj.theta = (float) getWidth() / event.getX();
            obj.phi = (float) getHeight() / event.getY();
            obj.rho = (obj.phi / obj.theta) * getHeight();
            centerX = event.getX();
            centerY = event.getY();
            invalidate(); // Redibujar
        }
        return true;
    }
}

class Obj{	// Posee los datos del objeto 3D
    float rho, theta=0.3F, phi=1.3F, d, objSize, v11, v12, v13, v21, v22, v23, v32, v33, v43; // elementos de la matriz V
    Point3D [] w;	// coordenadas universales
    Point2D [] vScr; // coordenadas de la pantalla
    Obj(){	// CAMBIAR LAS COORDENADAS X,Y,Z CON 0,1 PARA CONSTRUIR PRISMA, CILINDRO, PIRAMIDE, CONO Y ESFERA.
        w	= new Point3D[8];
        vScr	= new Point2D[8];

        //Cubo
        w[0]	= new Point3D(1, -1, -1); // desde la base
        w[1]	= new Point3D(1, 1, -1);
        w[2]	= new Point3D(-1, 1, -1);
        w[3]	= new Point3D(-1, -1, -1);
        w[4]	= new Point3D(1, -1, 1);
        w[5]	= new Point3D(1, 1, 1);
        w[6]	= new Point3D(-1, 1, 1);
        w[7]	= new Point3D(-1, -1, 1);

        objSize = (float) Math.sqrt(12F); // = sqrt(2*2 + 2*2 + 2*2) es la distancia entre dos vertices opuestos
        rho	= 5*objSize;		// para cambiar la perspectiva
    }
    void initPersp(){
        float costh = (float)Math.cos(theta), sinth=(float)Math.sin(theta), cosph=(float)Math.cos(phi), sinph=(float)Math.sin(phi);
        v11 = -sinth; v12 = -cosph*costh; v13 = sinph*costh;
        v21 = costh; v22 = -cosph*sinth; v23 = sinph*sinth;
        v32 = sinph; v33 = cosph; v43 = -rho;
    }
    void eyeAndScreen(){
        initPersp();
        for(int i=0; i<8; i++){
            Point3D p = w[i];
            float x = v11*p.x + v21*p.y, y = v12*p.x + v22*p.y + v32*p.z, z = v13*p.x + v23*p.y + v33*p.z + v43;
            vScr[i] = new Point2D(-d*x/z, -d*y/z);
        }
    }
}
class Point2D{
    float x, y;
    Point2D(float x, float y){
        this.x = x;
        this.y = y;
    }
}
class Point3D{
    float x, y, z;
    Point3D(double x, double y, double z){
        this.x = (float) x;
        this.y = (float) y;
        this.z = (float) z;
    }
}