package com.example.cuborotatorio;

class Obj {
    float rho, theta = 0.3F, phi = 1.3F, d, objSize, v11, v12, v13, v21, v22, v23, v32, v33, v43;
    Point3D[] w; // Coordenadas de la esfera
    Point2D[] vScr; // Coordenadas proyectadas en 2D

    Obj() {
        int numPoints = 100; // Número de puntos para representar la esfera
        w = new Point3D[numPoints * numPoints];
        vScr = new Point2D[numPoints * numPoints];

        float radius = 1.0f; // Radio de la esfera
        float deltaTheta = (float) (2 * Math.PI / numPoints); // Incremento del ángulo theta
        float deltaPhi = (float) (Math.PI / numPoints); // Incremento del ángulo phi

        int index = 0;
        for (int i = 0; i < numPoints; i++) {
            for (int j = 0; j < numPoints; j++) {
                float theta = i * deltaTheta; // Ángulo azimutal
                float phi = j * deltaPhi; // Ángulo polar

                // Convertir coordenadas esféricas a cartesianas
                float x = radius * (float) (Math.sin(phi) * Math.cos(theta));
                float y = radius * (float) (Math.sin(phi) * Math.sin(theta));
                float z = radius * (float) Math.cos(phi);

                w[index] = new Point3D(x, y, z);
                index++;
            }
        }

        objSize = radius * 2; // Tamaño del objeto
        rho = 5 * objSize; // Distancia de la cámara
    }

    void initPersp() {
        float costh = (float) Math.cos(theta), sinth = (float) Math.sin(theta);
        float cosph = (float) Math.cos(phi), sinph = (float) Math.sin(phi);
        v11 = -sinth;
        v12 = -cosph * costh;
        v13 = sinph * costh;
        v21 = costh;
        v22 = -cosph * sinth;
        v23 = sinph * sinth;
        v32 = sinph;
        v33 = cosph;
        v43 = -rho;
    }

    void eyeAndScreen() {
        initPersp();
        for (int i = 0; i < w.length; i++) {
            Point3D p = w[i];
            float x = v11 * p.x + v21 * p.y;
            float y = v12 * p.x + v22 * p.y + v32 * p.z;
            float z = v13 * p.x + v23 * p.y + v33 * p.z + v43;
            vScr[i] = new Point2D(-d * x / z, -d * y / z);
        }
    }
}