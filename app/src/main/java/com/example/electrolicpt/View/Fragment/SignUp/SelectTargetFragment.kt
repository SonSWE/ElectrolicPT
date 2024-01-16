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

class SelectTargetFragment : Fragment() {
    private lateinit var activity: SignupActivity
    private lateinit var sliderTarget: ViewPager2
    var selectIndex = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_select_target, container, false)
        try {
            activity = getActivity() as SignupActivity
            val radioGroupTarget = rootView.findViewById<RadioGroup>(R.id.radioGroupTarget)
            sliderTarget = rootView.findViewById<ViewPager2>(R.id.sliderTarget)
            var lstImage = ArrayList<Int>()
            lstImage.add(R.drawable.gymfitness)
            lstImage.add(R.drawable.gym_giamcan)
            lstImage.add(R.drawable.luyen_tap)

            sliderTarget.adapter = SliderApderter(sliderTarget, lstImage)
            sliderTarget.clipToPadding = false
            sliderTarget.clipChildren = false
            sliderTarget.offscreenPageLimit = 2
            sliderTarget.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            val transform = CompositePageTransformer()
            transform.addTransformer(MarginPageTransformer(40))
            transform.addTransformer(SliderTransform())
            sliderTarget.setPageTransformer(transform)

            sliderTarget.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    selectIndex = position

                    if (selectIndex == 0) {
                        radioGroupTarget.check(R.id.rd1)
                    } else if (selectIndex == 1) {
                        radioGroupTarget.check(R.id.rd2)
                    } else if (selectIndex == 2) {
                        radioGroupTarget.check(R.id.rd3)
                    }

                }
            })

            val btnNext = rootView.findViewById<MaterialButton>(R.id.btnNext)
            btnNext.setOnClickListener(View.OnClickListener {
                activity.user_info.target = selectIndex
                activity.NextStep()
            })


            radioGroupTarget.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener { group, checkedId ->
                    val radio: RadioButton = rootView.findViewById(checkedId)
                    if (checkedId == R.id.rd1) {
                        selectIndex = 0
                    } else if (checkedId == R.id.rd2) {
                        selectIndex = 1
                    } else if (checkedId == R.id.rd3) {
                        selectIndex = 2
                    }
                    sliderTarget.currentItem = selectIndex
                })
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
        }
        // Inflate the layout for this fragment
        return rootView
    }
}