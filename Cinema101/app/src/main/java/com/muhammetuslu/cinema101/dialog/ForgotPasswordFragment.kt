package com.muhammetuslu.cinema101.dialog


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.muhammetuslu.cinema101.R

class ForgotPasswordFragment : DialogFragment() {
    lateinit var emailEditText: EditText
    private var mContext: Context? = null
    private lateinit var auth:FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth= FirebaseAuth.getInstance()
        var view = inflater.inflate(R.layout.fragment_forgot_password, container, false)

        mContext=activity
        emailEditText=view.findViewById(R.id.etForgotEmail)

        var btnSend=view.findViewById<Button>(R.id.btnSend)
        btnSend.setOnClickListener {
            auth.sendPasswordResetEmail(emailEditText.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                    {
                        Toast.makeText(mContext,"Please, check your email",Toast.LENGTH_SHORT).show()
                        dialog?.dismiss()
                    }
                    else
                    {
                        Toast.makeText(mContext,"Error : "+task.exception?.message,Toast.LENGTH_SHORT).show()
                    }
                }
        }

        return view
    }


}
