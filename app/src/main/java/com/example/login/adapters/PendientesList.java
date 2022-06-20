package com.example.login.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.activities.NewActivitie;
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

        if(lista.get(position).getEstado().equals("finalizadas")) {
            holder.btnAvanzar.setVisibility(View.INVISIBLE);
        }

        if(lista.get(position).getEstado().equals("pendientes")){
            holder.btnRegresar.setVisibility(View.INVISIBLE);
        }

        holder.btnAvanzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (lista.get(pos).getEstado().equals("pendientes")) {
                        pendiente(pos);
                    } else if (lista.get(pos).getEstado().equals("inProgress")) {
                        inProgress(pos);
                    }
                }catch (Exception e) {
                    Toast.makeText(view.getContext(), "Error al cambiar estado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (lista.get(pos).getEstado().equals("inProgress")) {
                        inProgressBack(pos);
                    } else if (lista.get(pos).getEstado().equals("finalizadas")) {
                        finalizadaBack(pos);
                    }
                }catch (Exception e) {
                    Toast.makeText(view.getContext(), "Error al cambiar estado", Toast.LENGTH_SHORT).show();
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
        actividad.setEstado("finalizadas");
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
        actividad.setEstado("pendientes");
        actividad.setFecha(java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
        ref = FirebaseDatabase.getInstance().getReference().child(idUser).child("proyectos").child(padre).child("pendientes").child(actividad.getId());
        ref.setValue(actividad);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button btnAvanzar,btnRegresar,button4,button5;
        TextView tvActivityName,tvActivityDescription,tvFecha;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnAvanzar = itemView.findViewById(R.id.btnAvanzar);
            tvActivityName = itemView.findViewById(R.id.tvActivityName);
            tvActivityDescription = itemView.findViewById(R.id.tvActivityDescription);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            btnRegresar = itemView.findViewById(R.id.btnRegresar);
            button4 = itemView.findViewById(R.id.button4);
            button5 = itemView.findViewById(R.id.button5);

            button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, NewActivitie.class);
                    intent.putExtra("id", padre);
                    intent.putExtra("idUser", idUser);
                    intent.putExtra("idEdit", lista.get(getAdapterPosition()).getId());
                    intent.putExtra("estado",lista.get(getAdapterPosition()).getEstado());
                    context.startActivity(intent);
                }
            });
            button5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Eliminar");
                    builder.setMessage("¿Desea eliminar esta actividad?");
                    builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                if (lista.get(getAdapterPosition()).getEstado().equals("pendientes")) {
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(idUser).child("proyectos").child(padre).child("pendientes").child(lista.get(getAdapterPosition()).getId());
                                    ref.removeValue();
                                } else if (lista.get(getAdapterPosition()).getEstado().equals("inProgress")) {
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(idUser).child("proyectos").child(padre).child("inProgress").child(lista.get(getAdapterPosition()).getId());
                                    ref.removeValue();
                                } else {
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(idUser).child("proyectos").child(padre).child("finalizadas").child(lista.get(getAdapterPosition()).getId());
                                    ref.removeValue();
                                }
                                Toast.makeText(view.getContext(), "Actividad eliminada", Toast.LENGTH_SHORT).show();
                            }catch (Exception e) {
                                Toast.makeText(view.getContext(), "Error al eliminar", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();
                }
            });

        }
    }
}
