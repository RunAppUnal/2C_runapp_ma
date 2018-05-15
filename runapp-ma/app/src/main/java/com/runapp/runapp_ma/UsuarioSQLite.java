package com.runapp.runapp_ma;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.runapp.runapp_ma.ConexionSQLiteHelper;

public class UsuarioSQLite {

    //Constates campos tabla usuario
    public final static String TABLA_NOMBRE = "Usuario";
    public final static String CAMPO_ID = "id";
    public final static String CAMPO_UID =  "uid";
    public final static String CAMPO_TOKEN =  "token";
    public final static String CAMPO_NAME =  "name";
    public final static String CAMPO_LASTNAME =  "lastname";
    public final static String CAMPO_USERNAME =  "username";
    public final static String CAMPO_EMAIL =  "email";
    public final static String CAMPO_CLIENT = "client";



    public static final String CREAR_TABLA_USUARIO = "create table " +   TABLA_NOMBRE + " ("
            + CAMPO_ID + " integer, "
            + CAMPO_UID + " text, "
            + CAMPO_TOKEN + " text, "
            + CAMPO_NAME + " text, "
            + CAMPO_LASTNAME + " text, "
            + CAMPO_USERNAME + " text, "
            + CAMPO_EMAIL + " text, "
            + CAMPO_CLIENT + " text);";


    public static String[] consultaUsuario(ConexionSQLiteHelper con) {

        SQLiteDatabase db = con.getReadableDatabase();

        Cursor cursor = db.query(UsuarioSQLite.TABLA_NOMBRE, new String[] { "*" },null,null, null, null, null, null);
        String arrData[] = null;

        if(cursor != null)
        {
            if (cursor.moveToFirst()) {
                arrData = new String[cursor.getColumnCount()];

                arrData[0] = cursor.getString(0); // id
                arrData[1] = cursor.getString(1); // uid
                arrData[2] = cursor.getString(2); // token
                arrData[3] = cursor.getString(3); // name
                arrData[4] = cursor.getString(4); // lastname
                arrData[5] = cursor.getString(5); // username
                arrData[6] = cursor.getString(6); // email
                arrData[7] = cursor.getString(7); // client

                //System.out.println(cursor.getString(0) + cursor.getString(1) + cursor.getString(2));
            }
            cursor.close();
        }

        db.close();

        return  arrData;
    }

    public static void registrarUsuario(ConexionSQLiteHelper con, ContentValues values) {
        SQLiteDatabase db = con.getWritableDatabase();

        //ContentValues values = new ContentValues();

        Long idResult = db.insert(UsuarioSQLite.TABLA_NOMBRE, null, values);
        //Toast.makeText(getApplicationContext(), "Id registro: " + idResult, Toast.LENGTH_SHORT).show();
        System.out.println("-------->" + idResult);

        db.close();

    }

}
