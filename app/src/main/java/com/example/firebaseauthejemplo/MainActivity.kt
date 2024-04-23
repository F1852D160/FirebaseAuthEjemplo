package com.example.firebaseauthejemplo



import android.os.Bundle
import android.view.MenuItem
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    lateinit var listAdapter: ArrayAdapter<Pais>
    lateinit var dbRef : DatabaseReference

    //autenticacion
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dbRef = Firebase.database.reference


        mAuth = FirebaseAuth.getInstance()

        setContentView(R.layout.activity_main)

        var listView = findViewById<ListView>(R.id.listviewPaises)
        var btnAdd = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        var paisesList = mutableListOf<Pais>()
        listAdapter = ArrayAdapter<Pais>(
            applicationContext,
            android.R.layout.simple_list_item_1,
            paisesList
        )

        listView.adapter = listAdapter

        btnAdd.setOnClickListener { agregarPais(Pais()) }


        listView.onItemLongClickListener =
            OnItemLongClickListener { parent, view, position, id ->
                val pop = PopupMenu(this@MainActivity, view)
                pop.getMenuInflater().inflate(R.menu.menu_modificar_eliminar, pop.getMenu())
                pop.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(item: MenuItem): Boolean {
                        if (item.itemId == R.id.itemEliminar) {
                            val m: Pais? = listAdapter.getItem(position)
                           // eliminarMensaje(m)
                            if(m != null)
                                eliminar(m)

                            Toast.makeText(this@MainActivity,"Eliminar",Toast.LENGTH_SHORT).show()
                        }

//                        else if (item.itemId == R.id.itemModificar) {
//                            val m: Pais? = listAdapter.getItem(position)
//                            Toast.makeText(this@MainActivity,"Modificar",Toast.LENGTH_SHORT).show()
//                           /// modificarMensaje(m)
//                        }
                        return true
                    }
                })
                pop.show()
                true
            }



    }

    fun agregarPais(m:Pais){
        val input = EditText(this)
        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle("Agregar Pais")
        builder.setView(input)
        builder.setPositiveButton("SI") { dialog, which ->
            val userInput = input.text.toString().trim()

            val id: String? = dbRef.push().key
            val nuevoPais = Pais(id!!,userInput,"A")
            dbRef.child("paises").child(id).setValue(nuevoPais)

//            dbRef.child("paises").
        }

        builder.setNegativeButton("Cancelar") { dialog, which ->
            dialog.dismiss()
        }

        builder.show()

//        input.setText(m.nombre)
//
//        MaterialAlertDialogBuilder(this, Ma)
//            .setTitle("Modificar")
//            .setView(input)
//            .setPositiveButton("Guardar") { dialog, which ->
//                val mensaje = input.text.toString()
//                m.setMensaje(mensaje)
//                dbRef.child(m.getId()).setValue(m)
//            }
//            .setNegativeButton("Cancelar", null)
//            .show()

    }

    fun eliminar(idEliminar:Pais){
        //val idEliminar = "-Nvne8blF61GMuuUdlDn"
        dbRef.child("paises").child(idEliminar.id).removeValue()
       // listAdapter.remove(idEliminar)
    }


    fun modificar(){
        val nuevoPais = Pais("-Nvne8blF61GMuuUdlDn","URUGUAY MODIFICADO","A")
        dbRef.child("paises").child("-Nvne8blF61GMuuUdlDn").setValue(nuevoPais)
    }

    fun listar(){
        dbRef.child("paises").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listAdapter.clear()
                for (snap in dataSnapshot.children) {
                    val m: Pais? = snap.getValue(Pais::class.java)
                    listAdapter.add(m)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onStart() {

        //iniciamos conexion a la base de datos
        listar()

        super.onStart()
    }
}