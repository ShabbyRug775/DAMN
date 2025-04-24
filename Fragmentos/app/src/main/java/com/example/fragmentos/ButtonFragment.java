package com.example.fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
public class ButtonFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
// Inflar el layout del fragmento
        View view = inflater.inflate(R.layout.fragment_button, container, false);
// Obtener el botón y asignar un listener
        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// Cambiar el mensaje en el fragmento de mensaje
                MessageFragment messageFragment = (MessageFragment) getParentFragmentManager().findFragmentById(R.id.fragment_message_container);
                if (messageFragment != null) {
                    messageFragment.updateMessage("¡Botón presionado!");
                }
            }
        });
        return view;
    }
}