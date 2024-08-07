package com.example.gestco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etUser, etPassword;
    Button btConectar, btCrear;
    DBHelper bd = null; // gestor de la BD


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicializamos la BD
        bd = new DBHelper(this);



        //Recuperamos los componentes de la Interfaz
        etUser = findViewById(R.id.editTextUser);
        etPassword = findViewById(R.id.editTextPassword);
        btConectar = findViewById(R.id.btConectar);
        btCrear = findViewById(R.id.btCrear);

        //Establecemos los listener de los botones
        btConectar.setOnClickListener(this);
        btCrear.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {

        //Si se pulsa el botón de crear una cuenta
        if (v.getId()==R.id.btCrear){
            //Se accede a la ventana Registrarse
            //Creamos un intent
            Intent intent = new Intent(this, Registrarse.class);
            //Iniciamos la actividad que hay en el intent
            startActivity(intent);
        }
        //Si se pulsa el botón de conectar
        else if (v.getId()==R.id.btConectar) {
            String usuario = etUser.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String hasedPassword = PasswordHasher.hashPassword(password);
            //Verificamos si el usuario y la contraseña coinciden en la base de datos
            if(validarCredenciales(usuario, hasedPassword)){
                //Si las credenciales son validas permitimos el acceso
                Intent intent = new Intent(this, Conectado.class);
                //Iniciamos la actividad que hay en el intent
                startActivity(intent);
            } else {
                CustomToastUtil.mostrarToastPersonalizado(this,"Usuario o contraseña incorrecta");
            }

        }
    }

    private boolean validarCredenciales(String usuario, String hasedPassword) {
        SQLiteDatabase db = bd.getReadableDatabase();
        String [] columns = {"Usuario", "Password"};
        String selection = "Usuario=? AND Password=?";
        String [] selectionArgs = {usuario, hasedPassword};
        Cursor cursor = db.query(bd.getTablaUsuarios(),columns, selection, selectionArgs, null, null, null);
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;

    }
}