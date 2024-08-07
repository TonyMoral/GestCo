package com.example.gestco;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class congelador extends AppCompatActivity implements View.OnClickListener {

    ListView listCongelador;
    Button btAñadirCongelador, btVolverCongelador;
    EditText etCongelador, etLocalizacion;
    DBHelper bd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congelador);
        //Recuperamos los componentes de la interfaz
        listCongelador = findViewById(R.id.listCongelador);
        btAñadirCongelador = findViewById(R.id.btAñadirCongelador);
        btVolverCongelador = findViewById(R.id.btVolverCongelador);
        etCongelador = findViewById(R.id.editTextCongelador);
        etLocalizacion = findViewById(R.id.editTextLocalizacion);
        bd = new DBHelper(this);

        btVolverCongelador.setOnClickListener(this);
        btAñadirCongelador.setOnClickListener(this);

        //Mostramos la lista del congelador al iniciar la actividad
        mostrarListaCongelador();

        listCongelador.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                @SuppressLint("Range") String nombreAlimento = cursor.getString(cursor.getColumnIndex("Nombre"));

                //Llamamos al método para eliminar el alimento de la lista
                int filasEliminadas = bd.eliminarAlimentoCongelador(nombreAlimento);

                if (filasEliminadas > 0) {
                    CustomToastUtil.mostrarToastPersonalizado(getApplicationContext(), "Alimento eliminado de la lista del congelador");
                    //Actualizamos la lista después de eliminar
                    mostrarListaCongelador();
                } else {
                    CustomToastUtil.mostrarToastPersonalizado(getApplicationContext(), "No se pudo eliminar el alimento");
                }

                //Indicamos que el evento ha sido manejado correctamente
                return true;
            }
        });

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btAñadirCongelador) {
            //Obtenemos el nombre del alimento introducido por el usuario
            String nombreCongelador = etCongelador.getText().toString().trim();
            String localizacion = etLocalizacion.getText().toString().trim();
            //Comprobamos que el campo nombre no esté vacíos
            if (!nombreCongelador.isEmpty()) {
                //Comprobamos si el alimento existe
                if (existeAlimento(nombreCongelador)){
                    CustomToastUtil.mostrarToastPersonalizado(this, "El alimento ya existe");
                    return;
                }
                //Si no, insertamos el alimento en la base de datos
                long resultado;
                if (!localizacion.isEmpty()) {
                    resultado = bd.insertarCongelador(nombreCongelador, localizacion);
                } else {
                    resultado = bd.insertarCongelador(nombreCongelador, null);
                }
                //Comprobamos si la inserción se realizó
                if (resultado != -1) {
                    CustomToastUtil.mostrarToastPersonalizado(this,"Elemento añadido");
                    //Limpiamos los EditText después de agregar el alimento
                    etCongelador.setText("");
                    etLocalizacion.setText("");
                    //Mostramos la lista actualizada del congelador
                    mostrarListaCongelador();
                } else {
                    CustomToastUtil.mostrarToastPersonalizado(this, "Error al añadir elemento");
                }
            } else {
                CustomToastUtil.mostrarToastPersonalizado(this, "Por favor ingrese un alimento");
            }
        } else if (v.getId() == R.id.btVolverCongelador) {
            Intent intent = new Intent(this, Conectado.class);
            //Iniciamos la actividad que hay en el intent
            startActivity(intent);
        }
    }
    private void mostrarListaCongelador() {
        Cursor cursor = bd.getCongeladorRaw();
        if (cursor != null) {
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    this,
                    android.R.layout.simple_list_item_2,
                    cursor,
                    new String[]{"Nombre", "Localizacion"},
                    new int[]{android.R.id.text1, android.R.id.text2},
                    0
            );
            listCongelador.setAdapter(adapter);
        } else {
            CustomToastUtil.mostrarToastPersonalizado(this, "No hay datos disponibles en el congelador");
        }
    }
    //Metodo para comprobar si existe ya un alimento con ese nombre
    private boolean existeAlimento(String nombreAlimento) {
        //Obtenemos la referencia a la base de datos en modo lectura
        SQLiteDatabase db = bd.getReadableDatabase();
        //Definimos las columnas que queremos recuperar
        String[] columns = {"Nombre"};
        //Definimos la clausula where
        String selection = "Nombre=?";
        String[] selectionArgs = {nombreAlimento};
        //Realizamos la consulta en la base de datos
        Cursor cursor = db.query(bd.getTablaCongelador(), columns, selection, selectionArgs, null, null, null);
        //Verificamos si el cursor tiene al menos una fila
        boolean alimentoExists = cursor.moveToFirst();
        cursor.close();
        //Devuelve true si existe y false sino
        return alimentoExists;
    }
}