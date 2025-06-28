package com.example.realidadvirtual;

import android.content.Context;
import android.opengl.GLES20;

import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CuboRenderer implements GvrView.Renderer {
    private Obj obj;
    private Context context;

    public CuboRenderer(MainActivity context) {
        this.context = context;
        obj = new Obj();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1, 1, 0, 1); // Fondo amarillo
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        // Dibujar el cubo en 3D (implementación con OpenGL ES)
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        // Actualizar la vista basada en el movimiento de la cabeza
    }

    @Override
    public void onFinishFrame(Viewport viewport) {}
}