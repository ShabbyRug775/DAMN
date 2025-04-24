package com.example.fragmentos3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class FragmentDetalle extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle, container, false);

        // Configurar HelechoView
        HelechoView helechoView = view.findViewById(R.id.helechoView);
        TextView textView = view.findViewById(R.id.xtvDetalle);

        return view;
    }

    public void mostrarDetalle(String texto) {
        View view = getView();
        if (view != null) {
            TextView textView = view.findViewById(R.id.xtvDetalle);
            if (textView != null && texto != null) {
                textView.setText(texto);
            }

            HelechoView helechoView = view.findViewById(R.id.helechoView);
            if (helechoView != null) {
                helechoView.invalidate(); // Forzar redibujado
            }
        }
    }
}