package com.example.gestco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Registrarse extends AppCompatActivity implements View.OnClickListener{

    EditText etUser, etPassword;
    Button btRegistrarse, btVolverRegistro;
    DBHelper bd = null; // gestor de la BD

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

         //Inicializamos la BD
         bd = new DBHelper(this);
         //Recuperamos los componentes de la Interfaz
         etUser = findViewById(R.id.editTextUser2);
         etPassword = findViewById(R.id.editTextTextPassword2);
         btRegistrarse = findViewById(R.id.btRegistrarse);
         btVolverRegistro = findViewById(R.id.btVolverRegistro);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btRegistrarse) {
            //Obtenemos el nombre de usuario y la contraseña que se ha insertado
            String usuario = etUser.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String hasedPassword = PasswordHasher.hashPassword(password);

            //Verificamos que el nombre de usuario y la contraseña no estén vacíos
            if (!usuario.isEmpty() && !password.isEmpty()) {
                //Verificamos si la contraseña tiene al menos 5 caracteres
                if (password.length() >= 5) {
                    //Verificamos si el usuario ya existe
                    if (!existeUsuario(usuario)) {
                        //Insertamos el usuario en la base de datos
                        long resultado = bd.insertarUsuario(usuario, hasedPassword);

                        if (resultado != -1) {
                            //Si la inserción fue exitosa, accedemos a la pantalla principal
                            Intent intent = new Intent(this, MainActivity.class);
                            CustomToastUtil.mostrarToastPersonalizado(this, "Usuario registrado con éxito");
                            //Iniciamos la actividad que hay en el intent
                            startActivity(intent);
                        } else {
                            CustomToastUtil.mostrarToastPersonalizado(this, "Error al realizar el registro");
                        }

                    } else {
                        CustomToastUtil.mostrarToastPersonalizado(this, "El usuario ya existe");

                    }
                } else {
                    CustomToastUtil.mostrarToastPersonalizado(this, "La contraseña debe tener al menos 5 caracteres");
                }
            } else {
                CustomToastUtil.mostrarToastPersonalizado(this, "Por favor, complete todos los campos");

            }
        }
        else if (v.getId() == R.id.btVolverRegistro) {
            Intent intent = new Intent(this, MainActivity.class);
            //Iniciamos la actividad que hay en el intent
            startActivity(intent);
        }
    }
    //Metodo para verificar si un usuario ya existe
    private boolean existeUsuario(String usuario) {
        //Obtenemos la referencia a la base de datos en modo lectura
        SQLiteDatabase db = bd.getReadableDatabase();
        //Definimos las columnas que queremos recuperar
        String[] columns = {"Usuario"};
        //Definimos la clausula where
        String selection = "Usuario=?";
        String[] selectionArgs = {usuario};
        //Realizamos la consulta en la base de datos
        Cursor cursor = db.query(bd.getTablaUsuarios(), columns, selection, selectionArgs, null, null, null);
        //Verificamos si el cursor tiene al menos una fila
        boolean userExists = cursor.moveToFirst();
        cursor.close();
        return userExists;
    }
}