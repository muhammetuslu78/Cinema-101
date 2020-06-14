package com.muhammetuslu.cinema101.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.muhammetuslu.cinema101.R
import com.muhammetuslu.cinema101.dialog.ForgotPasswordFragment
import com.muhammetuslu.cinema101.view.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.etEmail

class LoginActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        var currentUser=auth.currentUser

        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        btnSignIn.setOnClickListener {
            if (etEmail.text.isNotEmpty() && etPasword.text.isNotEmpty())
            {
                auth.signInWithEmailAndPassword(etEmail.text.toString(),etPasword.text.toString()).addOnCompleteListener {task ->
                    if (task.isSuccessful)
                    {
                    Toast.makeText(this,"Welcome : "+auth.currentUser?.email.toString(),Toast.LENGTH_SHORT).show()

                    val intent = Intent(applicationContext,
                        MainActivity::class.java)
                    startActivity(intent)

                    finish()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(applicationContext,exception.localizedMessage.toString(),Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                Toast.makeText(this,"Please, fill in the blanks",Toast.LENGTH_SHORT).show()
            }
        }
        tvForgotPassword.setOnClickListener {
            var dialogForgotPassword= ForgotPasswordFragment()
            dialogForgotPassword.show(supportFragmentManager,"gosterdidialogsifre")
        }
    }
}
