package com.example.electrolicpt


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.electrolicpt.adapter.DetailMealAdapter
import com.example.electrolicpt.adapter.MeaStorageAdapter
import com.example.electrolicpt.ObjectInfor.FoodInfo
import com.example.electrolicpt.ObjectInfor.MealMorningInfo

import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class DetailMealActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var recyclerViewFoodAdd: RecyclerView? = null
    private var mDetailMealAdapter: DetailMealAdapter? = null
    private var mMealStorageAdapter: MeaStorageAdapter? = null
    private var mListFood: MutableList<FoodInfo>? = null
    private var mListStorage: MutableList<MealMorningInfo>? = null

    var name: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_meal)

        val bottomSheet = findViewById<FrameLayout>(R.id.sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

// Thiết lập chiều cao tối thiểu (peek height) và trạng thái ban đầu
        bottomSheetBehavior.peekHeight = 80
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        // Khởi tạo UI và danh sách thực phẩm
        initUi()
        initUi_Storage()
        // Bắt sự kiện khi người dùng nhấn nút ImageView
        val myImageView = findViewById<ImageView>(R.id.myImageView)
        myImageView.setOnClickListener { v: View? ->
            val intent = Intent(this@DetailMealActivity, MealMenuActivity::class.java)
            startActivity(intent)
        }

        // Lấy danh sách thực phẩm từ cơ sở dữ liệu Firebase
        listFoodDatabase
        listFoodStorageDatabase
    }

    private fun initUi() {
        // Khởi tạo RecyclerView và Adapter cho DetailMeal
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewFood)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)


        // Khởi tạo danh sách thực phẩm
        mListFood = ArrayList()
        mDetailMealAdapter = DetailMealAdapter(mListFood)
        recyclerView.setAdapter(mDetailMealAdapter)


    }

    private fun initUi_Storage() {
        // Khởi tạo RecyclerView và Adapter cho DetailMeal
        val recyclerViewFoodAdd: RecyclerView = findViewById(R.id.recyclerViewFoodAdd)
        val linearLayoutManagerFoodAdd = LinearLayoutManager(this)
        recyclerViewFoodAdd.layoutManager = linearLayoutManagerFoodAdd
        val dividerItemDecorationFoodAdd =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerViewFoodAdd.addItemDecoration(dividerItemDecorationFoodAdd)

        // Khởi tạo danh sách thực phẩm add
        mListStorage = ArrayList()
        mMealStorageAdapter = MeaStorageAdapter(mListStorage, onDeleteClickListener)
        recyclerViewFoodAdd.setAdapter(mMealStorageAdapter)
    }

    private val listFoodDatabase: Unit
        private get() {
            // Kết nối đến cơ sở dữ liệu Firebase
            val database = FirebaseDatabase.getInstance()
            val myRef = database.reference.child("food")

            // Lắng nghe sự thay đổi trong dữ liệu
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Xóa danh sách cũ trước khi cập nhật
                    mListFood!!.clear()

                    // Duyệt qua dữ liệu mới và thêm vào danh sách
                    for (dataSnapshot in snapshot.children) {
                        val food = dataSnapshot.getValue(FoodInfo::class.java)
                        if (food != null) {
                            food.img_food = dataSnapshot.child("img_food").getValue(
                                String::class.java
                            )
                            mListFood!!.add(food)
                        }
                    }


                    // Thông báo cho Adapter biết là dữ liệu đã thay đổi
                    mDetailMealAdapter!!.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Xử lý khi có lỗi xảy ra
                    Toast.makeText(
                        this@DetailMealActivity,
                        "Lấy danh sách thực phẩm thất bại!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

    private val listFoodStorageDatabase: Unit
        private get() {
            // Kết nối đến cơ sở dữ liệu Firebase
            val database = FirebaseDatabase.getInstance()
            val myRef = database.reference.child("Meal_Morning")

            // Lắng nghe sự thay đổi trong dữ liệu
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Xóa danh sách cũ trước khi cập nhật
                    mListStorage!!.clear()

                    // Duyệt qua dữ liệu mới và thêm vào danh sách
                    for (dataSnapshot in snapshot.children) {
                        val foodStorage = dataSnapshot.getValue(MealMorningInfo::class.java)
                        if (foodStorage != null) {
//                            foodStorage.img_food = dataSnapshot.child("img_food").getValue(String::class.java) ?: ""
                            mListStorage!!.add(foodStorage)
                        }
                    }


                    // Thông báo cho Adapter biết là dữ liệu đã thay đổi
                    mMealStorageAdapter!!.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Xử lý khi có lỗi xảy ra
                    Toast.makeText(
                        this@DetailMealActivity,
                        "Lấy danh sách thực phẩm thất bại!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    private val onDeleteClickListener = object : MeaStorageAdapter.OnDeleteClickListener {
        override fun onDeleteClick(id: String) {
            deleteDataInFirebase(id)
        }
    }

    private fun deleteDataInFirebase(id: String) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child("Meal_Morning")

        // Thực hiện một truy vấn để tìm nút con có id tương ứng
        val query = myRef.orderByChild("id").equalTo(id.toDouble())

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    // Xóa nút cha của nút con được tìm thấy
                    dataSnapshot.ref.removeValue()
                        ?.addOnSuccessListener {
                            // Xóa item trên giao diện
                            mListStorage?.removeAll { it.id.toString() == id }
                            mMealStorageAdapter?.notifyDataSetChanged()

                            Toast.makeText(
                                this@DetailMealActivity,
                                "Xóa thành công",
                                Toast.LENGTH_SHORT
                            ).show()
                        }?.addOnFailureListener {
                            Toast.makeText(
                                this@DetailMealActivity,
                                "Xóa thất bại",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu
                Toast.makeText(
                    this@DetailMealActivity,
                    "Không tìm thấy mục để xóa",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}