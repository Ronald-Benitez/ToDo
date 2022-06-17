package com.example.login.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.login.R;
import com.example.login.models.Actividad;
import com.example.login.models.Proyecto;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NewProject extends AppCompatActivity {
    Button btnAdd,btnBack;
    EditText etProjectName,etProjectDescription;
    List<Actividad> pendientes = new ArrayList<>();
    List<Actividad> inProgress = new ArrayList<>();
    List<Actividad> finalizadas = new ArrayList<>();
    Proyecto proyecto = new Proyecto();
    String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);
        Bundle bundle = getIntent().getExtras();
        idUser = bundle.getString("idUser");

        //Seteo de views
        btnAdd = findViewById(R.id.btnAdd);
        btnBack = findViewById(R.id.btnBack);
        etProjectName = findViewById(R.id.etProjectName);
        etProjectDescription = findViewById(R.id.etProjectDescription);

        //Base de datos
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        //Listener de boton de agregar proyecto
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verificarDatos()){
                    ref.child(idUser).child("proyectos").child(proyecto.getId()).setValue(proyecto);
                    Toast.makeText(NewProject.this,"Proyecto agregado",Toast.LENGTH_LONG).show();
                    proyecto = new Proyecto();
                    etProjectName.setText("");
                    etProjectDescription.setText("");
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

    //Método que se activa al realizar la acción de regresar
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(NewProject.this, Projects.class);
        intent.putExtra("idUser",idUser);
        startActivity(intent);
    }

    //Método que verifica que los datos ingresados sean correctos
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
        return true;
    }
}