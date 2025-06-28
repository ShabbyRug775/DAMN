package com.example.textoavoz;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Locale;

public class Main extends Activity implements TextToSpeech.OnInitListener {
    private TextToSpeech textToSpeech;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSpanish = findViewById(R.id.xbnEspañol);
        Button btnEnglish = findViewById(R.id.xbnIngles);
        textToSpeech = new TextToSpeech(this, this);
        editText = findViewById(R.id.xet);

        btnSpanish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.setLanguage(new Locale("es", "ES"));
                speak(editText.getText().toString());
            }
        });

        btnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.setLanguage(Locale.ENGLISH);
                speak(editText.getText().toString());
            }
        });
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.LANG_MISSING_DATA || status == TextToSpeech.LANG_NOT_SUPPORTED) {
            Toast.makeText(this, "Idioma no soportado o faltan datos", Toast.LENGTH_SHORT).show();
        }
    }

    private void speak(String str) {
        if (str == null || str.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese texto", Toast.LENGTH_SHORT).show();
            return;
        }

        textToSpeech.speak(str, TextToSpeech.QUEUE_FLUSH, null, null);
        textToSpeech.setSpeechRate(1.0f); // Velocidad normal (1.0)
        textToSpeech.setPitch(1.0f); // Tono normal (1.0)
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}