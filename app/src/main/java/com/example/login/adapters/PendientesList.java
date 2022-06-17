package com.example.login.adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.models.Actividad;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.List;

public class PendientesList extends RecyclerView.Adapter<PendientesList.ViewHolder> {
    List<Actividad> lista;
    String padre;
    String idUser;
    public PendientesList(List<Actividad> lista, String padre,String idUser) {
        this.lista = lista;
        this.padre = padre;
        this.idUser = idUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pendiente_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int pos = position;
        holder.tvActivityName.setText(lista.get(position).getNombre());
        holder.tvActivityDescription.setText(lista.get(position).getDescripcion());
        holder.tvFecha.setText(lista.get(position).getFecha());
        Log.i("PendientesList", "onBindViewHolder: " + lista.get(position).getNombre());

        if(lista.get(position).getEstado().equals("finalizada")) {
            holder.btnAvanzar.setVisibility(View.INVISIBLE);
        }

        if(lista.get(position).getEstado().equals("pendiente")){
            holder.btnRegresar.setVisibility(View.INVISIBLE);
        }

        holder.btnAvanzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lista.get(pos).getEstado().equals("pendiente")){
                    pendiente(pos);
                }else if(lista.get(pos).getEstado().equals("inProgress")){
                    inProgress(pos);
                }
            }
        });

        holder.btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lista.get(pos).getEstado().equals("inProgress")){
                    inProgressBack(pos);
                }else if(lista.get(pos).getEstado().equals("finalizada")){
                    finalizadaBack(pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        try{
            return lista.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void pendiente(int position) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(idUser).child("proyectos").child(padre).child("pendientes").child(lista.get(position).getId());
        ref.removeValue();
        Actividad actividad = lista.get(position);
        actividad.setEstado("inProgress");
        actividad.setFecha(java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
        ref = FirebaseDatabase.getInstance().getReference().child(idUser).child("proyectos").child(padre).child("inProgress").child(actividad.getId());
        ref.setValue(actividad);

    }

    public void inProgress(int position) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(idUser).child("proyectos").child(padre).child("inProgress").child(lista.get(position).getId());
        ref.removeValue();
        Actividad actividad = lista.get(position);
        actividad.setEstado("finalizada");
        actividad.setFecha(java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
        ref = FirebaseDatabase.getInstance().getReference().child(idUser).child("proyectos").child(padre).child("finalizadas").child(actividad.getId());
        ref.setValue(actividad);
    }

    public void finalizadaBack(int position){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(idUser).child("proyectos").child(padre).child("finalizadas").child(lista.get(position).getId());
        ref.removeValue();
        Actividad actividad = lista.get(position);
        actividad.setEstado("inProgress");
        actividad.setFecha(java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
        ref = FirebaseDatabase.getInstance().getReference().child(idUser).child("proyectos").child(padre).child("inProgress").child(actividad.getId());
        ref.setValue(actividad);
    }

    public void inProgressBack(int position){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(idUser).child("proyectos").child(padre).child("inProgress").child(lista.get(position).getId());
        ref.removeValue();
        Actividad actividad = lista.get(position);
        actividad.setEstado("pendiente");
        actividad.setFecha(java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
        ref = FirebaseDatabase.getInstance().getReference().child(idUser).child("proyectos").child(padre).child("pendientes").child(actividad.getId());
        ref.setValue(actividad);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button btnAvanzar,btnRegresar;
        TextView tvActivityName,tvActivityDescription,tvFecha;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnAvanzar = itemView.findViewById(R.id.btnAvanzar);
            tvActivityName = itemView.findViewById(R.id.tvActivityName);
            tvActivityDescription = itemView.findViewById(R.id.tvActivityDescription);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            btnRegresar = itemView.findViewById(R.id.btnRegresar);

        }
    }
}
