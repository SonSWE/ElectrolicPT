package com.example.electrolicpt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.electrolicpt.DataAccess.UserDa
import com.example.electrolicpt.ObjectInfor.UserInfo
import com.example.electrolicpt.Utils.LoadingDialog
import com.example.electrolicpt.Utils.UserShareReferentHelper
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class LoginActivity : AppCompatActivity() {
    var userLogin: UserInfo? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userLogin = UserShareReferentHelper().getUser(this)
        if (userLogin != null && userLogin?.phone != "") {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val btnLogin = findViewById<MaterialButton>(R.id.btnLogin)
        btnLogin.setOnClickListener(View.OnClickListener {
            var tvPhone = findViewById<TextView>(R.id.tvPhone).text.trim().toString()
            if (tvPhone == "") {
                Toast.makeText(
                    this@LoginActivity, "SĐT không được để trống!",
                    Toast.LENGTH_LONG
                ).show()
                return@OnClickListener
            }

            var tvPassword = findViewById<TextView>(R.id.tvPassword).text.trim().toString()
            if (tvPassword == "") {
                Toast.makeText(
                    this@LoginActivity, "Mật khẩu không được để trống!",
                    Toast.LENGTH_LONG
                ).show()
                return@OnClickListener
            }
            val loader = LoadingDialog(this@LoginActivity)
            loader.show()
            UserDa().LoginV3(tvPhone, tvPassword)?.addOnSuccessListener { _user ->
                loader.cancel()
                if (_user != null && _user.phone != "") {
                    Toast.makeText(
                        this@LoginActivity, "Đăng nhập thành công!",
                        Toast.LENGTH_LONG
                    ).show();

                    UserShareReferentHelper().saveUser(_user, this@LoginActivity)

                    val mainActivity = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(mainActivity)
                } else {
                    Toast.makeText(
                        this@LoginActivity, "Sai tài khoản hoặc mật khẩu!",
                        Toast.LENGTH_LONG
                    ).show();
                }
            }?.addOnFailureListener { e ->
                loader.cancel()
                Log.e("LOG DEBUG FragmentPatientHomeBinding| ", e.message.toString())
            }
        })

        val btnGoSignup = findViewById<MaterialButton>(R.id.btnGoSignup)
        btnGoSignup.setOnClickListener(View.OnClickListener {
            val signup = Intent(this, SignupActivity::class.java)
            startActivity(signup)
        })

    }


}