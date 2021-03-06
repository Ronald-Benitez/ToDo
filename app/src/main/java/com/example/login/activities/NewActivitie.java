package com.example.login.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.R;
import com.example.login.models.Actividad;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.UUID;

public class NewActivitie extends AppCompatActivity {
    Button btnAddActivity,btnBack;
    EditText etActivityName,etActivityDescription;
    String idProyecto;
    TextView textView14;
    Actividad actividad = new Actividad();
    String idUser,idEdit,estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_activitie);
        //Id
        Bundle bundle = getIntent().getExtras();
        idProyecto = bundle.getString("id");
        idUser = bundle.getString("idUser");
        idEdit = bundle.getString("idEdit");
        estado = bundle.getString("estado");

        //Seteo de views
        btnAddActivity = findViewById(R.id.btnAddActivity);
        btnBack = findViewById(R.id.btnBack);
        etActivityName = findViewById(R.id.etActivityName);
        etActivityDescription = findViewById(R.id.etActivityDescription);
        textView14 = findViewById(R.id.textView14);

        //Base de datos
        final FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        DatabaseReference ref2 = database2.getReference();

        //Editar
        if(idEdit != null) {
            btnAddActivity.setText("Editar");
            textView14.setText("Editar Actividad");
            try {
                ref2 = FirebaseDatabase.getInstance().getReference().child(idUser).child("proyectos").child(idProyecto).child(estado).child(idEdit);
                ref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        actividad = dataSnapshot.getValue(Actividad.class);
                        try {
                            etActivityName.setText(actividad.getNombre());
                            etActivityDescription.setText(actividad.getDescripcion());
                        }catch (Exception e) {
                            Toast.makeText(NewActivitie.this, "Error al cargar actividad", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(NewActivitie.this, ProjectDetail.class);
                            intent.putExtra("id", idProyecto);
                            intent.putExtra("idUser", idUser);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }catch (Exception e){
                Toast.makeText(this, "Error al cargar la actividad", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, Projects.class));
            }
        }

        //Base de datos
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        //Listener de boton de agregar actividad
        btnAddActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "Actividad agregada";
                if (verificarDatos()) {
                    if(idEdit != null) {
                        actividad.setId(idEdit);
                        message = "Actividad editada";
                    }else{
                        etActivityName.setText("");
                        etActivityDescription.setText("");
                    }
                    ref.child(idUser).child("proyectos").child(idProyecto).child("pendientes").child(actividad.getId()).setValue(actividad);
                    Toast.makeText(NewActivitie.this, message, Toast.LENGTH_LONG).show();
                    actividad = new Actividad();

                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewActivitie.this, ProjectDetail.class);
                intent.putExtra("id", idProyecto);
                intent.putExtra("idUser", idUser);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }

    //Metodo para verificar datos no vacios
    public boolean verificarDatos() {
        if (etActivityName.getText().toString().isEmpty()) {
            Toast.makeText(this, "El nombre de la actividad no debe estar vacio", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etActivityDescription.getText().toString().isEmpty()) {
            Toast.makeText(this, "La descripci??n de la actividad no debe estar vacia", Toast.LENGTH_SHORT).show();
            return false;
        }
        actividad.setNombre(etActivityName.getText().toString());
        actividad.setDescripcion(etActivityDescription.getText().toString());
        actividad.setId(UUID.randomUUID().toString());
        actividad.setFecha(java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
        actividad.setEstado("pendientes");

        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(NewActivitie.this,ProjectDetail.class);
        intent.putExtra("id",idProyecto);
        intent.putExtra("idUser",idUser);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}