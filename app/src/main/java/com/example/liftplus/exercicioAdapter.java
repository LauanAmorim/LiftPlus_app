package com.example.liftplus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class exercicioAdapter extends ArrayAdapter<exercicioRepository> {
    public exercicioAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.activity_exercicio_repository, parent, false);
        }

        TextView idExercicio = view.findViewById(R.id.idExercicio);
        TextView nomeExercicio = view.findViewById(R.id.nomeExercicio);
        TextView grupoMuscular = view.findViewById(R.id.grupoMuscular);

        exercicioRepository currentExercicio = getItem(position);

        idExercicio.setText(currentExercicio.getId());
        nomeExercicio.setText(currentExercicio.getNome());
        grupoMuscular.setText(currentExercicio.getGrupoMuscular());

        return view;
    }
}
