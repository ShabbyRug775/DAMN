package com.example.cubo3d;

import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class MiCubo {
    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;
    private FloatBuffer edgeBuffer;
    private FloatBuffer edgeColors;
    private final int numCaras = 6;
    private int[] textureIds = new int[numCaras];
    private Bitmap[] bitmaps = new Bitmap[numCaras];
    private final float tam = 1.2f;

    private final int[] recursosImagenes = {
            R.drawable.escom,
            R.drawable.ipn,
            R.drawable.f18,
            R.drawable.f15,
            R.drawable.mexico,
            R.drawable.marte
    };

    public MiCubo(Context context) {
        inicializarBuffersVertices();
        inicializarBuffersTexturas();
        inicializarBuffersAristas();
        cargarBitmaps(context);
    }

    private void inicializarBuffersVertices() {
        ByteBuffer vbb = ByteBuffer.allocateDirect(12 * 4 * numCaras);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();

        for (int cara = 0; cara < numCaras; cara++) {
            float[] vertices = calcularVerticesCara();
            vertexBuffer.put(vertices);
        }
        vertexBuffer.position(0);
    }

    private float[] calcularVerticesCara() {
        float width = 2.0f;
        float height = 2.0f;

        float izq = -width / 2;
        float der = -izq;
        float base = -height / 2;
        float tope = -base;

        return new float[] {
                izq, base, 0.0f,  // 0 = izq-base-frente
                der, base, 0.0f,  // 1 = der-base-frente
                izq, tope, 0.0f,  // 2 = izq-tope-frente
                der, tope, 0.0f   // 3 = der-tope-frente
        };
    }

    private void inicializarBuffersTexturas() {
        float[] coordenadasTextura = {
                0.0f, 1.0f,  // A = izq-base
                1.0f, 1.0f,  // B = der-base
                0.0f, 0.0f,  // C = izq-tope
                1.0f, 0.0f   // D = der-tope
        };

        ByteBuffer bb = ByteBuffer.allocateDirect(coordenadasTextura.length * 4 * numCaras);
        bb.order(ByteOrder.nativeOrder());
        textureBuffer = bb.asFloatBuffer();

        for (int c = 0; c < numCaras; c++) {
            textureBuffer.put(coordenadasTextura);
        }
        textureBuffer.position(0);
    }

    private void inicializarBuffersAristas() {
        float[] edgeVertices = {
                // Aristas frontales
                -1.0f, -1.0f, 1.0f,  1.0f, -1.0f, 1.0f,
                1.0f, -1.0f, 1.0f,   1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,    -1.0f, 1.0f, 1.0f,
                -1.0f, 1.0f, 1.0f,   -1.0f, -1.0f, 1.0f,

                // Aristas traseras
                -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,  1.0f, 1.0f, -1.0f,
                1.0f, 1.0f, -1.0f,   -1.0f, 1.0f, -1.0f,
                -1.0f, 1.0f, -1.0f,  -1.0f, -1.0f, -1.0f,

                // Aristas verticales
                -1.0f, -1.0f, 1.0f,  -1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, 1.0f,   1.0f, -1.0f, -1.0f,
                1.0f, 1.0f, 1.0f,    1.0f, 1.0f, -1.0f,
                -1.0f, 1.0f, 1.0f,   -1.0f, 1.0f, -1.0f
        };

        float[] colors = {
                1.0f, 1.0f, 1.0f, 1.0f, // Blanco para todas las aristas
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f
        };

        ByteBuffer vbb = ByteBuffer.allocateDirect(edgeVertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        edgeBuffer = vbb.asFloatBuffer();
        edgeBuffer.put(edgeVertices);
        edgeBuffer.position(0);

        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        edgeColors = cbb.asFloatBuffer();
        edgeColors.put(colors);
        edgeColors.position(0);
    }

    private void cargarBitmaps(Context context) {
        for (int i = 0; i < numCaras; i++) {
            bitmaps[i] = BitmapFactory.decodeStream(
                    context.getResources().openRawResource(recursosImagenes[i]));
        }
    }

    public void draw(GL10 gl) {
        configurarEstadoOpenGL(gl);

        // Dibujar cada cara del cubo
        dibujarCara(gl, 0, 0f, 1f, 0f, 0f, tam, 0, 4);      // Frente (0° en Y)
        dibujarCara(gl, 1, 0f, 1f, 0f, 270.0f, tam, 4, 4);  // Izquierda
        dibujarCara(gl, 2, 0f, 1f, 0f, 180.0f, tam, 8, 4);  // Atrás
        dibujarCara(gl, 3, 0f, 1f, 0f, 90.0f, tam, 12, 4);  // Derecha
        dibujarCara(gl, 4, 1f, 0f, 0f, 270.0f, tam, 16, 4); // Tope
        dibujarCara(gl, 5, 1f, 0f, 0f, 90.0f, tam, 20, 4);  // Base

        // Dibujar aristas
        dibujarAristas(gl);

        deshabilitarEstadosOpenGL(gl);
    }

    private void configurarEstadoOpenGL(GL10 gl) {
        gl.glFrontFace(GL10.GL_CCW);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
    }

    private void dibujarCara(GL10 gl, int texturaId, float rotX, float rotY, float rotZ,
                             float angulo, float trasZ, int offset, int count) {
        gl.glPushMatrix();
        gl.glRotatef(angulo, rotX, rotY, rotZ);
        gl.glTranslatef(0f, 0f, trasZ);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIds[texturaId]);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, offset, count);
        gl.glPopMatrix();
    }

    private void dibujarAristas(GL10 gl) {
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, edgeColors);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, edgeBuffer);
        gl.glLineWidth(2.0f); // Grosor de las líneas
        gl.glDrawArrays(GL10.GL_LINES, 0, 24); // 24 vértices (12 líneas)
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    }

    private void deshabilitarEstadosOpenGL(GL10 gl) {
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }

    public void loadTexture(GL10 gl) {
        gl.glGenTextures(numCaras, textureIds, 0);

        for (int cara = 0; cara < numCaras; cara++) {
            configurarTextura(gl, cara);
        }
    }

    private void configurarTextura(GL10 gl, int cara) {
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIds[cara]);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmaps[cara], 0);
        bitmaps[cara].recycle();
    }
}