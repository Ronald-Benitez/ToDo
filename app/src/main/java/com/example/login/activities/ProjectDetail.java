package com.example.login.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.R;
import com.example.login.adapters.PendientesList;
import com.example.login.models.Actividad;
import com.example.login.models.Proyecto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProjectDetail extends AppCompatActivity {
    TextView tvProjectName,tvProjectDescription;
    RecyclerView rvPendientes,rvInProgress,rvFinalizadas;
    Button addActividad,btnRegresar;
    Proyecto proyecto;
    DatabaseReference ref;
    List<Actividad> pendientes,inProgress,finalizadas;
    String idUser;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);
        Bundle bundle = getIntent().getExtras();
        idUser = bundle.getString("idUser");
        firebaseAuth = FirebaseAuth.getInstance();

        //Seteo de views
        tvProjectName = findViewById(R.id.tvProjectName);
        tvProjectDescription = findViewById(R.id.tvProjectDescription);
        rvPendientes = findViewById(R.id.rvPendientes);
        rvInProgress = findViewById(R.id.rvInProgress);
        rvFinalizadas = findViewById(R.id.rvFinalizadas);
        addActividad = findViewById(R.id.addActividad);
        btnRegresar = findViewById(R.id.btnRegresar);

        //Listas
        pendientes = new ArrayList<>();
        inProgress = new ArrayList<>();
        finalizadas = new ArrayList<>();

        //Recycle view management
        rvPendientes.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        rvInProgress.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        rvFinalizadas.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        rvFinalizadas.setHasFixedSize(true);
        rvInProgress.setHasFixedSize(true);
        rvPendientes.setHasFixedSize(true);

        try {
            //Base de datos
            ref = FirebaseDatabase.getInstance().getReference().child(idUser).child("proyectos").child(bundle.getString("id"));
        }catch (Exception e) {
            Toast.makeText(this, "Error al cargar el proyecto", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,Projects.class));
        }

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    Toast.makeText(ProjectDetail.this, "Error al cargar el proyecto", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ProjectDetail.this,Projects.class));
                }

                proyecto = new Proyecto();
                proyecto.setId(snapshot.child("id").getValue(String.class));
                proyecto.setNombre(snapshot.child("nombre").getValue(String.class));
                proyecto.setDescripcion(snapshot.child("descripcion").getValue(String.class));
                proyecto.setCompartir(snapshot.child("compartir").getValue(Boolean.class));
                if(!proyecto.getCompartir() && !idUser.equals(firebaseAuth.getCurrentUser().getUid())) {
                    Toast.makeText(ProjectDetail.this, "Proyecto inhabilidato para compartir", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ProjectDetail.this, Projects.class));
                }
                pendientes = new ArrayList<>();
                try {
                    for(DataSnapshot ds2: snapshot.child("pendientes").getChildren()) {
                        Actividad a = ds2.getValue(Actividad.class);
                        pendientes.add(a);
                    }
                    Log.i("Pendientes",pendientes.get(0).getNombre());
                    PendientesList pendientesList = new PendientesList(pendientes,bundle.getString("id"),idUser);
                    rvPendientes.setAdapter(pendientesList);
                }catch(Exception e){
                    e.printStackTrace();
                    pendientes = new ArrayList<>(0);
                    PendientesList pendientesList = new PendientesList(pendientes,bundle.getString("id"),idUser);
                    rvPendientes.setAdapter(pendientesList);
                }

                try{
                    inProgress = new ArrayList<>();
                    for(DataSnapshot ds2: snapshot.child("inProgress").getChildren()) {
                        Actividad a = ds2.getValue(Actividad.class);
                        inProgress.add(a);
                    }
                    PendientesList inProgressList = new PendientesList(inProgress,bundle.getString("id"),idUser);
                    rvInProgress.setAdapter(inProgressList);
                }catch (Exception e){
                    e.printStackTrace();
                    inProgress = new ArrayList<>(0);
                    PendientesList inProgressList = new PendientesList(inProgress,bundle.getString("id"),idUser);
                    rvInProgress.setAdapter(inProgressList);
                }


                try {
                    finalizadas = new ArrayList<>();
                    for(DataSnapshot ds2: snapshot.child("finalizadas").getChildren()) {
                        Actividad a = ds2.getValue(Actividad.class);
                        finalizadas.add(a);
                    }
                    PendientesList finalizadasList = new PendientesList(finalizadas,bundle.getString("id"),idUser);
                    rvFinalizadas.setAdapter(finalizadasList);
                }catch (Exception e){
                    e.printStackTrace();
                    finalizadas = new ArrayList<>(0);
                    PendientesList finalizadasList = new PendientesList(finalizadas,bundle.getString("id"),idUser);
                    rvFinalizadas.setAdapter(finalizadasList);
                }

                tvProjectName.setText(proyecto.getNombre());
                tvProjectDescription.setText(proyecto.getDescripcion());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProjectDetail.this, NewActivitie.class);
                intent.putExtra("id", proyecto.getId());
                intent.putExtra("idUser", idUser);
                startActivity(intent);
            }

        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectDetail.this, Projects.class);
                intent.putExtra("idUser", idUser);
                startActivity(intent);
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProjectDetail.this, Projects.class);
        intent.putExtra("idUser", idUser);
        startActivity(intent);
    }
}