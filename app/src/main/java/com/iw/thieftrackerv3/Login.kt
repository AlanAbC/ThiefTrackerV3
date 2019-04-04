package com.iw.thieftrackerv3

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.iw.thieftrackerv3.Config.Service
import com.iw.thieftrackerv3.Config.Session
import com.iw.thieftrackerv3.Services.Services
import okhttp3.Callback
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class Login : AppCompatActivity() {
    var btn_register: TextView? = null
    var input_user: EditText? = null
    var input_pass: EditText? = null
    var btn_login: Button? = null
    var services: Services = Service().getService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.hide()

        btn_register = findViewById(R.id.btn_register)
        input_user = findViewById(R.id.input_user)
        input_pass = findViewById(R.id.input_pass)
        btn_login = findViewById(R.id.btn_login)
        setOnClicks()
    }
    fun RedirectHome(){
        val intent = Intent(this, Splash::class.java)
        startActivity(intent)
        finish()
    }
    fun setOnClicks(){
        btn_register!!.setOnClickListener{
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
        btn_login!!.setOnClickListener{
            val call: Call<com.iw.thieftrackerv3.Services.Login.Login> = services.login(input_user!!.text.toString(), input_pass!!.text.toString())
            call.enqueue(object : retrofit2.Callback<com.iw.thieftrackerv3.Services.Login.Login> {
                override fun onResponse(call: retrofit2.Call<com.iw.thieftrackerv3.Services.Login.Login>, response: Response<com.iw.thieftrackerv3.Services.Login.Login>) {
                    when (response.code()) {
                        200 -> {
                            val obj_login = response.body()
                            val session: Session = Session(context = applicationContext, login = response.body())
                            session.createSession()
                            if(session.getSession().id != 0){
                                RedirectHome()
                            }
                        }
                        else -> {
                        }
                    }
                }

                override fun onFailure(call: retrofit2.Call<com.iw.thieftrackerv3.Services.Login.Login>, t: Throwable) {
                }
            })
        }
    }
}
