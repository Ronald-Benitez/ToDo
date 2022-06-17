package com.example.login.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.login.R;
import com.example.login.adapters.ProjectList;
import com.example.login.models.Proyecto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Projects extends AppCompatActivity {
    RecyclerView rvProjects;
    Button add;
    DatabaseReference ref ;
    List<Proyecto> proyectos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        //Seteo de views
        rvProjects = findViewById(R.id.rvProjects);
        add = findViewById(R.id.addProject);

        //Recycle view management
        proyectos = new ArrayList<>();
        rvProjects.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        ProjectList adapter = new ProjectList(proyectos);
        rvProjects.setAdapter(adapter);

        //Base de datos
        ref = FirebaseDatabase.getInstance().getReference().child("proyectos");
        rvProjects.setHasFixedSize(true);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                proyectos.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    Proyecto p = ds.getValue(Proyecto.class);
                    proyectos.add(p);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Listener de boton de agregar proyecto
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Projects.this, NewProject.class));
            }
        });

    }
}