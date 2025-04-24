package com.example.botones;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    Button jbn4;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);
    }

    // Método llamado desde el XML
    public void xbn4DesdeXML(View v) { // MÉTODO 4
        showToastMessage("Botón digitado: xbn4\nInvoca al método desde el XML.");
    }

    private void showToastMessage(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
