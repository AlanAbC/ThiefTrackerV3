package com.iw.thieftrackerv3

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar!!.hide()
    }
}
