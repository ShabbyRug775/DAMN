package com.example.fragmentos3;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class FragmentListado extends Fragment {

    private Grupo[] datos = new Grupo[]{
            new Grupo("Estudiante 1", "Calificación 1", "Reporte de aprovechamiento 1"),
            new Grupo("Estudiante 2", "Calificación 2", "Reporte de aprovechamiento 2"),
            new Grupo("Estudiante 3", "Calificación 3", "Reporte de aprovechamiento 3"),
            new Grupo("Estudiante 4", "Calificación 4", "Reporte de aprovechamiento 4"),
            new Grupo("Estudiante 5", "Calificación 5", "Reporte de aprovechamiento 5")
    };

    private ListView listView;
    private GruposListener gruposListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listado, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        if (view != null) {
            listView = view.findViewById(R.id.xlvListado);
            if (listView != null) {
                listView.setAdapter(new AdaptadorGrupos(this));

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        if (gruposListener != null) {
                            Grupo grupoSeleccionado = (Grupo) parent.getItemAtPosition(position);
                            gruposListener.onGrupoSeleccionado(grupoSeleccionado);
                        }
                    }
                });
            }
        }
    }

    private class AdaptadorGrupos extends ArrayAdapter<Grupo> {
        private Activity activity;

        AdaptadorGrupos(Fragment fragment) {
            super(fragment.getActivity(), R.layout.listitem_grupo, datos);
            this.activity = fragment.getActivity();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View item = convertView;
            if (item == null) {
                LayoutInflater inflater = activity.getLayoutInflater();
                item = inflater.inflate(R.layout.listitem_grupo, null);
            }

            Grupo grupo = datos[position];

            TextView textViewDe = item.findViewById(R.id.xtvDe);
            if (textViewDe != null) {
                textViewDe.setText(grupo.getDe());
            }

            TextView textViewAsunto = item.findViewById(R.id.xtvAsunto);
            if (textViewAsunto != null) {
                textViewAsunto.setText(grupo.getAsunto());
            }

            return item;
        }
    }

    public interface GruposListener {
        void onGrupoSeleccionado(Grupo grupo);
    }

    public void setGruposListener(GruposListener listener) {
        this.gruposListener = listener;
    }
}