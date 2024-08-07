package com.example.gestco;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ListaComidas extends AppCompatActivity implements View.OnClickListener {

    private EditText etLunesComida, etLunesCena, etMartesComida, etMartesCena;
    private EditText etMiercolesComida, etMiercolesCena, etJuevesComida, etJuevesCena;
    private EditText etViernesComida, etViernesCena, etSabadoComida, etSabadoCena;
    private EditText etDomingoComida, etDomingoCena;
    private Button btGuardar, btVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_comidas);

        //Recuperamos los componentes de la interfaz
        etLunesComida = findViewById(R.id.etLunesComida);
        etLunesCena = findViewById(R.id.etLunesCena);
        etMartesComida = findViewById(R.id.etMartesComida);
        etMartesCena = findViewById(R.id.etMartesCena);
        etMiercolesComida = findViewById(R.id.etMiercolesComida);
        etMiercolesCena = findViewById(R.id.etMiercolesCena);
        etJuevesComida = findViewById(R.id.etJuevesComida);
        etJuevesCena = findViewById(R.id.etJuevesCena);
        etViernesComida = findViewById(R.id.etViernesComida);
        etViernesCena = findViewById(R.id.etViernesCena);
        etSabadoComida = findViewById(R.id.etSabadoComida);
        etSabadoCena = findViewById(R.id.etSabadoCena);
        etDomingoComida = findViewById(R.id.etDomingoComida);
        etDomingoCena = findViewById(R.id.etDomingoCena);
        btGuardar = findViewById(R.id.btGuardar);
        btVolver = findViewById(R.id.btVolverComidas);

        //Recuperamos los ficheros
        String archivos [] = fileList();

        //Verificamos si el archivo existe
        if(ArchivoExiste(archivos, "comidas.txt")){
            try {
                //Intentamos leer el archivo "comidas.txt" y mostramos los datos en los EditText correspondientes
                InputStreamReader archivo = new InputStreamReader(openFileInput("comidas.txt"));
                BufferedReader br = new BufferedReader(archivo);
                String linea;
                int contador = 0;

                //Leemos cada línea del archivo y la asignamos al EditText correspondiente
                while ((linea = br.readLine())!= null && contador < 14) {
                    switch (contador){
                        case 0:
                            etLunesComida.setText(linea);
                            break;
                        case 1:
                            etLunesCena.setText(linea);
                            break;
                        case 2:
                            etMartesComida.setText(linea);
                            break;
                        case 3:
                            etMartesCena.setText(linea);
                            break;
                        case 4:
                            etMiercolesComida.setText(linea);
                            break;
                        case 5:
                            etMiercolesCena.setText(linea);
                             break;
                        case 6:
                            etJuevesComida.setText(linea);
                            break;
                        case 7:
                            etJuevesCena.setText(linea);
                            break;
                        case 8:
                            etViernesComida.setText(linea);
                            break;
                        case 9:
                            etViernesCena.setText(linea);
                            break;
                        case 10:
                            etSabadoComida.setText(linea);
                            break;
                        case 11:
                            etSabadoCena.setText(linea);
                            break;
                        case 12:
                            etDomingoComida.setText(linea);
                            break;
                        case 13:
                            etDomingoCena.setText(linea);
                            break;
                    }
                    contador ++;
                }
                //Cerramos el lector del archivo y el archivo
                br.close();
                archivo.close();

            } catch (IOException e) {

            }

        }
    }
    //Método para comprobar si el archivo existe
    private boolean ArchivoExiste(String archivos [], String NombreArchivo){
        for(int i = 0; i < archivos.length; i++)
            if(NombreArchivo.equals(archivos[i]))
                return true;
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btGuardar){
            try {
                //Abrimos el archivo "comidas.txt" para escritura
                OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput("comidas.txt", Activity.MODE_PRIVATE));
                //Escribimos los datos de los EditText en el archivo
                archivo.write(etLunesComida.getText().toString() + "\n");
                archivo.write(etLunesCena.getText().toString() + "\n");
                archivo.write(etMartesComida.getText().toString() + "\n");
                archivo.write(etMartesCena.getText().toString() + "\n");
                archivo.write(etMiercolesComida.getText().toString() + "\n");
                archivo.write(etMiercolesCena.getText().toString() + "\n");
                archivo.write(etJuevesComida.getText().toString() + "\n");
                archivo.write(etJuevesCena.getText().toString() + "\n");
                archivo.write(etViernesComida.getText().toString() + "\n");
                archivo.write(etViernesCena.getText().toString() + "\n");
                archivo.write(etSabadoComida.getText().toString() + "\n");
                archivo.write(etSabadoCena.getText().toString() + "\n");
                archivo.write(etDomingoComida.getText().toString() + "\n");
                archivo.write(etDomingoCena.getText().toString() + "\n");
                archivo.flush();
                archivo.close();
                CustomToastUtil.mostrarToastPersonalizado(this, "Guardado Correctamente");
            } catch (IOException e) {
                CustomToastUtil.mostrarToastPersonalizado(this, "Error al Guardar");
            }


        }
        else if (v.getId() == R.id.btVolverComidas){
            Intent intent = new Intent(this, Conectado.class);
            //Iniciamos la actividad que hay en el intent
            startActivity(intent);
        }

    }
}