package com.example.nfc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.nio.charset.Charset;
import java.util.Arrays;

public class MainActivity extends Activity {
    private static final int DIALOG_WRITE_URL = 1;
    private static final int DIALOG_NEW_TAG = 3;
    private static final int PENDING_INTENT_TECH_DISCOVERED = 1;
    private static final String ARG_MESSAGE = "message";

    private EditText mMyUrl;
    private Button mMyWriteUrlButton;
    private boolean mWriteUrl = false;
    private NfcAdapter mNfcAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMyUrl = (EditText) findViewById(R.id.myUrl);
        mMyWriteUrlButton = (Button) findViewById(R.id.myWriteUrlButton);

        mMyWriteUrlButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mWriteUrl = true;
                MainActivity.this.showDialog(DIALOG_WRITE_URL);
            }
        });

        // Resuelve el intent que inició la actividad
        resolveIntent(getIntent(), false);
    }

    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        switch (id) {
            case DIALOG_WRITE_URL:
                return new AlertDialog.Builder(this)
                        .setTitle("Escribir la URL a la etiqueta...")
                        .setMessage("Touch tag to start writing.")
                        .setCancelable(true)
                        .setNeutralButton(android.R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface d, int arg) {
                                        d.cancel();
                                    }
                                })
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            public void onCancel(DialogInterface d) {
                                mWriteUrl = false;
                            }
                        }).create();
            case DIALOG_NEW_TAG:
                return new AlertDialog.Builder(this)
                        .setTitle("Etiqueta detectada")
                        .setCancelable(true)
                        .setNeutralButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface d, int arg) {
                                        d.dismiss();
                                    }
                                }).create();
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
        switch (id) {
            case DIALOG_NEW_TAG:
                String message = args.getString(ARG_MESSAGE);
                if (message != null) ((AlertDialog) dialog).setMessage(message);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Obtiene una instancia del NfcAdapter:
        NfcManager nfcManager = (NfcManager) this.getSystemService(Context.NFC_SERVICE);
        mNfcAdapter = nfcManager.getDefaultAdapter();

        // Crea un PendingIntent para manejar el descubrimiento de tags:
        PendingIntent pi = createPendingResult(PENDING_INTENT_TECH_DISCOVERED, new Intent(), 0);

        // Habilita el foreground dispatch para tags NDEF:
        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch(
                    this,
                    pi,
                    new IntentFilter[]{
                            new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED),
                            new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
                    },
                    new String[][]{
                            new String[]{"android.nfc.tech.NdefFormatable"},
                            new String[]{"android.nfc.tech.Ndef"}
                    });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // Se llama cuando se detecta un tag NFC mientras la actividad está en primer plano
        resolveIntent(intent, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PENDING_INTENT_TECH_DISCOVERED:
                resolveIntent(data, true);
                break;
        }
    }

    private void resolveIntent(Intent data, boolean foregroundDispatch) {
        if (data == null) return;

        // Se inició desde el historial de aplicaciones: sólo muestra la actividad principal
        if ((data.getFlags() & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0) {
            return;
        }

        String action = data.getAction();
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action) ||
                NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            Tag tag = data.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (foregroundDispatch && mWriteUrl) {
                // Modo escritura
                mWriteUrl = false;

                // Obtiene la URL desde el campo de texto:
                String urlStr = mMyUrl.getText().toString();
                if (urlStr.isEmpty()) {
                    dismissDialog(DIALOG_WRITE_URL);
                    return;
                }

                try {
                    // Convierte la URL a byte array (UTF-8 encoded):
                    byte[] urlBytes = urlStr.getBytes(Charset.forName("UTF-8"));

                    // Ensambla el payload del registro NDEF URI:
                    byte[] urlPayload = new byte[urlBytes.length + 1];
                    urlPayload[0] = 0; // no prefix reduction
                    System.arraycopy(urlBytes, 0, urlPayload, 1, urlBytes.length);

                    // Crea un registro NDEF URI (NFC well-known tipo "urn:nfc:wkt:U")
                    NdefRecord urlRecord = new NdefRecord(
                            NdefRecord.TNF_WELL_KNOWN, // TNF: NFC Forum tipo well-known
                            NdefRecord.RTD_URI,       // Type: urn:nfc:wkt:U
                            new byte[0],               // no ID
                            urlPayload);

                    // Crea el mensaje NDEF del registro URI:
                    NdefMessage msg = new NdefMessage(new NdefRecord[]{urlRecord});

                    Ndef ndefTag = Ndef.get(tag);
                    if (ndefTag != null) {
                        /* Nuestra etiqueta ya está formateada, sólo se requiere escribir nuestro mensaje */
                        try {
                            // Se conecta a la etiqueta:
                            ndefTag.connect();
                            // Escribe el mensaje NDEF:
                            ndefTag.writeNdefMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            // Cierra la conexión:
                            try {
                                ndefTag.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        // Nuestra etiqueta no está formateada con NDEF!
                        NdefFormatable ndefFormatableTag = NdefFormatable.get(tag);
                        if (ndefFormatableTag != null) {
                            /* La etiqueta necesita formatearse con nuestro mensaje */
                            try {
                                // Se conecta a la etiqueta:
                                ndefFormatableTag.connect();
                                // Formato con el mensaje NDEF:
                                ndefFormatableTag.format(msg);
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                // Cierra la conexión:
                                try {
                                    ndefFormatableTag.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    dismissDialog(DIALOG_WRITE_URL);
                }
            } else {
                // Modo lectura
                StringBuilder tagInfo = new StringBuilder();

                // Obtiene la UID de la etiqueta:
                byte[] uid = tag.getId();
                tagInfo.append("UID: ")
                        .append(convertByteArrayToHexString(uid))
                        .append("\n\n");

                // Obtiene los mensajes NDEF de la etiqueta:
                Parcelable[] ndefRaw = data.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
                NdefMessage[] ndefMsgs = null;

                if (ndefRaw != null) {
                    ndefMsgs = new NdefMessage[ndefRaw.length];
                    for (int i = 0; i < ndefMsgs.length; ++i) {
                        ndefMsgs[i] = (NdefMessage) ndefRaw[i];
                    }
                }

                if (ndefMsgs != null) {
                    // Itera los mensajes NDEF en la etiqueta:
                    for (int i = 0; i < ndefMsgs.length; ++i) {
                        // Obtiene los registros de los mensajes NDEF:
                        NdefRecord[] records = ndefMsgs[i].getRecords();
                        if (records != null) {
                            // Itera todos los registros NDEF:
                            for (int j = 0; j < records.length; ++j) {
                                // Verifica si este registro es un registro URI:
                                if ((records[j].getTnf() == NdefRecord.TNF_WELL_KNOWN)
                                        && Arrays.equals(records[j].getType(), NdefRecord.RTD_URI)) {
                                    byte[] payload = records[j].getPayload();
                                    /* Quita el byte de identificación y convierte la URL a cadena (UTF-8): */
                                    String uri = new String(Arrays.copyOfRange(payload, 1, payload.length),
                                            Charset.forName("UTF-8"));
                                    tagInfo.append("URI: ").append(uri).append("\n");
                                }
                            }
                        }
                    }
                }

                // Muestra la información del tag en un diálogo
                Bundle args = new Bundle();
                args.putString(ARG_MESSAGE, tagInfo.toString());
                showDialog(DIALOG_NEW_TAG, args);
            }
        }
    }

    // Método auxiliar para convertir byte array a string hexadecimal
    private static String convertByteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}