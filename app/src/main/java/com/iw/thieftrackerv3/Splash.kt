package com.iw.thieftrackerv3

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.os.HandlerCompat.postDelayed
import android.view.WindowManager
import com.iw.thieftrackerv3.Config.Session


class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar!!.hide()

        val handler = Handler()
        handler.postDelayed(Runnable {
            val session: Session = Session(applicationContext)
            if(session.getSession().id != 0){
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent(this, LocationRequest::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_out, R.anim.fade_in)
                    finish()
                }else{
                    val intent = Intent(this, Home::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_out, R.anim.fade_in)
                    finish()
                }
            }else{
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in)
                finish()
            }

        }, 3000)
    }
}
