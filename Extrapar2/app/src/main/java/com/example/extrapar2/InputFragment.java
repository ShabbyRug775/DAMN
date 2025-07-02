package com.example.extrapar2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class InputFragment extends Fragment {

    // Variables accesibles desde GraphFragment
    public static double tiempo = 0;
    public static double amplitud = 0;

    private SeekBar seekTiempo, seekAmplitud;
    private TextView tvTiempoValor, tvAmplitudValor;
    private Button btnGenerar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflar el layout
        View view = inflater.inflate(R.layout.fragment_input, container, false);

        // Referencias UI
        seekTiempo = view.findViewById(R.id.seekTiempo);
        seekAmplitud = view.findViewById(R.id.seekAmplitud);
        tvTiempoValor = view.findViewById(R.id.tvTiempoValor);
        tvAmplitudValor = view.findViewById(R.id.tvAmplitudValor);
        btnGenerar = view.findViewById(R.id.btnGenerar);

        // Configurar SeekBar de Tiempo (0 a 20)
        seekTiempo.setMax(20);
        seekTiempo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tiempo = progress;
                tvTiempoValor.setText("Tiempo: " + progress + " s");
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // Configurar SeekBar de Amplitud (0 a 100)
        seekAmplitud.setMax(100);
        seekAmplitud.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                amplitud = progress;
                tvAmplitudValor.setText("Amplitud: " + progress);
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // Botón "Generar"
        btnGenerar.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Valores establecidos:\nTiempo: " + tiempo + " s\nAmplitud: " + amplitud, Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
