package com.example.proyecto2;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    // Constantes
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 2;
    private static final int DEVICE_SELECTED = 3;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Variables Bluetooth
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private ConnectedThread connectedThread;
    private OutputStream outputStream;

    // Views
    private TextView ledStatusTextView;
    private TextView statusTextView;
    private Button connectButton;
    private Button disconnectButton;
    private Button ledOnButton;
    private Button ledOffButton;

    // Clase interna para manejar la conexión Bluetooth
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private final byte[] mmBuffer = new byte[1024];

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e("Bluetooth", "Error al obtener los streams", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            outputStream = tmpOut;
        }

        public void run() {
            while (true) {
                try {
                    int bytes = mmInStream.read(mmBuffer);
                    String readMessage = new String(mmBuffer, 0, bytes);

                    // Actualizar UI con el estado del LED
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("ledStatus", readMessage);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    Log.e("Bluetooth", "Error al leer datos", e);
                    break;
                }
            }
        }

        public void write(String command) {
            try {
                mmOutStream.write(command.getBytes());
            } catch (IOException e) {
                Log.e("Bluetooth", "Error al enviar comando", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e("Bluetooth", "Error al cerrar el socket", e);
            }
        }
    }

    // Handler para actualizar la UI
    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String ledStatus = bundle.getString("ledStatus");

            if (ledStatus != null && !ledStatus.isEmpty()) {
                ledStatusTextView.setText("LED: " + ledStatus);
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        checkBluetoothSupport();
        requestNecessaryPermissions();
    }

    private void initViews() {
        ledStatusTextView = findViewById(R.id.ledStatusTextView);
        statusTextView = findViewById(R.id.statusTextView);
        connectButton = findViewById(R.id.connectButton);
        disconnectButton = findViewById(R.id.disconnectButton);
        ledOnButton = findViewById(R.id.ledOnButton);
        ledOffButton = findViewById(R.id.ledOffButton);

        connectButton.setOnClickListener(v -> handleConnectClick());
        disconnectButton.setOnClickListener(v -> disconnectDevice());
        ledOnButton.setOnClickListener(v -> sendCommand("ON"));
        ledOffButton.setOnClickListener(v -> sendCommand("OFF"));

        // Deshabilitar botones de control hasta que se conecte
        setLedControlEnabled(false);
    }

    private void setLedControlEnabled(boolean enabled) {
        ledOnButton.setEnabled(enabled);
        ledOffButton.setEnabled(enabled);
    }

    private void sendCommand(String command) {
        if (connectedThread != null) {
            connectedThread.write(command);
        } else {
            showToast("No hay conexión Bluetooth activa");
        }
    }

    private void checkBluetoothSupport() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            showToast("Bluetooth no soportado en este dispositivo");
            finish();
        }
    }

    private void requestNecessaryPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.BLUETOOTH_CONNECT,
                                Manifest.permission.BLUETOOTH_SCAN,
                                Manifest.permission.ACCESS_FINE_LOCATION
                        },
                        REQUEST_BLUETOOTH_PERMISSIONS);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_BLUETOOTH_PERMISSIONS);
            }
        }
    }

    private void handleConnectClick() {
        if (!hasBluetoothPermissions()) {
            showToast("Se requieren permisos de Bluetooth");
            requestNecessaryPermissions();
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            enableBluetooth();
        } else {
            connectToDevice();
        }
    }

    private boolean hasBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void enableBluetooth() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            requestNecessaryPermissions();
            return;
        }
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    private void connectToDevice() {
        startActivityForResult(new Intent(this, DeviceListActivity.class), DEVICE_SELECTED);
    }

    private void disconnectDevice() {
        try {
            if (connectedThread != null) {
                connectedThread.cancel();
                connectedThread = null;
            }

            if (bluetoothSocket != null) {
                bluetoothSocket.close();
                bluetoothSocket = null;
            }

            statusTextView.setText("Estado: Desconectado");
            ledStatusTextView.setText("LED: Apagado");
            setLedControlEnabled(false);
            showToast("Dispositivo desconectado");
        } catch (IOException e) {
            Log.e("Bluetooth", "Error al desconectar", e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                showToast("Bluetooth activado");
                connectToDevice();
            } else {
                showToast("Bluetooth no activado");
            }
        } else if (requestCode == DEVICE_SELECTED && resultCode == RESULT_OK) {
            String address = data.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
            connectToBluetoothDevice(address);
        }
    }

    private void connectToBluetoothDevice(String address) {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                requestNecessaryPermissions();
                return;
            }

            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
            bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            bluetoothSocket.connect();

            connectedThread = new ConnectedThread(bluetoothSocket);
            connectedThread.start();

            statusTextView.setText("Conectado a: " + device.getName());
            setLedControlEnabled(true);
            showToast("Conexión exitosa");
        } catch (IOException e) {
            Log.e("Bluetooth", "Error al conectar", e);
            showToast("Error al conectar");
        } catch (SecurityException e) {
            Log.e("Bluetooth", "Error de permisos", e);
            showToast("Error de permisos");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                handleConnectClick();
            } else {
                showToast("Permisos denegados - La funcionalidad Bluetooth está limitada");
            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnectDevice();
    }
}