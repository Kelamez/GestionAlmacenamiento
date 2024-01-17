package com.example.gestinalmacenamiento

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    var tbAlmacen:TableLayout?=null
    lateinit var txtNombre : EditText;
    lateinit var Aniadir : ImageButton;
    lateinit var Buscar : ImageButton;
    lateinit var Borrar : ImageButton;
    lateinit var Entrar : ImageButton;

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tbAlmacen= findViewById(R.id.tbItem)
        txtNombre = findViewById(R.id.Name)
        Aniadir = findViewById(R.id.Aniadir)
        Buscar = findViewById(R.id.Buscar)
        Borrar = findViewById(R.id.Borrar)
        Entrar = findViewById(R.id.Entrar)
        Refrescar(null)

        Aniadir.setOnClickListener(){
            Insertar()
        }

        Buscar.setOnClickListener(){
            var nombre = txtNombre?.text.toString()
            txtNombre?.setText("")
            if (nombre.isEmpty() == true){
                Refrescar(null)
            }
            else{
                Refrescar(nombre)
            }
        }

        Borrar.setOnClickListener(){
            var nombre = txtNombre?.text.toString()
            txtNombre?.setText("")
            Borrar(nombre)
        }

        Entrar.setOnClickListener(){
            var nombre = txtNombre?.text.toString()
            txtNombre?.setText("")
            Entrar(nombre)
        }

    }

    fun Insertar(){

        var con= DbHelper(this)
        var baseDatos = con.writableDatabase
        var nombre = txtNombre?.text.toString()

        if(nombre.isEmpty()==false){
            var registro = ContentValues()

            var numerotabla = 1
            do{
                val prueba = baseDatos.rawQuery("select tabla from t_almacen where tabla="+numerotabla, null)
                if (!prueba.isAfterLast){
                    numerotabla++
                }
            }while(prueba.isAfterLast == false)
            registro.put("nombre", nombre)
            registro.put("tabla", numerotabla)
            baseDatos.insert("t_almacen", null, registro)
            txtNombre?.setText("")
            Toast.makeText(this, "Se ha añadido exitosamente.", Toast.LENGTH_LONG).show()
        }
        else{
            Toast.makeText(this, "Ponga primero el nombre por favor.", Toast.LENGTH_LONG).show()
        }
        Refrescar(null)

    }

    fun Borrar(nombre : String){

        val con=DbHelper(this)
        val baseDatos = con.writableDatabase
        val cant = baseDatos.rawQuery("select tabla from t_almacen where nombre='"+nombre+"'", null)
        if(cant.isAfterLast != null) {
            val builder = AlertDialog.Builder(this);

            builder.setTitle("¿Seguro que desea borrar?")
            builder.setMessage("Tanto el almacén seleccionado cómo los items del mismo serán eliminados permanentemente.")
            builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener{dialog, which ->
                val can=baseDatos.rawQuery("select * from t_item where id='"+cant.getInt(0)+"'", null)
                if(can.isAfterLast == null) {
                    baseDatos.delete("t_item", "id='"+cant.getInt(0)+"'", null)
                }
                baseDatos.delete("t_almacen", "nombre='"+nombre+"'", null)
                Toast.makeText(this, "El producto fue eliminado.", Toast.LENGTH_LONG).show()
                Refrescar(null)
            })
            builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(this, "Se canceló el borrado", Toast.LENGTH_LONG).show()
            })

            builder.show()

            Refrescar(null)
        }
        else{
            Toast.makeText(this,"El producto no se encontró.", Toast.LENGTH_LONG).show()
        }

    }

    fun Entrar(nombre : String){

        val con=DbHelper(this)
        val baseDatos = con.writableDatabase
        var fila = baseDatos.rawQuery("select tabla from t_almacen where nombre='"+nombre+"'", null)

        if(fila.isAfterLast == false) {
            var tabla = fila.getInt(0)

            val i = Intent(this, MainActivity2::class.java)
            i.putExtra("numerotabla", tabla)
            startActivity(i)
        }
        else{
            Toast.makeText(this, "No se pudo entrar", Toast.LENGTH_LONG).show()
        }

    }

    fun Refrescar(nombre: String?){

        tbAlmacen?.removeAllViews()

        val con=DbHelper(this)
        val baseDatos = con.writableDatabase
        var fila = baseDatos.rawQuery("select * from t_almacen", null)
        if(nombre != null){
            fila = baseDatos.rawQuery("select * from t_almacen where nombre ='"+nombre+"'", null)
        }
        if(fila.isAfterLast == false) {
            fila.moveToFirst()
            do{
                val registro = LayoutInflater.from(this).inflate(R.layout.activity_sub, null, false)
                val colNombre = registro.findViewById<View>(R.id.NombreAlmacen) as TextView
                val colID = registro.findViewById<View>(R.id.ID) as TextView
                colNombre.setText(fila.getString(1))
                colID.setText(fila.getString(0))
                tbAlmacen?.addView(registro)
            }while (fila.moveToNext())
        }
    }

}