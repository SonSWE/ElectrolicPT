package com.example.electrolicpt

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.electrolicpt.R
import com.example.electrolicpt.SignupActivity
import com.example.electrolicpt.Utils.SliderTransform
import com.example.electrolicpt.adapter.SliderApderter
import com.google.android.material.button.MaterialButton

class SelectGenderFragment : Fragment() {

    private lateinit var activity: SignupActivity
    private lateinit var sliderGender: ViewPager2
    var selectIndex = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_select_gender, container, false)
        try {
            activity = getActivity() as SignupActivity
            val radioGroupGender = rootView.findViewById<RadioGroup>(R.id.radioGroupGender)
            sliderGender = rootView.findViewById<ViewPager2>(R.id.sliderGender)
            var lstImage = ArrayList<Int>()
            lstImage.add(R.drawable.male)
            lstImage.add(R.drawable.female)
            lstImage.add(R.drawable.other_gender)

            sliderGender.adapter = SliderApderter(sliderGender, lstImage)
            sliderGender.clipToPadding = false
            sliderGender.clipChildren = false
            sliderGender.offscreenPageLimit = 2
            sliderGender.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            val transform = CompositePageTransformer()
            transform.addTransformer(MarginPageTransformer(40))
            transform.addTransformer(SliderTransform())
            sliderGender.setPageTransformer(transform)

            sliderGender.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    selectIndex = position

                    if (selectIndex == 0) {
                        radioGroupGender.check(R.id.rdMale)
                    } else if (selectIndex == 1) {
                        radioGroupGender.check(R.id.rdFemale)
                    } else if (selectIndex == 2) {
                        radioGroupGender.check(R.id.rdOther)
                    }

                }
            })

            val btnNext = rootView.findViewById<MaterialButton>(R.id.btnNext)
            btnNext.setOnClickListener(View.OnClickListener {
                activity.user_info.gender = selectIndex
                activity.NextStep()
            })


            radioGroupGender.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener { group, checkedId ->
                    val radio: RadioButton = rootView.findViewById(checkedId)
                    if (checkedId == R.id.rdMale) {
                        selectIndex = 0
                    } else if (checkedId == R.id.rdFemale) {
                        selectIndex = 1
                    } else if (checkedId == R.id.rdOther) {
                        selectIndex = 2
                    }
                    sliderGender.currentItem = selectIndex
                })
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
        }
        // Inflate the layout for this fragment
        return rootView
    }

}