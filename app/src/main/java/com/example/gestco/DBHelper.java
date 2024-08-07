package com.example.gestco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    static String DATABASE_NAME = "BDGestCo"; //Nombre de la base de datos
    static int DATABASE_VERSION = 1; //Version de la BD
    String tablaUsuarios = "Usuarios";
    String tablaCompra = "Compra";
    String tablaNevera = "Nevera";
    String tablaCongelador = "Congelador";
    String tablaDespensa = "Despensa";
    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + tablaUsuarios + "(Usuario VARCHAR (20)PRIMARY KEY, " + "Password TEXT);");
        db.execSQL("CREATE TABLE " + tablaCompra + "(Id INTEGER PRIMARY KEY AUTOINCREMENT, Nombre VARCHAR (20));");
        db.execSQL("CREATE TABLE " + tablaNevera + " (Id INTEGER PRIMARY KEY AUTOINCREMENT, Date TEXT, Nombre VARCHAR(20));");
        db.execSQL("CREATE TABLE " + tablaCongelador + "(Id INTEGER PRIMARY KEY AUTOINCREMENT, Nombre VARCHAR (20), Localizacion VARCHAR (50));");
        db.execSQL("CREATE TABLE " + tablaDespensa + "(Id INTEGER PRIMARY KEY AUTOINCREMENT, Nombre VARCHAR (20), Descripcion VARCHAR (100));");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + tablaUsuarios + tablaCompra + tablaNevera + tablaCongelador + tablaDespensa);
        db.execSQL("CREATE TABLE " + tablaUsuarios + "(Usuario VARCHAR (20)PRIMARY KEY, " + "Password TEXT);");
        db.execSQL("CREATE TABLE " + tablaCompra + "(Id INTEGER PRIMARY KEY AUTOINCREMENT, Nombre VARCHAR (20));");
        db.execSQL("CREATE TABLE " + tablaNevera + " (Id INTEGER PRIMARY KEY AUTOINCREMENT, Date DATE, Nombre VARCHAR(20));");
        db.execSQL("CREATE TABLE " + tablaCongelador + "(Id INTEGER PRIMARY KEY AUTOINCREMENT, Nombre VARCHAR (20), Localizacion VARCHAR (50));");
        db.execSQL("CREATE TABLE " + tablaDespensa + "(Id INTEGER PRIMARY KEY AUTOINCREMENT, Nombre VARCHAR (20), Descripcion VARCHAR (100));");

    }
    //Método para consultar la tabla con un SELECT
    public Cursor getUsuariosRaw(){
        //Pedir BD con permisos de lectura
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM "+tablaUsuarios,null);
    }
    //Método para insertar un usuario y una contraseña
    public long insertarUsuario(String Usuario, String Password){
        //Pedimos la bd con permisos de Escritura
        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues(); /*ContenValues guarda
    duplas (clave , valor) -> clave es la columna de la tabla y valor el
    valor de la columna*/
        value.put("Usuario",Usuario);
        value.put("Password",Password);
        //Inserción parametrizada, requiere los valores con contentValues
        return db.insert(tablaUsuarios,null,value);
    }
    public String getTablaUsuarios() {
        return tablaUsuarios;
    }

    //Método para consultar la tabla compra con un SELECT
    public Cursor getCompraRaw(){
        //Pedir BD con permisos de lectura
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT ROWID as _id, Nombre FROM " + tablaCompra, null);
    }
    //Método para insertar alimentos en la lista de la compra
    public long insertarCompra(String Nombre){
        //Pedimos la bd con permisos de Escritura
        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues(); /*ContenValues guarda
    duplas (clave , valor) -> clave es la columna de la tabla y valor el
    valor de la columna*/
        value.put("Nombre",Nombre);
        //Inserción parametrizada, requiere los valores con contentValues
        return db.insert(tablaCompra,null,value);
    }
    //Método para eliminar un alimento de la tabla de compras
    public int eliminarAlimentoCompra(String nombreAlimento) {
        //Obtenemos la base de datos con permisos de escritura
        SQLiteDatabase db = getWritableDatabase();
        //Definimos la cláusula WHERE para la eliminación
        String whereClause = "Nombre = ?";
        //Definimos los argumentos de la cláusula WHERE
        String[] whereArgs = {nombreAlimento};
        //Realizamos la eliminación y obtenemos el número de filas afectadas
        return db.delete(tablaCompra, whereClause, whereArgs);
    }
    public String getTablaCompra() {
        return tablaCompra;
    }

    //Método para consultar la tabla nevera con un SELECT
    public Cursor getNeveraRaw(){
        //Pedimos los permisos de lectura
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT ROWID as _id, Nombre, Date FROM " + tablaNevera, null);
    }
    //Método para insertar alimentos en la nevera con nombre y fecha de caducidad
    public long insertarAlimentoNevera(String nombreAlimento, String date) {
        //Pedimos la BD con permisos de escritura
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Nombre", nombreAlimento);
        if (date != null){
            values.put("Date", date);
        }
        //Inserción parametrizada, requiere los valores con ContentValues
        return db.insert(tablaNevera, null, values);
    }
    //Método para eliminar alimentos de la tabla nevera
    public int eliminarAlimentoNevera(String nombreAlimento) {
        //Obtenemos la base de datos con permisos de escritura
        SQLiteDatabase db = getWritableDatabase();
        //Definimos la cláusula WHERE para la eliminación
        String whereClause = "Nombre = ?";
        //Definimos los argumentos de la cláusula WHERE
        String[] whereArgs = {nombreAlimento};
        //Realizamos la eliminación y obtenemos el número de filas afectadas
        return db.delete(tablaNevera, whereClause, whereArgs);
    }
    public String getTablaNevera() {
        return tablaNevera;
    }

    //Método para consultar la tabla Congelador con un SELECT
    public Cursor getCongeladorRaw(){
        //Pedir BD con permisos de lectura
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT ROWID as _id, Nombre, Localizacion FROM " + tablaCongelador, null);
    }
    //Método para insertar alimentos en el congelador
    public long insertarCongelador(String Nombre, String Localizacion){
        //Pedimos la bd con permisos de Escritura
        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues(); /*ContenValues guarda
    duplas (clave , valor) -> clave es la columna de la tabla y valor el
    valor de la columna*/
        value.put("Nombre",Nombre);
        value.put("Localizacion", Localizacion);
        //Inserción parametrizada, requiere los valores con contentValues
        return db.insert(tablaCongelador,null,value);
    }
    //Método para eliminar un alimento de la tabla de congelador
    public int eliminarAlimentoCongelador(String nombreAlimento) {
        //Obtenemos la base de datos con permisos de escritura
        SQLiteDatabase db = getWritableDatabase();
        //Definimos la cláusula WHERE para la eliminación
        String whereClause = "Nombre = ?";
        //Definimos los argumentos de la cláusula WHERE
        String[] whereArgs = {nombreAlimento};
        //Realizamos la eliminación y obtenemos el número de filas afectadas
        return db.delete(tablaCongelador, whereClause, whereArgs);
    }
    public String getTablaCongelador() {
        return tablaCongelador;
    }

    //Método para consultar la tabla despensa con un SELECT
    public Cursor getDespensaRaw(){
        //Pedir BD con permisos de lectura
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT ROWID as _id, Nombre, Descripcion FROM " + tablaDespensa, null);


    }
    //Método para insertar alimentos en la lista de la despensa
    public long insertarDespensa(String Nombre, String Descripcion){
        //Pedimos la bd con permisos de Escritura
        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues(); /*ContenValues guarda
    duplas (clave , valor) -> clave es la columna de la tabla y valor el
    valor de la columna*/
        value.put("Nombre",Nombre);
        value.put("Descripcion", Descripcion);
        //Inserción parametrizada, requiere los valores con contentValues
        return db.insert(tablaDespensa,null,value);
    }
    //Método para eliminar un alimento de la tabla de la despensa
    public int eliminarAlimentoDespensa(String nombreAlimento) {
        //Obtenemos la base de datos con permisos de escritura
        SQLiteDatabase db = getWritableDatabase();
        //Definimos la cláusula WHERE para la eliminación
        String whereClause = "Nombre = ?";
        //Definimos los argumentos de la cláusula WHERE
        String[] whereArgs = {nombreAlimento};
        //Realizamos la eliminación y obtenemos el número de filas afectadas
        return db.delete(tablaDespensa, whereClause, whereArgs);
    }
    public String getTablaDespensa() {
        return tablaDespensa;
    }
}