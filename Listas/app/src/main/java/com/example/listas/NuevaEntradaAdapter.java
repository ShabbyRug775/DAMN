package com.example.listas;

import android.content.Context;
import android.view.*;
import android.widget.*;
import java.text.DateFormat;

public final class NuevaEntradaAdapter extends ArrayAdapter<NuevaEntrada> {
    private final int entradaLayoutRecurso;

    public NuevaEntradaAdapter(final Context c, final int entLayRec) {
        super(c, 0);
        this.entradaLayoutRecurso = entLayRec;
    }

    @Override
    public View getView(final int i, final View v, final ViewGroup vg) {
        final View v2 = getWorkingView(v);
        final ViewHolder vh = getViewHolder(v2);
        final NuevaEntrada ne = getItem(i);

        if (ne != null) { // Verificar que 'ne' no sea null
            vh.tituloView.setText(ne.getTitulo());
            final String s = String.format("Por %s on %s", ne.getAutor(),
                    DateFormat.getDateInstance(DateFormat.SHORT).format(ne.getFecha()));
            vh.subTituloView.setText(s);
            vh.imagenView.setImageResource(ne.getIcono());
        } else {
            // Manejar el caso en que 'ne' es null (opcional)
            vh.tituloView.setText("Entrada no disponible");
            vh.subTituloView.setText("");
            vh.imagenView.setImageResource(R.drawable.icon_1); // Usar un icono por defecto
        }

        return v2;
    }

    private View getWorkingView(final View v3) {
        View workingView = null;
        if (null == v3) {
            final Context c2 = getContext();
            final LayoutInflater inflater = (LayoutInflater)c2.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            workingView = inflater.inflate(entradaLayoutRecurso, null);
        } else {
            workingView = v3;
        }
        return workingView;
    }

    private ViewHolder getViewHolder(final View workingView) {
        final Object tag = workingView.getTag();
        ViewHolder vh = null;

        if (null == tag || !(tag instanceof ViewHolder)) {
            vh = new ViewHolder();
            vh.tituloView = workingView.findViewById(R.id.xtvtitulo);
            vh.subTituloView = workingView.findViewById(R.id.xtvsubtitulo);
            vh.imagenView = workingView.findViewById(R.id.xivicono);
            workingView.setTag(vh);
        } else {
            vh = (ViewHolder) tag;
        }
        return vh;
    }

    private static class ViewHolder {
        public TextView tituloView;
        public TextView subTituloView;
        public ImageView imagenView;
    }
}
