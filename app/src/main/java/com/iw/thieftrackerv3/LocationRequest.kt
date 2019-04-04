package com.iw.thieftrackerv3

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.WindowManager

class LocationRequest : AppCompatActivity() {
    internal lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar!!.hide()
        setContentView(R.layout.activity_location_request)

        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val dialog = AlertDialog.Builder(this@LocationRequest)
        dialog.setCancelable(false)
        dialog.setTitle("Activar Ubicación")
        .setMessage("Para darte una mejor experiencia")
        .setPositiveButton("Configurar ubicación"
        ) { paramDialogInterface, paramInt ->
            ActivityCompat.requestPermissions(this@LocationRequest,
                arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION), 0)
        }
                    .setNegativeButton("Cancelar", object: DialogInterface.OnClickListener {
        override fun onClick(paramDialogInterface: DialogInterface, paramInt:Int) {
        val mainIntent = Intent(this@LocationRequest, Home::class.java)
        mainIntent.putExtra("section", "home")
        mainIntent.putExtra("direction", "home")
        this@LocationRequest.startActivity(mainIntent)
        this@LocationRequest.finish()
        }
        })
        dialog.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == 0) {
            val mainIntent = Intent(this@LocationRequest, Home::class.java)
            this@LocationRequest.startActivity(mainIntent)
            this@LocationRequest.finish()
        } else {
            val mainIntent = Intent(this@LocationRequest, Home::class.java)
            mainIntent.putExtra("section", "home")
            mainIntent.putExtra("direction", "home")
            this@LocationRequest.startActivity(mainIntent)
            this@LocationRequest.finish()
        }
    }
}
