package com.example.electrolicpt

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.kevalpatel2106.rulerpicker.RulerValuePicker
import com.kevalpatel2106.rulerpicker.RulerValuePickerListener
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar

class UserProfileFragment : Fragment() {

    private lateinit var activity: SignupActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_user_profile, container, false)
        activity = getActivity() as SignupActivity

        val txtFullname = rootView.findViewById<TextInputEditText>(R.id.txtFullname)
        val txtBirthday = rootView.findViewById<TextInputEditText>(R.id.txtBirthday)
        val btnDatePicker = rootView.findViewById<MaterialButton>(R.id.btnDatePicker)
        txtBirthday.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val calendar = Calendar.getInstance()
                val year = calendar[Calendar.YEAR]
                val month = calendar[Calendar.MONTH]
                val day = calendar[Calendar.DAY_OF_MONTH]
                val datePickerDialog = DatePickerDialog(
                    activity,
                    { view: DatePicker?, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                        val selectedDate =
                            selectedDay.toString() + "/" + (selectedMonth + 1) + "/" + selectedYear
                        txtBirthday.setText(selectedDate)
                    },
                    year, month, day
                )
                datePickerDialog.show()
            }
        })
        btnDatePicker.setOnClickListener(View.OnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH]
            val day = calendar[Calendar.DAY_OF_MONTH]
            val datePickerDialog = DatePickerDialog(
                activity,
                { view: DatePicker?, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                    val selectedDate =
                        selectedDay.toString() + "/" + (selectedMonth + 1) + "/" + selectedYear
                    txtBirthday.setText(selectedDate)
                },
                year, month, day
            )
            datePickerDialog.show()
        })
        val txtHeight = rootView.findViewById<TextInputEditText>(R.id.txtHeight)

        val pickerHeight = rootView.findViewById<RulerValuePicker>(R.id.pickerHeight)

//        txtHeight.filters =  arrayOf<InputFilter>(InputFilterMinMax(100, 300))
        pickerHeight.setMinMaxValue(100, 300)
        pickerHeight.selectValue(175)

//        txtHeight.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
//            override fun afterTextChanged(s: Editable) {
//                var str = txtHeight.text.toString()
//                if(str != null && str != ""){
//                    var value = str.toInt()
//                    pickerHeight.selectValue(value)
//                }
//
//            }
//        })

        pickerHeight.setValuePickerListener(object : RulerValuePickerListener {
            override fun onValueChange(value: Int) {
                txtHeight.setText(value.toString())
            }

            override fun onIntermediateValueChange(selectedValue: Int) {
//                txtHeight.setText(selectedValue.toString())
                //Value changed but the user is still scrolling the ruler.
                //This value is not final value. You can utilize this value to display the current selected value.
            }
        })

        val txtWeight = rootView.findViewById<TextInputEditText>(R.id.txtWeight)
        val pickerWeight = rootView.findViewById<RulerValuePicker>(R.id.pickerWeight)
//        txtWeight.filters =  arrayOf<InputFilter>(InputFilterMinMax(10, 100))
        pickerWeight.setMinMaxValue(10, 100)
        pickerWeight.selectValue(65)

//        txtWeight.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
//            override fun afterTextChanged(s: Editable) {
//                var str = txtWeight.text.toString()
//                if(str != null && str != ""){
//                    var value = str.toInt()
//                    pickerWeight.selectValue(value)
//                }
//
//            }
//        })


        pickerWeight.setValuePickerListener(object : RulerValuePickerListener {
            override fun onValueChange(value: Int) {
                txtWeight.setText(value.toString())
            }

            override fun onIntermediateValueChange(selectedValue: Int) {
                //Value changed but the user is still scrolling the ruler.
                //This value is not final value. You can utilize this value to display the current selected value.
            }
        })

        val btnNext = rootView.findViewById<MaterialButton>(R.id.btnNext)
        btnNext.setOnClickListener(View.OnClickListener {
            if (txtFullname.text.toString() == "") {
                Toast.makeText(activity, "Chưa nhập họ và tên", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if (txtBirthday.text.toString() == "") {
                Toast.makeText(activity, "Chưa nhập ngày sinh", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            activity.user_info.full_Name = txtFullname.text.toString()
            activity.user_info.birthday = SimpleDateFormat("dd/MM/yyyy").parse(txtBirthday.text.toString()).time
            activity.user_info.heigth = txtHeight.text.toString().toDouble()
            activity.user_info.weigth = txtWeight.text.toString().toDouble()

            activity.NextStep()
        })

        // Inflate the layout for this fragment
        return rootView
    }

}