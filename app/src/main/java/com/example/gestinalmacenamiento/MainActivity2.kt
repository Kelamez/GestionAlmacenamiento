package com.example.gestinalmacenamiento

import android.content.ContentValues
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog

class MainActivity2 : AppCompatActivity() {

    var tbItem: TableLayout?=null
    lateinit var txtNombre : EditText;
    lateinit var txtNumero : EditText;
    lateinit var Buscar : Button;
    lateinit var Aniadir : Button;
    lateinit var Cambiar : Button;
    lateinit var Borrar : Button;



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val numerotabla = intent.getIntExtra("numerotabla", 0)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        tbItem=findViewById(R.id.tbItem)
        txtNombre = findViewById(R.id.Name)
        Buscar = findViewById(R.id.Buscar)
        Aniadir = findViewById(R.id.Aniadir)
        Cambiar = findViewById(R.id.Cambiar)
        Borrar = findViewById(R.id.Borrar)
        Refrescar(null, numerotabla)
        Aniadir.setOnClickListener(){
            Insertar(numerotabla)
        }

        Buscar.setOnClickListener(){
            var nombre = txtNombre?.text.toString()
            txtNombre?.setText("")
            if (nombre.isEmpty() == true){
                Refrescar(null, numerotabla)
            }
            else{
                Refrescar(nombre, numerotabla)
            }
        }

        Borrar.setOnClickListener(){
            var nombre = txtNombre?.text.toString()
            txtNombre?.setText("")
            Borrar(nombre, numerotabla)
        }

        Cambiar.setOnClickListener(){
            var nombre = txtNombre?.text.toString()
            txtNombre?.setText("")
            Cambiar(nombre, numerotabla)
        }
    }

    fun Insertar(numerotabla : Int){
        var con= DbHelper(this)
        var baseDatos = con.writableDatabase
        var nombre = txtNombre?.text.toString()
        var numero = txtNumero?.text.toString()

        if(nombre.isEmpty()==false && numero.isEmpty()==false){
            var registro = ContentValues()

            registro.put("nombre", nombre)
            registro.put("tabla", numerotabla)
            registro.put("cantidad", numero)

            baseDatos.insert("t_item", null, registro)

            txtNombre?.setText("")
            txtNumero?.setText("")

            Toast.makeText(this, "Se ha añadido exitosamente.", Toast.LENGTH_LONG).show()
        }
        else{
            Toast.makeText(this, "Ponga primero el nombre por favor.", Toast.LENGTH_LONG).show()
        }
        Refrescar(null, numerotabla)
    }

    fun Cambiar(nombre: String?, numerotabla : Int){
        var con= DbHelper(this)
        var baseDatos = con.writableDatabase
        var nombre = txtNombre?.text.toString()
        var numero = txtNumero?.text.toString()

        if(nombre.isEmpty()==false && numero.isEmpty()==false){
            var registro = ContentValues()

            registro.put("nombre", nombre)
            registro.put("tabla", numerotabla)
            registro.put("cantidad", numero)

            baseDatos.update("t_item", registro, "nombre='" + nombre +"'", null)

            txtNombre?.setText("")
            txtNumero?.setText("")

            Toast.makeText(this, "Se ha añadido exitosamente.", Toast.LENGTH_LONG).show()
        }
        else{
            Toast.makeText(this, "Ponga primero el nombre por favor.", Toast.LENGTH_LONG).show()
        }
        Refrescar(null, numerotabla)
    }

    fun Borrar(nombre : String?, numerotabla : Int){

        val con=DbHelper(this)
        val baseDatos = con.writableDatabase
        val cant = baseDatos.rawQuery("select tabla from t_almacen where nombre='"+nombre+"'", null)
        if(cant.isAfterLast != null) {
            val builder = AlertDialog.Builder(this);

            builder.setTitle("¿Seguro que desea borrar?")
            builder.setMessage("El item seleccionado será borrado permanentemente")
            builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener{ dialog, which ->
                baseDatos.delete("t_item", "nombre='"+nombre+"'", null)
                Toast.makeText(this, "El producto fue eliminado.", Toast.LENGTH_LONG).show()
                Refrescar(null, numerotabla)
            })
            builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(this, "Se canceló el borrado", Toast.LENGTH_LONG).show()
            })

            builder.show()

            Refrescar(null, numerotabla)
        }
        else{
            Toast.makeText(this,"El producto no se encontró.", Toast.LENGTH_LONG).show()
        }

    }

    fun Refrescar(nombre: String?, tabla: Int){
        tbItem?.removeAllViews()

        val con=DbHelper(this)
        val baseDatos = con.writableDatabase
        var fila = baseDatos.rawQuery("select id, nombre, cantidad from t_item where id = '" + tabla + "'", null)

        if(nombre != null){
            fila = baseDatos.rawQuery("select id, nombre, cantidad from t_item where nombre ='"+nombre+"' and tabla = '" + tabla + "'", null)
        }

        if(fila.isAfterLast == false) {
            fila.moveToFirst()
            do{
                val registro = LayoutInflater.from(this).inflate(R.layout.activity_sub2, null, false)
                val colNombre = registro.findViewById<View>(R.id.Nombre) as TextView
                val colID = registro.findViewById<View>(R.id.Id) as TextView
                val colNumero = registro.findViewById<View>(R.id.Cantidad) as TextView
                colNombre.setText(fila.getString(1))
                colID.setText(fila.getString(0))
                colNumero.setText(fila.getString(2))
                tbItem?.addView(registro)
            }while (fila.moveToNext())
        }
    }
}
