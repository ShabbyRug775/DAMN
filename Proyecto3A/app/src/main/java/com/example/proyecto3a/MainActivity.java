package com.example.proyecto3a;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    // Views
    private TextView angle1TextView, angle2TextView, verticalAngle1TextView, verticalAngle2TextView;
    private SeekBar angle1SeekBar, angle2SeekBar, verticalAngle1SeekBar, verticalAngle2SeekBar;
    private TextView connectionStatus;
    private Button connectButton;

    // Bluetooth
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private ConnectedThread connectedThread;
    private boolean isConnected = false;

    // Variables miembro adicionales
    private boolean verticalSeekBar1Enabled = false;
    private boolean verticalSeekBar2Enabled = false;

    // Constants
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 2;
    private static final int REQUEST_SELECT_DEVICE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Bluetooth Adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            showToast("Este dispositivo no soporta Bluetooth");
            finish();
            return;
        }

        // Initialize views
        initViews();

        // Set up listeners
        setupListeners();

        // Check and request permissions
        checkBluetoothPermissions();
    }

    private void initViews() {
        angle1TextView = findViewById(R.id.angle1TextView);
        angle1SeekBar = findViewById(R.id.angle1SeekBar);
        angle2TextView = findViewById(R.id.angle2TextView);
        angle2SeekBar = findViewById(R.id.angle2SeekBar);
        verticalAngle1TextView = findViewById(R.id.verticalAngle1TextView);
        verticalAngle1SeekBar = findViewById(R.id.verticalAngle1SeekBar);
        verticalAngle2TextView = findViewById(R.id.verticalAngle2TextView);
        verticalAngle2SeekBar = findViewById(R.id.verticalAngle2SeekBar);
        connectButton = findViewById(R.id.connectButton);
        connectionStatus = findViewById(R.id.connectionStatus);

        // Disable controls until connected
        angle1SeekBar.setEnabled(false);
        angle2SeekBar.setEnabled(false);
        verticalAngle1SeekBar.setEnabled(false);
        verticalAngle2SeekBar.setEnabled(false);
    }

    private void setupListeners() {
        // SeekBar 1 (Servo 1: 0-90°)
        angle1SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isConnected) {
                    angle1TextView.setText("Servo 1: " + progress + "°");

                    // Activar verticalAngle1SeekBar si alcanza 90°
                    if (progress >= 90 && !verticalSeekBar1Enabled) {
                        verticalSeekBar1Enabled = true;
                        verticalAngle1SeekBar.setEnabled(true);
                        verticalAngle1SeekBar.setProgress(0); // Resetear a 0
                        verticalAngle1TextView.setText("Servo 1: 90°");
                        showToast("Control vertical Servo 1 activado");
                    } else if (progress < 90 && verticalSeekBar1Enabled) {
                        verticalSeekBar1Enabled = false;
                        verticalAngle1SeekBar.setEnabled(false);
                        verticalAngle1SeekBar.setProgress(0);
                        verticalAngle1TextView.setText("Servo 1: " + progress + "°");
                        showToast("Control vertical Servo 1 desactivado");
                    }

                    sendAngles(progress, angle2SeekBar.getProgress(),
                            verticalAngle1SeekBar.getProgress(),
                            verticalAngle2SeekBar.getProgress());
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        angle2SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isConnected) {
                    angle2TextView.setText("Servo 2: " + progress + "°");

                    // Activar verticalAngle2SeekBar si alcanza 90°
                    if (progress >= 90 && !verticalSeekBar2Enabled) {
                        verticalSeekBar2Enabled = true;
                        verticalAngle2SeekBar.setEnabled(true);
                        verticalAngle2SeekBar.setProgress(0); // Resetear a 0
                        verticalAngle2TextView.setText("Servo 2: 90°");
                        showToast("Control vertical Servo 2 activado");
                    } else if (progress < 90 && verticalSeekBar2Enabled) {
                        verticalSeekBar2Enabled = false;
                        verticalAngle2SeekBar.setEnabled(false);
                        verticalAngle2SeekBar.setProgress(0);
                        verticalAngle2TextView.setText("Servo 2: " + progress + "°");
                        showToast("Control vertical Servo 2 desactivado");
                    }

                    sendAngles(angle1SeekBar.getProgress(), progress,
                            verticalAngle1SeekBar.getProgress(),
                            verticalAngle2SeekBar.getProgress());
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        connectButton.setOnClickListener(v -> {
            if (!isConnected) {
                if (checkBluetoothPermissions()) {
                    selectBluetoothDevice();
                }
            } else {
                disconnect();
            }
        });
    }

    private void selectBluetoothDevice() {
        if (bluetoothAdapter == null) {
            showToast("Error de Bluetooth. Reinicie la aplicación.");
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            enableBluetooth();
        } else {
            Intent intent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(intent, REQUEST_SELECT_DEVICE);
        }
    }

    private boolean checkBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                    != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
                            != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.BLUETOOTH_CONNECT,
                                Manifest.permission.BLUETOOTH_SCAN
                        },
                        REQUEST_BLUETOOTH_PERMISSIONS
                );
                return false;
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_BLUETOOTH_PERMISSIONS
                );
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectBluetoothDevice();
            } else {
                showToast("Permisos necesarios para usar Bluetooth");
            }
        }
    }

    private void enableBluetooth() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                selectBluetoothDevice();
            } else {
                showToast("Bluetooth debe estar activado");
            }
        } else if (requestCode == REQUEST_SELECT_DEVICE && resultCode == RESULT_OK) {
            String address = data.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
            connectToDevice(address);
        }
    }

    private void connectToDevice(String deviceAddress) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);

        new Thread(() -> {
            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                bluetoothSocket.connect();

                runOnUiThread(() -> {
                    connectedThread = new ConnectedThread(bluetoothSocket);
                    connectedThread.start();
                    isConnected = true;
                    updateUI();
                    showToast("Conectado a " + device.getName());
                });
            } catch (IOException e) {
                runOnUiThread(() -> {
                    showToast("Error al conectar: " + e.getMessage());
                    isConnected = false;
                    updateUI();
                });
            }
        }).start();
    }

    private void disconnect() {
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                Log.e("BT", "Error al cerrar socket", e);
            }
            bluetoothSocket = null;
        }

        isConnected = false;
        updateUI();
        showToast("Desconectado");
    }

    private void sendAngles(int angle1, int angle2, int angle3, int angle4) {
        if (connectedThread != null) {
            // Orden de los valores:
            // angle1: SeekBar 1 (horizontal, 0-90°) para Servo 1
            // angle2: SeekBar 2 (horizontal, 0-90°) para Servo 2
            // angle3: SeekBar 3 (vertical, 0-90°) para Servo 1 (se suma 90 en Arduino)
            // angle4: SeekBar 4 (vertical, 0-90°) para Servo 2 (se suma 90 en Arduino)
            String data = angle1 + "," + angle2 + "," + angle3 + "," + angle4 + "\n";
            connectedThread.write(data);
        }
    }

    private void updateUI() {
        runOnUiThread(() -> {
            if (isConnected) {
                connectionStatus.setText("Conectado");
                connectionStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                connectButton.setText("Desconectar");

                // Habilitar siempre las SeekBars horizontales
                angle1SeekBar.setEnabled(true);
                angle2SeekBar.setEnabled(true);

                // Control vertical Servo 1
                verticalSeekBar1Enabled = (angle1SeekBar.getProgress() >= 90);
                verticalAngle1SeekBar.setEnabled(verticalSeekBar1Enabled);

                // Control vertical Servo 2
                verticalSeekBar2Enabled = (angle2SeekBar.getProgress() >= 90);
                verticalAngle2SeekBar.setEnabled(verticalSeekBar2Enabled);
            } else {
                connectionStatus.setText("Desconectado");
                connectionStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                connectButton.setText("Conectar");

                // Deshabilitar todos los controles
                angle1SeekBar.setEnabled(false);
                angle2SeekBar.setEnabled(false);
                verticalAngle1SeekBar.setEnabled(false);
                verticalAngle2SeekBar.setEnabled(false);

                // Resetear estados
                verticalSeekBar1Enabled = false;
                verticalSeekBar2Enabled = false;
            }
        });
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e("BT", "Error al obtener streams", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
                    final String receivedData = new String(buffer, 0, bytes);
                    Log.d("BT", "Datos recibidos: " + receivedData);

                    runOnUiThread(() -> {
                        if (receivedData.startsWith("OK")) {
                            String[] parts = receivedData.split(",");
                            if (parts.length >= 5) {
                                angle1TextView.setText("Servo 1: " + parts[1] + "°");
                                angle2TextView.setText("Servo 2: " + parts[2] + "°");
                                verticalAngle1TextView.setText("Servo 3: " + parts[3] + "°");
                                verticalAngle2TextView.setText("Servo 4: " + parts[4] + "°");
                            }
                        }
                    });
                } catch (IOException e) {
                    runOnUiThread(() -> {
                        showToast("Conexión perdida");
                        disconnect();
                    });
                    break;
                }
            }
        }

        public void write(String data) {
            try {
                mmOutStream.write(data.getBytes());
                Log.d("BT", "Datos enviados: " + data);
            } catch (IOException e) {
                Log.e("BT", "Error al enviar datos", e);
                runOnUiThread(() -> disconnect());
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e("BT", "Error al cerrar socket", e);
            }
        }
    }

    private void showToast(String message) {
        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnect();
    }
}