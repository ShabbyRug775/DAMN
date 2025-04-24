package com.example.fragmentos;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Agregar MessageFragment a su contenedor
        MessageFragment messageFragment = new MessageFragment();
        fragmentTransaction.add(R.id.fragment_message_container, messageFragment);

        // Agregar ButtonFragment a su contenedor
        ButtonFragment buttonFragment = new ButtonFragment();
        fragmentTransaction.add(R.id.fragment_button_container, buttonFragment);

        fragmentTransaction.commit();
    }
}