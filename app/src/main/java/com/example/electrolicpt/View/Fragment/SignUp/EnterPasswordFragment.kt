package com.example.electrolicpt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.electrolicpt.Utils.CommomFunc
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class EnterPasswordFragment : Fragment() {

    private lateinit var activity: SignupActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_enter_password, container, false)
        activity = getActivity() as SignupActivity

        val pwbPassword = rootView.findViewById<TextInputEditText>(R.id.pwbPassword)
        val pwbRePassword = rootView.findViewById<TextInputEditText>(R.id.pwbRePassword)

        val btnNext = rootView.findViewById<MaterialButton>(R.id.btnNext)
        btnNext.setOnClickListener(View.OnClickListener{
            val password = pwbPassword.text.toString()
            val rePassword = pwbRePassword.text.toString()
            if(password == "" || rePassword == "pwbRePassword"){
                Toast.makeText(activity, "Mật khẩu không được để trống", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if(password != rePassword){
                Toast.makeText(activity, "Mật khẩu nhắc lại không đúng", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            activity.user_info.password = CommomFunc().encrypt(password)
            val de = CommomFunc().decrypt(activity.user_info.password.toString())
            activity.NextStep()
        })
        // Inflate the layout for this fragment
        return rootView
    }

}