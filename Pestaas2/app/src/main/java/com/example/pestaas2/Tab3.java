package com.example.pestaas2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
public class Tab3 extends Fragment {
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
    }
    @Override
    public View onCreateView(LayoutInflater li, ViewGroup vg, Bundle bn) {
        return li.inflate(R.layout.tab3, vg, false);
    }
}