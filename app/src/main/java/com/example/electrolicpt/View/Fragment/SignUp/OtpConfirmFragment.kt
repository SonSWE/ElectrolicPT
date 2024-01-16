package com.example.electrolicpt

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.chaos.view.PinView
import com.example.electrolicpt.R
import com.example.electrolicpt.SignupActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class OtpConfirmFragment : Fragment() {
    private lateinit var activity: SignupActivity

    // variable for FirebaseAuth class
    private var mAuth: FirebaseAuth? = null
    private var firebaseApp: FirebaseApp? = null

    // variable for our text input
    // field for phone and OTP.
    private var phoneNumber:String = ""

    // field for phone and OTP.
    private val edtOTP: EditText? = null

    // buttons for generating OTP and verifying OTP
    private val btnNext: MaterialButton? = null
    private val generateOTPBtn: Button? = null

    // string for storing our verification ID
    private var verificationId: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_otp_confirm, container, false)

        try {
            activity = getActivity() as SignupActivity
            val tvPhone = rootView.findViewById<TextView>(R.id.tvPhone)
            tvPhone.text = activity.user_info.phone

            val btnNext = rootView.findViewById<MaterialButton>(R.id.btnNext)
            btnNext.setOnClickListener(View.OnClickListener {
                val pinview = rootView.findViewById<PinView>(R.id.pinview)
                val otp = pinview.text.toString()
                if(otp.length < 6){
                    return@OnClickListener
                }
                if(activity.verifyCode(otp)){
                    activity.NextStep()
                }else{
                    Toast.makeText(activity, "OPT không hợp lệ", Toast.LENGTH_SHORT).show()
                }

            })

            val btnResendOtp = rootView.findViewById<MaterialButton>(R.id.btnResendOtp)
            btnResendOtp.setOnClickListener(View.OnClickListener {
                phoneNumber = activity.user_info?.phone.toString()
                if(phoneNumber != ""){
                    activity.SendOtp(phoneNumber)
                }else{
                    Toast.makeText(activity, "SĐT không hợp lệ", Toast.LENGTH_SHORT).show()
                }
            })


        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
        }
        // Inflate the layout for this fragment
        return rootView
    }

    override fun onResume() {
//        SendOtp(activity.user_info?.phone!!)
        Log.e("DEBUG", "onResume of HomeFragment")
        super.onResume()
    }

    override fun onStart() {
//        SendOtp(activity.user_info?.phone!!)
        Log.e("DEBUG", "onStart of HomeFragment")
        super.onStart()
    }



}