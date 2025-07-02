package com.example.extrapar2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GraphFragment extends Fragment {

    TextView tvGrafica;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        tvGrafica = view.findViewById(R.id.tvGrafica);

        // Mostrar los valores generados desde InputFragment
        tvGrafica.setText("Tiempo: " + InputFragment.tiempo +
                "\nAmplitud: " + InputFragment.amplitud);

        return view;
    }
}
