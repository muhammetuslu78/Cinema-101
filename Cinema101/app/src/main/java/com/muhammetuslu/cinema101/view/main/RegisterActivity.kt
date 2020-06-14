package com.muhammetuslu.cinema101.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.muhammetuslu.cinema101.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth :FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth= FirebaseAuth.getInstance()

        btnSignUp.setOnClickListener {

            if (etEmail.text.isNotEmpty() && etPassword.text.isNotEmpty() && etPasswordAgain.text.isNotEmpty())
            {
                if (etPassword.text.toString().equals(etPasswordAgain.text.toString()))
                {
                    newMemberRegister(etEmail.text.toString(),etPassword.text.toString())
                }
            }
            else
            {
                Toast.makeText(this,"Please, fill in the blanks",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun newMemberRegister(eMail: String, password: String) {

        auth.createUserWithEmailAndPassword(eMail,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                {
                    Toast.makeText(this,"Member is registered",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
                else
                {
                    Toast.makeText(this,"There was an error : "+task.exception,Toast.LENGTH_SHORT).show()
                }
            }

    }
}
