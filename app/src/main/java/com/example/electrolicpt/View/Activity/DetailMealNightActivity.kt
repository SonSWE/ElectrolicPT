package com.example.electrolicpt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.electrolicpt.adapter.DetailMealNightAdapter
import com.example.electrolicpt.adapter.MeaStorageNightAdapter
import com.example.electrolicpt.ObjectInfor.FoodInfo
import com.example.electrolicpt.ObjectInfor.MealNightInfo
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailMealNightActivity : AppCompatActivity() {
    private var recyclerViewNight: RecyclerView? = null
    private var recyclerViewFoodNightAdd: RecyclerView? = null
    private var mDetailMealNightAdapter: DetailMealNightAdapter? = null
    private var mMealStorageNightAdapter: MeaStorageNightAdapter? = null
    private var mListFoodNight: MutableList<FoodInfo>? = null
    private var mListStorageNight: MutableList<MealNightInfo>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_meal_night)

        val bottomSheet = findViewById<FrameLayout>(R.id.sheetNight)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        // Thiết lập chiều cao tối thiểu (peek height) và trạng thái ban đầu
        bottomSheetBehavior.peekHeight = 80
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        // Khởi tạo UI và danh sách thực phẩm
        initUi()
        initUi_Storage()

        // Bắt sự kiện khi người dùng nhấn nút ImageView
        val myImageViewNight = findViewById<ImageView>(R.id.myImageViewNight)
        myImageViewNight.setOnClickListener { v: View? ->
            val intent = Intent(this@DetailMealNightActivity, MealMenuActivity::class.java)
            startActivity(intent)
        }
        // Lấy danh sách thực phẩm từ cơ sở dữ liệu Firebase
        listFoodNightDatabase
        listFoodStorageNightDatabase
    }

    private fun initUi() {
        // Khởi tạo RecyclerView và Adapter cho DetailMeal
        val recyclerViewNight: RecyclerView = findViewById(R.id.recyclerViewFoodNight)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerViewNight.layoutManager = linearLayoutManager
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerViewNight.addItemDecoration(dividerItemDecoration)


        // Khởi tạo danh sách thực phẩm
        mListFoodNight = ArrayList()
        mDetailMealNightAdapter = DetailMealNightAdapter(mListFoodNight)
        recyclerViewNight.setAdapter(mDetailMealNightAdapter)


    }

    private fun initUi_Storage() {
        // Khởi tạo RecyclerView và Adapter cho DetailMeal
        val recyclerViewFoodNightAdd: RecyclerView = findViewById(R.id.recyclerViewFoodAddNight)
        val linearLayoutManagerFoodAdd = LinearLayoutManager(this)
        recyclerViewFoodNightAdd.layoutManager = linearLayoutManagerFoodAdd
        val dividerItemDecorationFoodAdd =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerViewFoodNightAdd.addItemDecoration(dividerItemDecorationFoodAdd)

        // Khởi tạo danh sách thực phẩm add
        mListStorageNight = ArrayList()
        mMealStorageNightAdapter = MeaStorageNightAdapter(mListStorageNight, onDeleteClickListener)
        recyclerViewFoodNightAdd.setAdapter(mMealStorageNightAdapter)
    }

    private val listFoodNightDatabase: Unit
        private get() {
            // Kết nối đến cơ sở dữ liệu Firebase
            val database = FirebaseDatabase.getInstance()
            val myRef = database.reference.child("food")

            // Lắng nghe sự thay đổi trong dữ liệu
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Xóa danh sách cũ trước khi cập nhật
                    mListFoodNight!!.clear()

                    // Duyệt qua dữ liệu mới và thêm vào danh sách
                    for (dataSnapshot in snapshot.children) {
                        val food = dataSnapshot.getValue(FoodInfo::class.java)
                        if (food != null) {
                            food.img_food = dataSnapshot.child("img_food").getValue(
                                String::class.java
                            )
                            mListFoodNight!!.add(food)
                        }
                    }


                    // Thông báo cho Adapter biết là dữ liệu đã thay đổi
                    mDetailMealNightAdapter!!.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Xử lý khi có lỗi xảy ra
                    Toast.makeText(
                        this@DetailMealNightActivity,
                        "Lấy danh sách thực phẩm thất bại!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

    private val listFoodStorageNightDatabase: Unit
        private get() {
            // Kết nối đến cơ sở dữ liệu Firebase
            val database = FirebaseDatabase.getInstance()
            val myRef = database.reference.child("Meal_Night")

            // Lắng nghe sự thay đổi trong dữ liệu
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Xóa danh sách cũ trước khi cập nhật
                    mListStorageNight!!.clear()

                    // Duyệt qua dữ liệu mới và thêm vào danh sách
                    for (dataSnapshot in snapshot.children) {
                        val foodStorage = dataSnapshot.getValue(MealNightInfo::class.java)
                        if (foodStorage != null) {
//                            foodStorage.img_food = dataSnapshot.child("img_food").getValue(String::class.java) ?: ""
                            mListStorageNight!!.add(foodStorage)
                        }
                    }


                    // Thông báo cho Adapter biết là dữ liệu đã thay đổi
                    mMealStorageNightAdapter!!.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Xử lý khi có lỗi xảy ra
                    Toast.makeText(
                        this@DetailMealNightActivity,
                        "Lấy danh sách thực phẩm thất bại!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    private val onDeleteClickListener = object : MeaStorageNightAdapter.OnDeleteClickListener {
        override fun onDeleteClick(id: String) {
            deleteDataInFirebase(id)
        }
    }

    private fun deleteDataInFirebase(id: String) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child("Meal_Night")

        // Thực hiện một truy vấn để tìm nút con có id tương ứng
        val query = myRef.orderByChild("id").equalTo(id.toDouble())

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    // Xóa nút cha của nút con được tìm thấy
                    dataSnapshot.ref.removeValue()
                        ?.addOnSuccessListener {
                            // Xóa item trên giao diện
                            mListStorageNight?.removeAll { it.id.toString() == id }
                            mMealStorageNightAdapter?.notifyDataSetChanged()

                            Toast.makeText(
                                this@DetailMealNightActivity,
                                "Xóa thành công",
                                Toast.LENGTH_SHORT
                            ).show()
                        }?.addOnFailureListener {
                            Toast.makeText(
                                this@DetailMealNightActivity,
                                "Xóa thất bại",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu
                Toast.makeText(
                    this@DetailMealNightActivity,
                    "Không tìm thấy mục để xóa",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}