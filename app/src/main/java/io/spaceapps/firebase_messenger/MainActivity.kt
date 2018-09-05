package io.spaceapps.firebase_messenger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        handleRegister()
        handleShowLoginScreen()
    }


    private fun handleRegister() {
        login_button_login.setOnClickListener {

            val email = email_edittext_login.text.toString()
            val password = password_edittext_login.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { result ->
                        if (!result.isSuccessful) return@addOnCompleteListener

                        Log.d(TAG, "Successfully created username with email and password, " +
                                "uid ${result.result.user.uid}")
                    }

                    .addOnFailureListener {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
        }
    }

    private fun handleShowLoginScreen() {
        already_have_account_textview.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
