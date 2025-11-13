package com.example.semana13;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // Declarar las vistas y la referencia a la base de datos
    EditText txtEjercicio, txtSeries, txtRepeticiones, txtPeso;
    Button btnGuardar;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Carga el layout XML

        // Inicializar la referencia a Firebase
        // Apuntamos a la raíz de la base de datos
        database = FirebaseDatabase.getInstance().getReference();

        // Enlazar las vistas del XML con las variables Java
        txtEjercicio = findViewById(R.id.txtEjercicio);
        txtSeries = findViewById(R.id.txtSeries);
        txtRepeticiones = findViewById(R.id.txtRepeticiones);
        txtPeso = findViewById(R.id.txtPeso);
        btnGuardar = findViewById(R.id.btnGuardar);

        // Configurar el listener para el botón
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los datos de los EditText
                String ejercicio = txtEjercicio.getText().toString();
                String series = txtSeries.getText().toString();
                String repeticiones = txtRepeticiones.getText().toString();
                String peso = txtPeso.getText().toString();

                // Validar que no estén vacíos
                if (ejercicio.isEmpty() || series.isEmpty() || repeticiones.isEmpty() || peso.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Guardar los datos en Firebase
                guardarRegistroEjercicio(ejercicio, series, repeticiones, peso);
            }
        });
    }

    private void guardarRegistroEjercicio(String ejercicio, String series, String repeticiones, String peso) {
        // Crear un HashMap para guardar los datos (similar a un objeto JSON)
        Map<String, Object> registro = new HashMap<>();
        registro.put("ejercicio", ejercicio);
        registro.put("series", Integer.parseInt(series)); // Convertir a número si se desea
        registro.put("repeticiones", Integer.parseInt(repeticiones)); // Convertir a número
        registro.put("peso_kg", Double.parseDouble(peso)); // Convertir a número decimal

        // Vamos a guardar esto bajo un nuevo "nodo" llamado "registros_gym"
        // .push() crea un ID único para cada entrada (como un ID de registro)
        database.child("registros_gym").push().setValue(registro)
                .addOnSuccessListener(aVoid -> {
                    // Éxito
                    Toast.makeText(MainActivity.this, "Registro guardado", Toast.LENGTH_SHORT).show();
                    // Limpiar campos
                    txtEjercicio.setText("");
                    txtSeries.setText("");
                    txtRepeticiones.setText("");
                    txtPeso.setText("");
                })
                .addOnFailureListener(e -> {
                    // Error
                    Toast.makeText(MainActivity.this, "Error al guardar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}