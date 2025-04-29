package com.example.proyecto3a;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.io.OutputStream;
import android.hardware.usb.*;
import com.hoho.android.usbserial.driver.*;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

public class MainActivity extends AppCompatActivity {
    private TextView angleTextView;
    private SeekBar angleSeekBar;
    private UsbSerialDriver serialDriver;
    private UsbDeviceConnection connection;
    private OutputStream outputStream;
    private UsbSerialPort outputPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        angleTextView = findViewById(R.id.angleTextView);
        angleSeekBar = findViewById(R.id.angleSeekBar);
        angleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                angleTextView.setText("Ángulo: " + progress);
                sendAngleToArduino(progress); // Enviar el ángulo a Arduino
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        setupUsbConnection();
    }
    private void setupUsbConnection() {
        UsbManager usbManager = (UsbManager) getSystemService(USB_SERVICE);
        UsbDevice device = null;

        // Buscar el dispositivo Arduino por el Vendor ID
        for (UsbDevice usbDevice : usbManager.getDeviceList().values()) {
            if (usbDevice.getVendorId() == 0x2341) { // Vendor ID de Arduino
                device = usbDevice;
                break;
            }
        }

        if (device != null) {
            // Obtener el driver de serie para el dispositivo USB
            UsbSerialDriver driver = UsbSerialProber.getDefaultProber().probeDevice(device);
            if (driver != null && !driver.getPorts().isEmpty()) {
                UsbSerialPort port = driver.getPorts().get(0); // Obtener el primer puerto de serie

                // Establecer la conexión con el dispositivo USB
                UsbDeviceConnection connection = usbManager.openDevice(driver.getDevice());
                if (connection != null) {
                    try {
                        // Abrir el puerto y configurar parámetros
                        port.open(connection);
                        port.setParameters(
                                9600, // Baud rate
                                8,    // Data bits
                                UsbSerialPort.STOPBITS_1,
                                UsbSerialPort.PARITY_NONE
                        );

                        // Guardar la referencia al puerto
                        this.serialDriver = driver;
                        this.connection = connection;
                        this.outputPort = port; // Asignar el puerto de salida

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void sendAngleToArduino(int angle) {
        if (outputStream != null) {
            try {
                outputStream.write(String.valueOf(angle).getBytes()); // Enviar el ángulo como cadena
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (outputPort != null) {
            try {
                outputPort.close();  // Cerrar el puerto serial
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}