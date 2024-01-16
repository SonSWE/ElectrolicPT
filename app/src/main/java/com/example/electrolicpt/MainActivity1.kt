package com.example.electrolicpt

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.electrolicpt.ui.theme.ElectrolicPTTheme
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class MainActivity1 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ElectrolicPTTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
        SendOtp("+84326737652")
//        try {
//            val options = PhoneAuthOptions.newBuilder()
//                .setPhoneNumber("+84867822410") // Phone number to verify
//                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                .setActivity(this) // Activity (for callback binding)
//                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                    // below method is used when
//                    // OTP is sent from Firebase
//                    override fun onCodeSent(
//                        s: String,
//                        forceResendingToken: PhoneAuthProvider.ForceResendingToken
//                    ) {
//                        super.onCodeSent(s, forceResendingToken)
//                        // when we receive the OTP it
//                        // contains a unique id which
//                        // we are storing in our string
//                        // which we have already created.
////                        verificationId = s
//                    }
//
//                    // this method is called when user
//                    // receive OTP from Firebase.
//                    override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
//                        // below line is used for getting OTP code
//                        // which is sent in phone auth credentials.
//                        val code = phoneAuthCredential.smsCode
//
//                        // checking if the code
//                        // is null or not.
//                        if (code != null) {
//
//                        }
//                    }
//
//                    // this method is called when firebase doesn't
//                    // sends our OTP code due to any error or issue.
//                    override fun onVerificationFailed(e: FirebaseException) {
//                        // displaying error message with firebase Exception.
//                        Toast.makeText(this@MainActivity1, e.message, Toast.LENGTH_LONG).show()
//                    }
//                }
//                ) // OnVerificationStateChangedCallbacks
//                .build()
//            PhoneAuthProvider.verifyPhoneNumber(options)
//        } catch (e: Exception) {
//            Log.e("Error", e.message.toString())
//        }

    }

    fun SendOtp(phoneNumber: String) {
        try {
            val options = PhoneAuthOptions.newBuilder()
                .setPhoneNumber(phoneNumber) // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this@MainActivity1) // Activity (for callback binding)
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
//                        verificationId = s
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
                            // if the code is not null then
                            // we are setting that code to
                            // our OTP edittext field.
//                    edtOTP!!.setText(code)

                            // after setting this code
                            // to OTP edittext field we
                            // are calling our verifycode method.
//                            verifyCode(code)
                        }
                    }

                    // this method is called when firebase doesn't
                    // sends our OTP code due to any error or issue.
                    override fun onVerificationFailed(e: FirebaseException) {
                        // displaying error message with firebase Exception.
                        Toast.makeText(this@MainActivity1, e.message, Toast.LENGTH_LONG).show()
                    }
                }
                ) // OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ElectrolicPTTheme {
        Greeting("Android")
    }
}