package com.example.firebaseauthejemplo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import java.util.Arrays


class LoginActivity : AppCompatActivity() {

    lateinit  var mAuth: FirebaseAuth
    var RC_SIGN_IN = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val btnAcceder = findViewById<Button>(R.id.btnAcceder);
        val btnAcceder2 = findViewById<Button>(R.id.btnAcceder2)

        val editTextUsuario = findViewById<TextInputEditText>(R.id.edtUsuario);
        val editTextClave = findViewById<TextInputEditText>(R.id.edtClave);
        mAuth = FirebaseAuth.getInstance();

        btnAcceder.setOnClickListener{
            val email = editTextUsuario.text.toString()
            val pass = editTextClave.text.toString()
            mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) { //login Exitoso

                        val intent: Intent = Intent(
                            applicationContext,
                            MainActivity::class.java
                        )
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Usuario/clave incorrecto",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }



        btnAcceder2.setOnClickListener{

            val providers: List<AuthUI.IdpConfig> = Arrays.asList(
                AuthUI.IdpConfig.EmailBuilder().build(),  // new AuthUI.IdpConfig.PhoneBuilder().build(),
//                AuthUI.IdpConfig.GoogleBuilder().build(),
//                AuthUI.IdpConfig.MicrosoftBuilder().build(),
//                AuthUI.IdpConfig.FacebookBuilder().build() // new AuthUI.IdpConfig.TwitterBuilder().build()
            )
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),
                RC_SIGN_IN
            )

        }


    }


    //metodo ejecutado por firebaseui-auth
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === RC_SIGN_IN) {
            if (resultCode === RESULT_OK) {
                //autenticacion exitosa abrimos el intent que muestra el usuario conectado
                val intent: Intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            } else {
                // data.getStringExtra();
            }
        }

    }


}