package com.example.login.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.activities.ProjectDetail;
import com.example.login.models.Proyecto;

import java.util.ArrayList;
import java.util.List;

public class ProjectList extends RecyclerView.Adapter<ProjectList.ProjectViewHolder> {
    List<Proyecto> projectList;

    public ProjectList(List<Proyecto> projectList) {
        this.projectList = projectList;
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
        return projectList.size();
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
        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProjectName = itemView.findViewById(R.id.tvProjectName);
            tvProjectDescription = itemView.findViewById(R.id.tvProjectDescription);
            tvCompleted = itemView.findViewById(R.id.tvCompleted);
            llProject = itemView.findViewById(R.id.llProject);
            llProject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ProjectDetail.class);
                    intent.putExtra("id", projectList.get(getAdapterPosition()).getId());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
