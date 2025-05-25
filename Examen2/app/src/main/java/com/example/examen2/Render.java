package com.example.examen2;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

public class Render implements GLSurfaceView.Renderer {
    private Cubo mc;
    private float anguloX = 0;
    private float anguloY = 0;
    private GL10 gl;

    public Render(Context cx) {
        mc = new Cubo(cx);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig eglc) {
        this.gl = gl;
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glDisable(GL10.GL_DITHER);
        mc.loadTexture(gl);
        gl.glEnable(GL10.GL_TEXTURE_2D);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int w, int h) {
        if (h == 0) h = 1;
        float spct = (float) w/h;
        gl.glViewport(0, 0, w, h);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 45, spct, 0.1f, 100.f);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, -6.0f);
        gl.glRotatef(anguloX, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(anguloY, 0.0f, 1.0f, 0.0f);
        mc.draw(gl);
    }

    public void setRotation(float dx, float dy) {
        anguloY += dx * 0.5f;
        anguloX += dy * 0.5f;
    }

    public void resetRotation() {
        anguloX = anguloY = 0;
    }

    public GL10 getGL() {
        return gl;
    }
}