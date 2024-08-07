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
import android.widget.Toast;

import java.util.ArrayList;

public class listaCompra extends AppCompatActivity implements View.OnClickListener {

    ListView listCompra;
    Button btCompra, btVolver;
    EditText etCompra;
    DBHelper bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lista_compra);

        //Recuperamos los componentes de la Interfaz
        listCompra = findViewById(R.id.list);
        btCompra = findViewById(R.id.btAñadirCompra);
        btVolver = findViewById(R.id.btVolver);
        etCompra = findViewById(R.id.editTextAñadirCompra);
        bd = new DBHelper(this);

        btCompra.setOnClickListener(this);
        btVolver.setOnClickListener(this);

        //Mostramos la lista de compras al iniciar la actividad
        mostrarListaCompra();

        listCompra.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                @SuppressLint("Range") String nombreAlimento = cursor.getString(cursor.getColumnIndex("Nombre"));

                //Llamamos al método para eliminar el alimento de la lista de compras
                int filasEliminadas = bd.eliminarAlimentoCompra(nombreAlimento);

                if (filasEliminadas > 0) {
                    CustomToastUtil.mostrarToastPersonalizado(getApplicationContext(), "Alimento eliminado de la lista de compras");
                    //Actualizamos la lista de compras después de eliminar
                    mostrarListaCompra();
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
        if (v.getId() == R.id.btAñadirCompra) {
            //Obtenemos el nombre del alimento introducido por el usuario
            String nombreCompra = etCompra.getText().toString().trim();
            //Comprobamos que los campos no estén vacíos
            if (!nombreCompra.isEmpty()) {
                //Comprobamos si el alimento existe
                if (existeAlimento(nombreCompra)){
                    CustomToastUtil.mostrarToastPersonalizado(this, "El alimento ya existe");
                    return;
                }
                //Sino insertamos la compra en la base de datos
                long resultado = bd.insertarCompra(nombreCompra);
                //Comprobamos si la inserción se realizó
                if (resultado != -1) {
                    CustomToastUtil.mostrarToastPersonalizado(this, "Elemento añadido");
                    //Limpiamos los EditText después de agregar la compra
                    etCompra.setText("");
                    //Mostramos la lista actualizada de compras
                    mostrarListaCompra();
                } else {
                    CustomToastUtil.mostrarToastPersonalizado(listaCompra.this, "Error al añadir elemento");
                }
            } else {
                CustomToastUtil.mostrarToastPersonalizado(listaCompra.this, "Por favor ingrese un elemento");
            }
        }
        else if (v.getId() == R.id.btVolver) {
            Intent intent = new Intent(this, Conectado.class);
            //Iniciamos la actividad que hay en el intent
            startActivity(intent);
        }
    }

    //Método para mostrar la lista de compras
    private void mostrarListaCompra() {
        Cursor cursor = bd.getCompraRaw();
        if (cursor != null) {
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"Nombre"},
                    new int[]{android.R.id.text1},
                    0
            );
            listCompra.setAdapter(adapter);
        } else {
            CustomToastUtil.mostrarToastPersonalizado(this, "No hay datos disponibles");
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
        Cursor cursor = db.query(bd.getTablaCompra(), columns, selection, selectionArgs, null, null, null);
        //Verificamos si el cursor tiene al menos una fila
        boolean alimentoExists = cursor.moveToFirst();
        cursor.close();
        //Devuelve true si existe y false sino
        return alimentoExists;
    }
}