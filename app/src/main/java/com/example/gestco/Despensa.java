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

public class Despensa extends AppCompatActivity implements View.OnClickListener {

    ListView listDespensa;
    Button btAñadirDespensa, btVolverDespensa;
    EditText etDespensa, etDescripcion;
    DBHelper bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despensa);
        //Recuperamos los componentes de la interfaz
        listDespensa = findViewById(R.id.listDespensa);
        btAñadirDespensa = findViewById(R.id.btAñadirDespensa);
        btVolverDespensa = findViewById(R.id.btVolverDespensa);
        etDespensa = findViewById(R.id.editTextDespensa);
        etDescripcion = findViewById(R.id.editTextDescripcion);
        bd = new DBHelper(this);

        btVolverDespensa.setOnClickListener(this);
        btAñadirDespensa.setOnClickListener(this);

        //Mostramos la lista de la despensa al iniciar la actividad
        mostrarListaDespensa();

        listDespensa.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                @SuppressLint("Range") String nombreAlimento = cursor.getString(cursor.getColumnIndex("Nombre"));
                //Llamamos al método para eliminar el alimento de la lista
                int filasEliminadas = bd.eliminarAlimentoDespensa(nombreAlimento);
                if (filasEliminadas > 0) {
                    CustomToastUtil.mostrarToastPersonalizado(getApplicationContext(), "Alimento eliminado de la lista de la Despensa");
                    //Actualizamos la lista de después de eliminar
                    mostrarListaDespensa();
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
        if (v.getId() == R.id.btAñadirDespensa) {
            //Obtenemos el nombre del alimento introducido por el usuario
            String nombreDespensa = etDespensa.getText().toString().trim();
            String descripcion = etDescripcion.getText().toString().trim();
            //Comprobamos que el campo nombre no esté vacíos
            if (!nombreDespensa.isEmpty()) {
                //Comprobamos si el alimento existe
                if (existeAlimento(nombreDespensa)){
                    CustomToastUtil.mostrarToastPersonalizado(this, "El alimento ya existe");
                    return;
                }
                //Si no, insertamos el alimento en la base de datos
                long resultado;
                if (!descripcion.isEmpty()) {
                    resultado = bd.insertarDespensa(nombreDespensa, descripcion);
                } else {
                    resultado = bd.insertarDespensa(nombreDespensa, null);
                }
                //Comprobamos si la inserción se realizó
                if (resultado != -1) {
                    CustomToastUtil.mostrarToastPersonalizado(this,"Elemento añadido");
                    //Limpiamos los EditText después de agregar el alimento
                    etDespensa.setText("");
                    etDescripcion.setText("");
                    //Mostramos la lista actualizada de la despensa
                    mostrarListaDespensa();
                } else {
                    CustomToastUtil.mostrarToastPersonalizado(this, "Error al añadir elemento");
                }
            } else {
                CustomToastUtil.mostrarToastPersonalizado(this, "Por favor ingrese un alimento");
            }
        } else if (v.getId() == R.id.btVolverDespensa) {
            Intent intent = new Intent(this, Conectado.class);
            //Iniciamos la actividad que hay en el intent
            startActivity(intent);
        }
    }
    //Metodo para mostrar la lista de la despensa
    private void mostrarListaDespensa(){
        Cursor cursor = bd.getDespensaRaw();
        if (cursor != null){
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    this,
                    android.R.layout.simple_list_item_2,
                    cursor,
                    new String[]{"Nombre", "Descripcion"},
                    new int[]{android.R.id.text1, android.R.id.text2},
                    0
            );
            listDespensa.setAdapter(adapter);
        } else {
            CustomToastUtil.mostrarToastPersonalizado(this,"No hay datos disponibles en la despensa");
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
        Cursor cursor = db.query(bd.getTablaDespensa(), columns, selection, selectionArgs, null, null, null);
        //Verificamos si el cursor tiene al menos una fila
        boolean alimentoExists = cursor.moveToFirst();
        cursor.close();
        //Devuelve true si existe y false sino
        return alimentoExists;
    }
}