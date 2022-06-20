package com.example.login.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.activities.NewProject;
import com.example.login.activities.ProjectDetail;
import com.example.login.models.Proyecto;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ProjectList extends RecyclerView.Adapter<ProjectList.ProjectViewHolder> {
    List<Proyecto> projectList;
    String idUser;
    public ProjectList(List<Proyecto> projectList, String idUser) {
        this.projectList = projectList;
        this.idUser = idUser;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_item, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        int valor = 0;

        if(projectList.get(position).getFinalizadas() != null) {
            valor = projectList.get(position).getFinalizadas().size();
        }

        String dato = String.valueOf(valor) + "/" + String.valueOf(conteo(position));
        if(valor>0) {
            valor = valor * 100 / conteo(position);
        }
        dato+= " (" + String.valueOf(valor) + "%)";

        holder.tvProjectName.setText(projectList.get(position).getNombre());
        holder.tvProjectDescription.setText(projectList.get(position).getDescripcion());
        holder.tvCompleted.setText(dato);

    }

    @Override
    public int getItemCount() {
        try {
            return projectList.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public int conteo(int position){
        Proyecto pro = projectList.get(position);
        int conteo=0;

        if(pro.getFinalizadas()!=null){
            conteo = pro.getFinalizadas().size();
        }
        if(pro.getInProgress()!=null){
            conteo += pro.getInProgress().size();
        }
        if(pro.getPendientes()!=null){
            conteo += pro.getPendientes().size();
        }

        return conteo;
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder {
        TextView tvProjectName, tvProjectDescription,tvCompleted;
        LinearLayout llProject;
        Button button2,button,button3;
        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProjectName = itemView.findViewById(R.id.tvProjectName);
            tvProjectDescription = itemView.findViewById(R.id.tvProjectDescription);
            tvCompleted = itemView.findViewById(R.id.tvCompleted);
            llProject = itemView.findViewById(R.id.llProject);
            button2 = itemView.findViewById(R.id.button2);
            button = itemView.findViewById(R.id.button);
            button3 = itemView.findViewById(R.id.button3);

            llProject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ProjectDetail.class);
                    intent.putExtra("id", projectList.get(getAdapterPosition()).getId());
                    intent.putExtra("idUser",idUser);
                    view.getContext().startActivity(intent);
                }
            });
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pas = idUser + "/" + projectList.get(getAdapterPosition()).getId();
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, pas);
                    sendIntent.setType("text/plain");
                    Intent shareIntent = Intent.createChooser(sendIntent, "Compartir proyecto");
                    view.getContext().startActivity(shareIntent);
                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), NewProject.class);
                    intent.putExtra("idProyecto", projectList.get(getAdapterPosition()).getId());
                    intent.putExtra("idUser",idUser);
                    view.getContext().startActivity(intent);
                }
            });

            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Eliminar proyecto");
                    builder.setMessage("¿Está seguro de eliminar este proyecto?");
                    builder.setPositiveButton("Eliminar", (dialog, which) -> {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(idUser).child("proyectos").child(projectList.get(getAdapterPosition()).getId());
                        ref.removeValue();
                    });
                    builder.setNegativeButton("Cancelar", (dialog, which) -> {
                        dialog.dismiss();
                    });
                    builder.show();
                }
            });
        }
    }
}
