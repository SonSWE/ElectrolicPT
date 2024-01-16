package com.example.electrolicpt

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.electrolicpt.R
import com.example.electrolicpt.SignupActivity
import com.google.android.material.button.MaterialButton

class SignUpSuccessFragment : Fragment() {
    private lateinit var activity: SignupActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_sign_up_success, container, false)
        activity = getActivity() as SignupActivity
        val btnNext = rootView.findViewById<MaterialButton>(R.id.btnNext)
        btnNext.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        })
        // Inflate the layout for this fragment
        return rootView
    }

}