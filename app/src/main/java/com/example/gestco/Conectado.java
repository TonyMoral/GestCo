package com.example.gestco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class Conectado extends AppCompatActivity implements View.OnClickListener{

    Button btSalir;
    ImageButton ibNevera, ibCongelador, ibCompra, ibComida, ibDespensa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conectado);

        //Recuperamos los componentes de la Interfaz
        btSalir = findViewById(R.id.btSalir);
        ibComida = findViewById(R.id.imageButtonComidas);
        ibCompra = findViewById(R.id.imageButtonCompra);
        ibCongelador = findViewById(R.id.imageButtonCongelador);
        ibDespensa = findViewById(R.id.imageButtonDespensa);
        ibNevera = findViewById(R.id.imageButtonNevera);

    }

    @Override
    public void onClick(View v) {
        //Si se pulsa el botón de Salir
        if (v.getId() == R.id.btSalir) {
            //Se accede a la ventana principal
            Intent intent = new Intent(this, MainActivity.class);
            //Iniciamos la actividad que hay en el intent
            startActivity(intent);

        } else if (v.getId() == R.id.imageButtonNevera) {
            //Se accede a la ventana principal
            Intent intent = new Intent(this, Nevera.class);
            //Iniciamos la actividad que hay en el intent
            startActivity(intent);

        } else if (v.getId() == R.id.imageButtonCompra) {
            //Se accede a la ventana principal
            Intent intent = new Intent(this, listaCompra.class);
            //Iniciamos la actividad que hay en el intent
            startActivity(intent);

        } else if (v.getId() == R.id.imageButtonComidas) {
            //Se accede a la ventana principal
            Intent intent = new Intent(this, ListaComidas.class);
            //Iniciamos la actividad que hay en el intent
            startActivity(intent);
        }
        else if (v.getId() == R.id.imageButtonCongelador) {
            //Se accede a la ventana principal
            Intent intent = new Intent(this, congelador.class);
            //Iniciamos la actividad que hay en el intent
            startActivity(intent);
        }
        else if (v.getId() == R.id.imageButtonDespensa) {
            //Se accede a la ventana principal
            Intent intent = new Intent(this, Despensa.class);
            //Iniciamos la actividad que hay en el intent
            startActivity(intent);
        }
    }
}