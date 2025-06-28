package com.example.aflogical;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public class ForensicToolsActivity extends AppCompatActivity {

    private static final int REQUEST_MULTIPLE_PERMISSIONS = 1001;
    private static final int REQUEST_STORAGE_PERMISSION = 1002;
    private TextView resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forensic_tools);

        resultView = findViewById(R.id.resultView);

        // Configurar botones
        setupButtons();
    }

    private void setupButtons() {
        Button btnInstalledApps = findViewById(R.id.btnInstalledApps);
        Button btnNetworkInfo = findViewById(R.id.btnNetworkInfo);
        Button btnContacts = findViewById(R.id.btnContacts);
        Button btnCallLogs = findViewById(R.id.btnCallLogs);
        Button btnSMS = findViewById(R.id.btnSMS);
        Button btnLocation = findViewById(R.id.btnLocation);
        Button btnExport = findViewById(R.id.btnExport);
        Button btnExit = findViewById(R.id.btnExit);

        btnInstalledApps.setOnClickListener(v -> showInstalledApps());
        btnNetworkInfo.setOnClickListener(v -> showNetworkInfo());
        btnContacts.setOnClickListener(v -> showContacts());
        btnCallLogs.setOnClickListener(v -> showCallLogs());
        btnSMS.setOnClickListener(v -> showSMS());
        btnLocation.setOnClickListener(v -> showLocationInfo());
        btnExport.setOnClickListener(v -> exportFullReport());
        btnExit.setOnClickListener(v -> finishAffinity());
    }

    // Métodos para mostrar información en la UI
    private void showInstalledApps() {
        resultView.setText(getInstalledAppsReport());
    }

    private void showNetworkInfo() {
        resultView.setText(getNetworkInfoReport());
    }

    private void showContacts() {
        resultView.setText(getContactsReport());
    }

    private void showCallLogs() {
        resultView.setText(getCallLogsReport());
    }

    private void showSMS() {
        resultView.setText(getSMSReport());
    }

    private void showLocationInfo() {
        resultView.setText(getLocationInfoReport());
    }

    // Métodos de generación de reportes (devuelven String)
    private String getDeviceInfoReport() {
        StringBuilder info = new StringBuilder();
        info.append("=== INFORMACIÓN DEL DISPOSITIVO ===\n\n");

        info.append("**Fabricante:** ").append(Build.MANUFACTURER).append("\n");
        info.append("**Modelo:** ").append(Build.MODEL).append("\n");
        info.append("**Producto:** ").append(Build.PRODUCT).append("\n");
        info.append("**Dispositivo:** ").append(Build.DEVICE).append("\n");
        info.append("**Hardware:** ").append(Build.HARDWARE).append("\n");

        info.append("**Versión Android:** ").append(Build.VERSION.RELEASE).append("\n");
        info.append("**SDK:** ").append(Build.VERSION.SDK_INT).append("\n");
        info.append("**Código de versión:** ").append(Build.VERSION.INCREMENTAL).append("\n\n");

        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ((ActivityManager) getSystemService(ACTIVITY_SERVICE)).getMemoryInfo(memoryInfo);
        info.append("**Memoria total:** ").append(memoryInfo.totalMem / (1024 * 1024)).append(" MB\n");
        info.append("**Memoria disponible:** ").append(memoryInfo.availMem / (1024 * 1024)).append(" MB\n");

        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        info.append("**Almacenamiento interno total:** ").append((statFs.getBlockCountLong() * statFs.getBlockSizeLong()) / (1024 * 1024)).append(" MB\n");
        info.append("**Almacenamiento disponible:** ").append((statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong()) / (1024 * 1024)).append(" MB\n");

        return info.toString();
    }

    private String getNetworkInfoReport() {
        StringBuilder netInfo = new StringBuilder();
        netInfo.append("=== INFORMACIÓN DE RED ===\n\n");

        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            if (cm != null) {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

                if (activeNetwork != null) {
                    netInfo.append("**Tipo de conexión:** ").append(activeNetwork.getTypeName()).append("\n");
                    netInfo.append("**Estado:** ").append(activeNetwork.getState()).append("\n");
                    netInfo.append("**Conectado:** ").append(activeNetwork.isConnected()).append("\n");
                    netInfo.append("**Roaming:** ").append(activeNetwork.isRoaming()).append("\n\n");
                }
            }
        } catch (SecurityException e) {
            netInfo.append("**Error:** ").append(e.getMessage()).append("\n\n");
        }

        try {
            if (checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                if (wifiManager != null) {
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();

                    netInfo.append("**WiFi SSID:** ").append(wifiInfo.getSSID()).append("\n");
                    netInfo.append("**BSSID:** ").append(wifiInfo.getBSSID()).append("\n");
                    netInfo.append("**MAC Address:** ").append(wifiInfo.getMacAddress()).append("\n");
                    netInfo.append("**IP Address:** ").append(Formatter.formatIpAddress(wifiInfo.getIpAddress())).append("\n");
                    netInfo.append("**Velocidad de enlace:** ").append(wifiInfo.getLinkSpeed()).append(" Mbps\n");
                    netInfo.append("**Señal WiFi:** ").append(wifiInfo.getRssi()).append(" dBm\n\n");
                }
            }
        } catch (SecurityException e) {
            netInfo.append("**Error WiFi:** ").append(e.getMessage()).append("\n\n");
        }

        return netInfo.toString();
    }

    private String getInstalledAppsReport() {
        StringBuilder appsInfo = new StringBuilder();
        appsInfo.append("=== APLICACIONES INSTALADAS ===\n\n");

        PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());

        for (ApplicationInfo packageInfo : packages) {
            try {
                PackageInfo pkgInfo = pm.getPackageInfo(packageInfo.packageName, 0);

                appsInfo.append("**Nombre:** ").append(pm.getApplicationLabel(packageInfo)).append("\n");
                appsInfo.append("**Paquete:** ").append(packageInfo.packageName).append("\n");
                appsInfo.append("**Versión:** ").append(pkgInfo.versionName).append("\n");
                appsInfo.append("**Código de versión:** ").append(pkgInfo.versionCode).append("\n");
                appsInfo.append("**Fecha instalación:** ").append(sdf.format(new Date(pkgInfo.firstInstallTime))).append("\n");
                appsInfo.append("**Fecha actualización:** ").append(sdf.format(new Date(pkgInfo.lastUpdateTime))).append("\n\n");
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("InstalledApps", "Error al obtener info de app", e);
            }
        }

        return appsInfo.toString();
    }

    @SuppressLint("Range")
    private String getContactsReport() {
        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestContactsPermission();
            return "Se necesitan permisos de contactos para mostrar esta información";
        }

        StringBuilder contacts = new StringBuilder();
        contacts.append("=== CONTACTOS ===\n\n");

        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(
                    ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    contacts.append("**Nombre:** ").append(name).append("\n");

                    if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        Cursor pCur = null;
                        try {
                            pCur = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{id}, null);

                            if (pCur != null) {
                                while (pCur.moveToNext()) {
                                    String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    contacts.append("**Teléfono:** ").append(phone).append("\n");
                                }
                            }
                        } finally {
                            if (pCur != null) {
                                pCur.close();
                            }
                        }
                    }

                    Cursor emailCur = null;
                    try {
                        emailCur = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                                new String[]{id}, null);

                        if (emailCur != null) {
                            while (emailCur.moveToNext()) {
                                String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                                contacts.append("**Email:** ").append(email).append("\n");
                            }
                        }
                    } finally {
                        if (emailCur != null) {
                            emailCur.close();
                        }
                    }

                    contacts.append("\n");
                }
            }
        } catch (Exception e) {
            Log.e("ContactsReport", "Error al leer contactos", e);
            contacts.append("Error al leer contactos: ").append(e.getMessage()).append("\n");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return contacts.toString();
    }

    @SuppressLint("Range")
    private String getCallLogsReport() {
        if (checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            requestCallLogPermission();
            return "Se necesitan permisos de historial de llamadas para mostrar esta información";
        }

        StringBuilder callLogs = new StringBuilder();
        callLogs.append("=== HISTORIAL DE LLAMADAS ===\n\n");

        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(
                    CallLog.Calls.CONTENT_URI,
                    null, null, null, CallLog.Calls.DATE + " DESC");

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                    String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                    String duration = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
                    String type = "";

                    switch (Integer.parseInt(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)))) {
                        case CallLog.Calls.OUTGOING_TYPE:
                            type = "Saliente";
                            break;
                        case CallLog.Calls.INCOMING_TYPE:
                            type = "Entrante";
                            break;
                        case CallLog.Calls.MISSED_TYPE:
                            type = "Perdida";
                            break;
                    }

                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
                    String date = formatter.format(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE)))));

                    callLogs.append("**Número:** ").append(number).append("\n");
                    callLogs.append("**Nombre:** ").append(name == null ? "Desconocido" : name).append("\n");
                    callLogs.append("**Tipo:** ").append(type).append("\n");
                    callLogs.append("**Duración:** ").append(duration).append(" segundos\n");
                    callLogs.append("**Fecha:** ").append(date).append("\n\n");
                }
            }
        } catch (Exception e) {
            Log.e("CallLogsReport", "Error al leer historial de llamadas", e);
            callLogs.append("Error al leer historial: ").append(e.getMessage()).append("\n");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return callLogs.toString();
    }

    @SuppressLint("Range")
    private String getSMSReport() {
        if (checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestSMSPermission();
            return "Se necesitan permisos de SMS para mostrar esta información";
        }

        StringBuilder smsBuilder = new StringBuilder();
        smsBuilder.append("=== MENSAJES SMS ===\n\n");

        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(
                    Telephony.Sms.CONTENT_URI,
                    null, null, null, Telephony.Sms.DATE + " DESC");

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String address = cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS));
                    String body = cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY));
                    int typeValue = cursor.getInt(cursor.getColumnIndex(Telephony.Sms.TYPE));
                    String type = getMessageType(typeValue);

                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
                    String date = formatter.format(new Date(cursor.getLong(cursor.getColumnIndex(Telephony.Sms.DATE))));

                    smsBuilder.append("**Número:** ").append(address).append("\n");
                    smsBuilder.append("**Tipo:** ").append(type).append("\n");
                    smsBuilder.append("**Fecha:** ").append(date).append("\n");
                    smsBuilder.append("**Mensaje:** ").append(body).append("\n\n");
                }
            }
        } catch (Exception e) {
            Log.e("SMSReport", "Error al leer SMS", e);
            smsBuilder.append("Error al leer SMS: ").append(e.getMessage()).append("\n");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return smsBuilder.toString();
    }

    // Método auxiliar para convertir el tipo numérico a texto
    private String getMessageType(int type) {
        switch (type) {
            case Telephony.Sms.MESSAGE_TYPE_INBOX:
                return "Recibido";
            case Telephony.Sms.MESSAGE_TYPE_SENT:
                return "Enviado";
            case Telephony.Sms.MESSAGE_TYPE_DRAFT:
                return "Borrador";
            case Telephony.Sms.MESSAGE_TYPE_OUTBOX:
                return "En cola para enviar";
            case Telephony.Sms.MESSAGE_TYPE_FAILED:
                return "Fallido";
            case Telephony.Sms.MESSAGE_TYPE_QUEUED:
                return "En cola";
            default:
                return "Desconocido (" + type + ")";
        }
    }

    private String getLocationInfoReport() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return "Se necesitan permisos de ubicación para mostrar esta información";
        }

        StringBuilder locationInfo = new StringBuilder();
        locationInfo.append("=== INFORMACIÓN DE UBICACIÓN ===\n\n");

        try {
            android.location.LocationManager locationManager = (android.location.LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                android.location.Location lastKnownLocation = locationManager.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER);

                if (lastKnownLocation != null) {
                    locationInfo.append("**Última ubicación conocida:**\n");
                    locationInfo.append("Latitud: ").append(lastKnownLocation.getLatitude()).append("\n");
                    locationInfo.append("Longitud: ").append(lastKnownLocation.getLongitude()).append("\n");
                    locationInfo.append("Precisión: ").append(lastKnownLocation.getAccuracy()).append(" metros\n");
                    locationInfo.append("Proveedor: ").append(lastKnownLocation.getProvider()).append("\n");
                    locationInfo.append("Hora: ").append(new Date(lastKnownLocation.getTime())).append("\n");
                    locationInfo.append("Velocidad: ").append(lastKnownLocation.getSpeed()).append(" m/s\n");
                } else {
                    locationInfo.append("No se pudo obtener la última ubicación\n");
                }
            }
        } catch (SecurityException e) {
            locationInfo.append("Error de permisos: ").append(e.getMessage()).append("\n");
        } catch (Exception e) {
            Log.e("LocationReport", "Error al obtener ubicación", e);
            locationInfo.append("Error al obtener ubicación: ").append(e.getMessage()).append("\n");
        }

        return locationInfo.toString();
    }

    // Método para exportar el reporte completo
    private void exportFullReport() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
            return;
        }

        new ExportReportTask().execute();
    }

    private class ExportReportTask extends AsyncTask<Void, Integer, File> {
        private Exception exception;
        private long startTime;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startTime = System.currentTimeMillis();
            resultView.setText("Preparando reporte...");
            Toast.makeText(ForensicToolsActivity.this, "Generando reporte, por favor espere...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected File doInBackground(Void... voids) {
            try {
                // 1. Generar contenido del reporte
                StringBuilder fullReport = new StringBuilder();
                fullReport.append("=== REPORTE FORENSE COMPLETO ===\n\n");
                fullReport.append("Generado: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                        .format(new Date())).append("\n\n");

                // Añadir cada sección con verificación de tiempo
                publishProgress(10);
                // Cambia todas las líneas que usan this::method por new Supplier<String>()
                fullReport.append(generateReportSection("Información del Dispositivo", new Supplier<String>() {
                    @Override
                    public String get() {
                        return getDeviceInfoReport();
                    }
                }));

                publishProgress(20);
                fullReport.append(generateReportSection("Aplicaciones Instaladas", new Supplier<String>() {
                    @Override
                    public String get() {
                        return getInstalledAppsReport();
                    }
                }));

                publishProgress(30);
                fullReport.append(generateReportSection("Información de Red", new Supplier<String>() {
                    @Override
                    public String get() {
                        return getNetworkInfoReport();
                    }
                }));

                publishProgress(50);
                fullReport.append(generateReportSection("Contactos", new Supplier<String>() {
                    @Override
                    public String get() {
                        return getContactsReport();
                    }
                }));

                publishProgress(70);
                fullReport.append(generateReportSection("Historial de Llamadas", new Supplier<String>() {
                    @Override
                    public String get() {
                        return getCallLogsReport();
                    }
                }));

                publishProgress(80);
                fullReport.append(generateReportSection("Mensajes SMS", new Supplier<String>() {
                    @Override
                    public String get() {
                        return getSMSReport();
                    }
                }));

                publishProgress(90);
                fullReport.append(generateReportSection("Ubicación", new Supplier<String>() {
                    @Override
                    public String get() {
                        return getLocationInfoReport();
                    }
                }));

                // 2. Crear directorio si no existe
                File dir = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOCUMENTS), "ForensicReports");
                if (!dir.exists() && !dir.mkdirs()) {
                    throw new IOException("No se pudo crear el directorio");
                }

                // 3. Crear archivo
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                File file = new File(dir, "forensic_report_" + timestamp + ".txt");

                // 4. Escribir archivo
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(fullReport.toString().getBytes());
                }

                return file;

            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        private String generateReportSection(String title, Supplier<String> contentSupplier) {
            StringBuilder section = new StringBuilder();
            section.append("=== ").append(title).append(" ===\n\n");

            try {
                String content = contentSupplier.get();
                section.append(content);
            } catch (Exception e) {
                Log.e("ReportSection", "Error en sección " + title, e);
                section.append("Error al generar esta sección: ")
                        .append(e.getMessage())
                        .append("\n");
            }

            section.append("\n\n");
            return section.toString();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            resultView.setText("Generando reporte... " + values[0] + "%");
        }

        @Override
        protected void onPostExecute(File file) {

            if (exception != null) {
                resultView.setText("Error al generar reporte");
                Toast.makeText(ForensicToolsActivity.this,
                        "Error: " + exception.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("ExportError", "Error al exportar", exception);
                return;
            }

            if (file == null || !file.exists()) {
                resultView.setText("No se pudo crear el archivo");
                Toast.makeText(ForensicToolsActivity.this,
                        "Error al crear el archivo", Toast.LENGTH_LONG).show();
                return;
            }

            long duration = System.currentTimeMillis() - startTime;
            String message = String.format(Locale.getDefault(),
                    "Reporte generado en %.1f segundos",
                    duration / 1000.0);

            resultView.setText("Reporte exportado con éxito");
            Toast.makeText(ForensicToolsActivity.this, message, Toast.LENGTH_LONG).show();

            // Mostrar opciones para el archivo
            showFileOptions(file);
        }
    }

    private void showFileOptions(File file) {
        new AlertDialog.Builder(this)
                .setTitle("Reporte generado")
                .setMessage("¿Qué deseas hacer con el reporte?")
                .setPositiveButton("Enviar por correo", (d, w) -> sendReportByEmail(file))
                .setNeutralButton("Abrir archivo", (d, w) -> openFileLocally(file))
                .setNegativeButton("Cerrar", null)
                .show();
    }

    private void sendReportByEmail(File file) {
        try {
            // Verificar que existe cliente de correo
            Intent testIntent = new Intent(Intent.ACTION_SENDTO);
            testIntent.setData(Uri.parse("mailto:"));
            if (getPackageManager().resolveActivity(testIntent, 0) == null) {
                Toast.makeText(this, "No hay aplicaciones de correo instaladas", Toast.LENGTH_LONG).show();
                return;
            }

            // Preparar intent principal
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reporte Forense - " +
                    new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));

            // Cuerpo del mensaje con información del dispositivo
            String emailBody = "Reporte forense generado desde:\n\n" +
                    "Dispositivo: " + Build.MANUFACTURER + " " + Build.MODEL + "\n" +
                    "Android: " + Build.VERSION.RELEASE + "\n" +
                    "Fecha: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date()) +
                    "\n\nEl reporte se adjunta a este mensaje.";

            emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);

            // Adjuntar archivo
            Uri fileUri = FileProvider.getUriForFile(this,
                    getApplicationContext().getPackageName() + ".provider",
                    file);

            emailIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Lanzar selector de aplicaciones de correo
            startActivity(Intent.createChooser(emailIntent, "Enviar reporte usando..."));

        } catch (Exception e) {
            Toast.makeText(this, "Error al enviar: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("EmailError", "Error al enviar correo", e);
        }
    }

    private void openFileLocally(File file) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri fileUri = FileProvider.getUriForFile(this,
                    getApplicationContext().getPackageName() + ".provider",
                    file);

            intent.setDataAndType(fileUri, "text/plain");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Verificar si hay aplicación para abrir el archivo
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "No hay aplicación para abrir archivos de texto", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "No se pudo abrir el archivo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("FileOpen", "Error al abrir archivo", e);
        }
    }

    // Métodos para solicitar permisos
    private void requestContactsPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_CONTACTS},
                REQUEST_MULTIPLE_PERMISSIONS);
    }

    private void requestCallLogPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_CALL_LOG},
                REQUEST_MULTIPLE_PERMISSIONS);
    }

    private void requestSMSPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_SMS},
                REQUEST_MULTIPLE_PERMISSIONS);
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_MULTIPLE_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_MULTIPLE_PERMISSIONS || requestCode == REQUEST_STORAGE_PERMISSION) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            if (allGranted) {
                Toast.makeText(this, "Permisos concedidos", Toast.LENGTH_SHORT).show();
                if (requestCode == REQUEST_STORAGE_PERMISSION) {
                    exportFullReport();
                }
            } else {
                Toast.makeText(this, "Algunos permisos fueron denegados", Toast.LENGTH_LONG).show();
            }
        }
    }
}