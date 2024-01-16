package com.example.electrolicpt



import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.electrolicpt.DataAccess.UserDa
import com.example.electrolicpt.ObjectInfor.UserInfo
import com.example.electrolicpt.Utils.LoadingDialog
import com.example.electrolicpt.adapter.PagerAdapter

import com.google.firebase.FirebaseException

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import java.util.concurrent.TimeUnit

class SignupActivity : AppCompatActivity() {
    private lateinit var viewpager: ViewPager
    private lateinit var adapter: PagerAdapter
    private lateinit var page_indicator: WormDotsIndicator
    private var currentStep = 0

    var user_info = UserInfo()

    private var mAuth: FirebaseAuth? = null

    private var verificationId: String? = null
    private var opt: String? = "000000"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (currentStep == 0) {
                    finish()
                } else {
                    PreviousStep()
                }
            }
        })
        initViews()

        adapter = PagerAdapter(supportFragmentManager)
        adapter.addFragment(PhoneNumberFragment(), "phone")
        adapter.addFragment(OtpConfirmFragment(), "otp")
        adapter.addFragment(EnterPasswordFragment(), "account")
        adapter.addFragment(SelectGenderFragment(), "Gender")
        adapter.addFragment(UserProfileFragment(), "Profile")
        adapter.addFragment(SelectTargetFragment(), "Target")
        adapter.addFragment(SignUpSuccessFragment(), "Success")

        viewpager.adapter = adapter
        page_indicator.attachTo(viewpager)


    }

    private fun initViews() {
        try {
            page_indicator = findViewById<WormDotsIndicator>(R.id.page_indicator)
            page_indicator.setDotIndicatorColor(R.color.orange)
            page_indicator.isClickable = false
            page_indicator.dotsClickable = false
            page_indicator.setOnClickListener(View.OnClickListener {

            })
            viewpager = findViewById(R.id.pager)

            mAuth = Firebase.auth

//
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
        }
    }

    fun NextStep() {
        try {
            if (currentStep == 0) {
                if (user_info.phone == "" || user_info.phone?.length!! < 9) {
                    //kiểm tra có bỏ trống sđt không
                    Toast.makeText(
                        this@SignupActivity, "SĐT không được để trống!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    //nếu nhập sđt rồi thì kiểm tra xem sđt này đã được đăng ký tài khoản nào chưa
                    UserDa().GetByPhone(user_info.phone!!)?.addOnSuccessListener {
                        if (it != null && it.phone != "") {
                            Toast.makeText(
                                this@SignupActivity, "SĐT đã được đăng ký!",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            //nếu chưa có user nào tồn tại sđt này thì chuyển sang bước tiếp theo

                            SendOtp(user_info.phone!!)
                            NextPage()
                        }
                    }?.addOnFailureListener { e ->
                        Log.e("LOG| ", e.message.toString())
                    }
                }
            } else if (currentStep == 5) {
                val loader = LoadingDialog(this@SignupActivity)
                loader.show()
                UserDa().Insert(user_info)?.addOnSuccessListener {
                    loader.cancel()
                    if (it > 0) {
                        NextPage()
                    } else {
                        Toast.makeText(
                            this@SignupActivity, "Đăng ký thất bại!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }?.addOnFailureListener { e ->
                    Log.e("LOG| ", e.message.toString())
                }
            }
            else{
                NextPage()
            }
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
        }
    }

    fun NextPage(){
        currentStep++
        viewpager.setCurrentItem(currentStep, true)
    }

    fun PreviousStep() {
        currentStep--
        viewpager.setCurrentItem(currentStep, true)
    }

    fun SendOtp(phoneNumber: String) {
        try {
            val options = PhoneAuthOptions.newBuilder(mAuth!!)
                .setPhoneNumber(phoneNumber) // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this@SignupActivity) // Activity (for callback binding)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    // below method is used when
                    // OTP is sent from Firebase
                    override fun onCodeSent(
                        s: String,
                        forceResendingToken: PhoneAuthProvider.ForceResendingToken
                    ) {
                        super.onCodeSent(s, forceResendingToken)
                        // when we receive the OTP it
                        // contains a unique id which
                        // we are storing in our string
                        // which we have already created.
                        verificationId = s
                    }

                    // this method is called when user
                    // receive OTP from Firebase.
                    override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                        // below line is used for getting OTP code
                        // which is sent in phone auth credentials.
                        val code = phoneAuthCredential.smsCode

                        // checking if the code
                        // is null or not.
                        if (code != null) {
                            opt = code
//                            verifyCode(code)
                        }
                    }

                    // this method is called when firebase doesn't
                    // sends our OTP code due to any error or issue.
                    override fun onVerificationFailed(e: FirebaseException) {
                        // displaying error message with firebase Exception.
                        Toast.makeText(this@SignupActivity, e.message, Toast.LENGTH_LONG).show()
                    }
                }
                ) // OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
        }
    }

    // below method is use to verify code from Firebase.
    fun verifyCode(code: String): Boolean {
//        try {
//            val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
//            return true
//        } catch (e: Exception) {
//            return false
//        }
        if (opt == code) {
            return true
        }
        return false
    }
}