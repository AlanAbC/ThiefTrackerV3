package com.iw.thieftrackerv3.Config

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.iw.thieftrackerv3.Services.Login.Login

class Session @SuppressLint("CommitPrefEdits") constructor(context: Context?) {
    private var login: Login? = null
    private var sharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private val PREFER_NAME = "pickpal_tablet"
    private val PRIVATE_MODE = 0

    init {
        if (context != null) {
            this.sharedPreferences = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE)
        }
        this.editor = this.sharedPreferences!!.edit()
    }

    constructor(context: Context?, login: Login?) : this(context) {
        this.login = login
        if (context != null) {
            this.sharedPreferences = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE)
        }
        this.editor = this.sharedPreferences!!.edit()
    }

    fun createSession(): Int? {
        try {
            editor!!.putInt("id", this.login!!.id!!)
            editor!!.putString("name", this.login!!.name!!)
            editor!!.putString("email", this.login!!.email!!)
            editor!!.putString("token", this.login!!.token!!)
            editor!!.commit()
            return 1
        } catch (e: Exception) {
            return 0
        }

    }

    fun getSession(): Login {
        val loginData = Login()
        loginData.id = sharedPreferences!!.getInt("id", 0)
        loginData.name = sharedPreferences!!.getString("name", null)
        loginData.token = sharedPreferences!!.getString("token", null)
        loginData.email = sharedPreferences!!.getString("email", null)
        return loginData
    }

    fun closeSession(): Int? {
        try {
            editor!!.clear()
            editor!!.commit()
            return 1
        } catch (e: Exception) {
            return 0
        }

    }
}