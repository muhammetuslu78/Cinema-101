package com.muhammetuslu.cinema101.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.animation.AnimationUtils
import com.google.firebase.auth.FirebaseAuth
import com.muhammetuslu.cinema101.view.main.LoginActivity
import com.muhammetuslu.cinema101.R
import com.muhammetuslu.cinema101.view.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        var nacak = AnimationUtils.loadAnimation(this,
            R.anim.yukaridan_gelen_nacak
        )
        var yazi = AnimationUtils.loadAnimation(this,
            R.anim.yandan_gelen_yazi
        )
        yaziView.animation=yazi
        nacakView.animation=nacak

        auth = FirebaseAuth.getInstance()

        var currentUser=auth.currentUser

        object : CountDownTimer(3000,1000)
        {
            override fun onFinish() {
                if (currentUser == null)
                {
                    val intent = Intent(this@SplashActivity,
                        LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else
                {
                    val intent = Intent(this@SplashActivity,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            override fun onTick(millisUntilFinished: Long) {
            }
        }.start()
    }
}
