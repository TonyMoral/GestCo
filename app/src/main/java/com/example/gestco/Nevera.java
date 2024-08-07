package com.example.gestco;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Nevera extends AppCompatActivity implements View.OnClickListener {

    ListView listNevera;
    Button btAñadirNevera, btVolverNevera, btComprobarCaducidad;
    EditText etNevera, etCaducidad;
    DBHelper bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nevera);
        //Recuperamos los componentes de la Interfaz
        listNevera = findViewById(R.id.listNevera);
        btAñadirNevera = findViewById(R.id.btAñadirNevera);
        btVolverNevera = findViewById(R.id.btVolverNevera);
        btComprobarCaducidad = findViewById(R.id.btComprobarCaducidad);
        etNevera = findViewById(R.id.editTextNevera);
        etCaducidad = findViewById(R.id.editTextDateNevera);
        bd = new DBHelper(this);


        btAñadirNevera.setOnClickListener(this);
        btVolverNevera.setOnClickListener(this);
        btComprobarCaducidad.setOnClickListener(this);

        //Mostramos la lista de compras al iniciar la actividad
        mostrarListaNevera();
        //Creamos un evento para borrar pulsando unos segundos sobre el elemento
        listNevera.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                @SuppressLint("Range") String nombreAlimento = cursor.getString(cursor.getColumnIndex("Nombre"));

                //Llamamos al método para eliminar el alimento de la lista de compras
                int filasEliminadas = bd.eliminarAlimentoNevera(nombreAlimento);

                if (filasEliminadas > 0) {
                    CustomToastUtil.mostrarToastPersonalizado(getApplicationContext(), "Alimento eliminado de la lista de la Nevera");
                    //Actualizamos la lista de compras después de eliminar
                    mostrarListaNevera();
                } else {
                    CustomToastUtil.mostrarToastPersonalizado(getApplicationContext(), "No se pudo eliminar el alimento");
                }
                //Indicamos que el evento ha sido manejado correctamente
                return true;
            }
        });
        //Asignamos un OnClickListener al EditText de la fecha
        etCaducidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatePickerDialog();
            }
        });
    }

    //Método para mostrar el cuadro de fechas
    private void mostrarDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int año = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        //Creamos una nueva instancia de DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(Nevera.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Se actualiza el texto del EditText con la fecha seleccionada
                String fechaSeleccionada = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                etCaducidad.setText(fechaSeleccionada);
            }
        }, año, mes, dia);
        datePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btAñadirNevera) {
            //Obtenemos el nombre del alimento introducido por el usuario
            String nombreNevera = etNevera.getText().toString().trim();
            String fecha = etCaducidad.getText().toString().trim();
            //Comprobamos que el campo nombre no esté vacíos
            if (!nombreNevera.isEmpty()) {
                //Comprobamos si el alimento existe
                if (existeAlimento(nombreNevera)){
                    CustomToastUtil.mostrarToastPersonalizado(this, "El alimento ya existe");
                    return;
                }
                //Si no existe insertamos el alimento en la base de datos
                long resultado;
                if (!fecha.isEmpty()) {
                    resultado = bd.insertarAlimentoNevera(nombreNevera, fecha);
                } else {
                    resultado = bd.insertarAlimentoNevera(nombreNevera, null);
                }
                //Comprobamos si la inserción se realizó
                if (resultado != -1) {
                    CustomToastUtil.mostrarToastPersonalizado(this,"Elemento añadido");
                    //Limpiamos los EditText después de agregar la compra
                    etNevera.setText("");
                    etCaducidad.setText("");
                    //Mostramos la lista actualizada de compras
                    mostrarListaNevera();
                } else {
                    CustomToastUtil.mostrarToastPersonalizado(this, "Error al añadir elemento");
                }
            } else {
                CustomToastUtil.mostrarToastPersonalizado(this, "Por favor ingrese un alimento");
            }
        } else if (v.getId() == R.id.btVolverNevera) {
            Intent intent = new Intent(this, Conectado.class);
            //Iniciamos la actividad que hay en el intent
            startActivity(intent);
        } else if (v.getId() == R.id.btComprobarCaducidad) {
            comprobarCaducidadAlimentos();
        }
    }
    //Metodo para mostrar la lista de la nevera
    private void mostrarListaNevera() {
        //Obtenemos el cursor con los datos de la base de datos
        Cursor cursor = bd.getNeveraRaw();
        //Verificamos si el cursor es nulo
        if (cursor != null) {
            //Creamos un adaptador para vincular los datos del cursor con las vistas en el listview
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    this,
                    //Diseño para elementos de lista con dos lineas de texto
                    android.R.layout.simple_list_item_2,
                    cursor,
                    //Nombres de las columnas
                    new String[]{"Nombre", "Date"},
                    new int[]{android.R.id.text1, android.R.id.text2},
                    0
            );
            //Asignamos el adaptador
            listNevera.setAdapter(adapter);
        } else {
            CustomToastUtil.mostrarToastPersonalizado(this, "No hay datos disponibles en la nevera");
        }
    }

    //Método para comprobar la caducidad de los alimentos en la nevera
    private void comprobarCaducidadAlimentos(){
        //Obtenemos los datos de la nevera desde la base de datos
        Cursor cursor = bd.getNeveraRaw();
        //Verificamos que el cursor no esté vacío
        if(cursor != null && cursor.moveToFirst()){
            StringBuilder mensajeBuilder = new StringBuilder();
            boolean hayAlimentosCaducados = false;
            do {
                @SuppressLint("Range") String nombreAlimento = cursor.getString(cursor.getColumnIndex("Nombre"));
                @SuppressLint("Range") String fechaCaducidad = cursor.getString(cursor.getColumnIndex("Date"));
                //Verificamos si la fecha de caducidad no está vacía
                if (fechaCaducidad != null && !fechaCaducidad.isEmpty()){
                    //Calculamos los días restantes para la caducidad
                    int diasRestantes = calcularDiasRestantes(fechaCaducidad);
                    //Agregamos el nombre del alimento al mensaje si está próximo a caducar
                    if (diasRestantes <= 3) {
                        hayAlimentosCaducados = true;
                        mensajeBuilder.append("El alimento <b>").append(nombreAlimento).append("</b>");
                        //Verificamos si el alimento está caducado o caduca en un número negativo de días
                        if (diasRestantes <= 0) {
                            mensajeBuilder.append(" ha <b>caducado</b>.");
                        } else {
                            mensajeBuilder.append(" caduca en <b>").append(diasRestantes).append(" días.</b>");
                        }
                        mensajeBuilder.append("<br/>");
                    }
                }
            } while (cursor.moveToNext());
            cursor.close();
            //Mostramos el diálogo solo si hay alimentos próximos a caducar
            if (hayAlimentosCaducados) {
                mostrarDialogAlert(mensajeBuilder.toString());
            } else {
                //Mostramos un Toast indicando que no hay alimentos caducados o próximos a caducar
                CustomToastUtil.mostrarToastPersonalizado(this, "No hay alimentos caducados o próximos a caducar");

            }
        }
    }


    //Método para calcular la diferencia de días entre la fecha de caducidad y la fecha actual
    private int calcularDiasRestantes(String fechaCaducidad) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            //Convertimos la fecha de caducidad a un objeto Date
            Date fechaCaducidadDate = sdf.parse(fechaCaducidad);

            //Obtenemos la fecha actual sin las horas, minutos y segundos
            Calendar calendarActual = Calendar.getInstance();
            calendarActual.set(Calendar.HOUR_OF_DAY, 0);
            calendarActual.set(Calendar.MINUTE, 0);
            calendarActual.set(Calendar.SECOND, 0);
            calendarActual.set(Calendar.MILLISECOND, 0);
            Date fechaActual = calendarActual.getTime();

            //Calculamos la diferencia en milisegundos entre las dos fechas
            long diferenciaMilisegundos = fechaCaducidadDate.getTime() - fechaActual.getTime();

            //Convertimos la diferencia a días
            long diferenciaDias = TimeUnit.DAYS.convert(diferenciaMilisegundos, TimeUnit.MILLISECONDS);

            return (int) diferenciaDias;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    //Método para mostrar el AlertDialog
    private void mostrarDialogAlert(String mensaje) {
        //Creamos un constructor de AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alerta de Caducidad");
        //Establecemos el mensaje del AlertDialog con formato HTML y las palabras en negrita
        builder.setMessage(Html.fromHtml(mensaje));
        //Agregamos un botón "Aceptar"
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Cerramos el AlertDialog cuando se hace clic en "Aceptar"
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
        Cursor cursor = db.query(bd.getTablaNevera(), columns, selection, selectionArgs, null, null, null);
        //Verificamos si el cursor tiene al menos una fila
        boolean alimentoExists = cursor.moveToFirst();
        cursor.close();
        //Devuelve true si existe y false sino
        return alimentoExists;
    }
}