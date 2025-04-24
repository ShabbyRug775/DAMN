package com.example.examen1;

public class Objeto{


    float rho, theta=0.3F, phi=1.3F, d, objSize, v11, v12, v13, v21, v22, v23, v32, v33, v43; // elementos de la matriz
    modelo3D[] w; // Coordenadas del paralepipedo
    modelo2D[] vScr; // Coordenadas proyectadas en 2D

    Objeto() {
        w = new modelo3D[8];
        vScr = new modelo2D[8];

        // Paralelepípedo (base rectangular, altura normal)
        w[0] = new modelo3D(2, -1, -1);  // Punto 0: x más grande (2 en lugar de 1)
        w[1] = new modelo3D(2, 1, -1);   // Punto 1: igual modificación
        w[2] = new modelo3D(-2, 1, -1);  // Punto 2: negativo pero con misma magnitud
        w[3] = new modelo3D(-2, -1, -1); // Punto 3: igual que 0 pero en negativo
        w[4] = new modelo3D(2, -1, 1);   // Cara superior: misma lógica
        w[5] = new modelo3D(2, 1, 1);
        w[6] = new modelo3D(-2, 1, 1);
        w[7] = new modelo3D(-2, -1, 1);

        // Nueva diagonal del paralelepípedo (sqrt(4² + 2² + 2²) = sqrt(16 + 4 + 4) = sqrt(24))
        objSize = (float) Math.sqrt(24F); // Antes era sqrt(12) para el cubo
        rho = 5 * objSize; // Distancia de la cámara (ajustable según necesidad)
    }

    void initPersp() {
        float costh = (float)Math.cos(theta), sinth=(float)Math.sin(theta), cosph=(float)Math.cos(phi), sinph=(float)Math.sin(phi);
        v11 = -sinth; v12 = -cosph*costh; v13 = sinph*costh;
        v21 = costh; v22 = -cosph*sinth; v23 = sinph*sinth;
        v32 = sinph; v33 = cosph; v43 = -rho;
    }

    void eyeAndScreen() {
        initPersp();
        for(int i=0; i<8; i++){
            modelo3D p = w[i];
            float x = v11*p.x + v21*p.y, y = v12*p.x + v22*p.y + v32*p.z, z = v13*p.x + v23*p.y + v33*p.z + v43;
            vScr[i] = new modelo2D(-d*x/z, -d*y/z);
        }
    }

}
