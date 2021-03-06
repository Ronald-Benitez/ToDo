package com.example.login.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.R;
import com.example.login.models.Actividad;
import com.example.login.models.Proyecto;
import com.example.login.models.updateProject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class NewProject extends AppCompatActivity {
    Button btnAdd,btnBack;
    EditText etProjectName,etProjectDescription;
    Switch sCompartir;
    TextView textView12;
    List<Actividad> pendientes = new ArrayList<>();
    List<Actividad> inProgress = new ArrayList<>();
    List<Actividad> finalizadas = new ArrayList<>();
    Proyecto proyecto = new Proyecto();
    String idUser,idProyecto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);
        Bundle bundle = getIntent().getExtras();
        idUser = bundle.getString("idUser");
        idProyecto = bundle.getString("idProyecto");


        //Seteo de views
        btnAdd = findViewById(R.id.btnAdd);
        btnBack = findViewById(R.id.btnBack);
        etProjectName = findViewById(R.id.etProjectName);
        etProjectDescription = findViewById(R.id.etProjectDescription);
        sCompartir = findViewById(R.id.sCompartir);
        textView12 = findViewById(R.id.textView12);

        //Base de datos
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        final FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        DatabaseReference ref2 = database2.getReference();
        if(idProyecto != null){
            btnAdd.setText("Editar");
            textView12.setText("Editar proyecto");
            ref2 = FirebaseDatabase.getInstance().getReference().child(idUser).child("proyectos").child(idProyecto);
            ref2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        etProjectName.setText(dataSnapshot.child("nombre").getValue().toString());
                        etProjectDescription.setText(dataSnapshot.child("descripcion").getValue().toString());
                        if (dataSnapshot.child("compartir").getValue().toString().equals("true")) {
                            sCompartir.setChecked(true);
                        } else {
                            sCompartir.setChecked(false);
                        }
                    }catch (Exception e){
                        Toast.makeText(NewProject.this, "Error al cargar el proyecto", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(NewProject.this, Projects.class));

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        //Listener de boton de agregar proyecto
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verificarDatos()){
                    if(idProyecto==null) {
                        ref.child(idUser).child("proyectos").child(proyecto.getId()).setValue(proyecto);
                        Toast.makeText(NewProject.this, "Proyecto agregado", Toast.LENGTH_LONG).show();
                        etProjectName.setText("");
                        etProjectDescription.setText("");
                    }else {
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("nombre",proyecto.getNombre());
                        map.put("descripcion",proyecto.getDescripcion());
                        if(sCompartir.isChecked()){
                            map.put("compartir","true");
                        }else{
                            map.put("compartir","false");
                        }
                        ref.child(idUser).child("proyectos").child(idProyecto).updateChildren(map);
                        Toast.makeText(NewProject.this, "Proyecto editado", Toast.LENGTH_LONG).show();
                    }
                    proyecto = new Proyecto();

                }
            }
        });

        //Listener de boton de volver
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewProject.this, Projects.class);
                intent.putExtra("idUser",idUser);
                startActivity(intent);
            }
        });
    }

    //M??todo que se activa al realizar la acci??n de regresar
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(NewProject.this, Projects.class);
        intent.putExtra("idUser",idUser);
        startActivity(intent);
    }

    //M??todo que verifica que los datos ingresados sean correctos
    public boolean verificarDatos(){
        if(etProjectName.getText().toString().isEmpty()){
            Toast.makeText(this, "Ingrese un nombre", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(etProjectDescription.getText().toString().isEmpty()){
            Toast.makeText(this, "Ingrese una descripcion", Toast.LENGTH_SHORT).show();
            return false;
        }
        proyecto.setNombre(etProjectName.getText().toString());
        proyecto.setDescripcion(etProjectDescription.getText().toString());
        proyecto.setPendientes(pendientes);
        proyecto.setInProgress(inProgress);
        proyecto.setFinalizadas(finalizadas);
        proyecto.setId(UUID.randomUUID().toString());
        if(sCompartir.isChecked()) {
            proyecto.setCompartir("true");
        }else{
            proyecto.setCompartir("false");
        }
        return true;
    }
}