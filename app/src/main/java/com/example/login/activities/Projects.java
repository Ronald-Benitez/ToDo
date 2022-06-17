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
import com.example.login.models.Actividad;
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
    String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);
        Bundle bundle = getIntent().getExtras();
        idUser = bundle.getString("idUser");

        //Seteo de views
        rvProjects = findViewById(R.id.rvProjects);
        add = findViewById(R.id.addProject);

        //Recycle view management
        proyectos = new ArrayList<>();
        rvProjects.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        ProjectList adapter = new ProjectList(proyectos,idUser);
        rvProjects.setAdapter(adapter);

        //Base de datos
        ref = FirebaseDatabase.getInstance().getReference().child(idUser).child("proyectos");
        rvProjects.setHasFixedSize(true);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                proyectos.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    Proyecto p = new Proyecto();
                    p.setId(ds.child("id").getValue(String.class));
                    p.setNombre(ds.child("nombre").getValue(String.class));
                    p.setDescripcion(ds.child("descripcion").getValue(String.class));

                    List<Actividad> actividades = new ArrayList<>();
                    try {
                        for(DataSnapshot ds2: ds.child("pendientes").getChildren()) {
                            Actividad a = ds2.getValue(Actividad.class);
                            actividades.add(a);
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                        actividades = new ArrayList<>(0);
                    }
                    p.setPendientes(actividades);

                    try{
                        actividades = new ArrayList<>();
                        for(DataSnapshot ds2: ds.child("inProgress").getChildren()) {
                            Actividad a = ds2.getValue(Actividad.class);
                            actividades.add(a);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        actividades = new ArrayList<>(0);
                    }
                    p.setInProgress(actividades);

                    try {
                        actividades = new ArrayList<>();
                        for(DataSnapshot ds2: ds.child("finalizadas").getChildren()) {
                            Actividad a = ds2.getValue(Actividad.class);
                            actividades.add(a);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        actividades = new ArrayList<>(0);
                    }
                    p.setFinalizadas(actividades);


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
                Intent intent = new Intent(Projects.this, NewProject.class);
                intent.putExtra("idUser", idUser);
                startActivity(intent);
            }
        });

    }
}