package com.example.electrolicpt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.electrolicpt.R
import com.example.electrolicpt.SignupActivity
import com.fredporciuncula.phonemoji.PhonemojiTextInputEditText
import com.google.android.material.button.MaterialButton

class PhoneNumberFragment : Fragment() {
    private lateinit var activity: SignupActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_phone_number, container, false)
        activity = getActivity() as SignupActivity

        val txtPhoneNumber = rootView.findViewById<PhonemojiTextInputEditText>(R.id.txtPhoneNumber)
        val btnNext = rootView.findViewById<MaterialButton>(R.id.btnNext)
        btnNext.setOnClickListener(View.OnClickListener{
            if(txtPhoneNumber.text == null || txtPhoneNumber.text.toString() == "" || txtPhoneNumber.text.toString().replace("+","") == txtPhoneNumber.initialCountryCode.toString()){
                Toast.makeText(activity, "Vui lòng nhập SĐT", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            activity.user_info.phone = txtPhoneNumber.text.toString().replace(" ","").trim()
            activity.NextStep()
        })



        // Inflate the layout for this fragment
        return rootView
    }


}