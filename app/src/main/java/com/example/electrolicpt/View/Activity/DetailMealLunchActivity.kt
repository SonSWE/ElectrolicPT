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
import com.example.electrolicpt.adapter.DetailMealLunchAdapter
import com.example.electrolicpt.adapter.MeaStorageLunchAdapter
import com.example.electrolicpt.ObjectInfor.FoodInfo
import com.example.electrolicpt.ObjectInfor.MealLunchInfo
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailMealLunchActivity : AppCompatActivity() {
    private var recyclerViewLunch: RecyclerView? = null
    private var recyclerViewFoodLunchAdd: RecyclerView? = null
    private var mDetailMealLunchAdapter: DetailMealLunchAdapter? = null
    private var mMealStorageLunchAdapter: MeaStorageLunchAdapter? = null
    private var mListFoodLunch: MutableList<FoodInfo>? = null
    private var mListStorageLunch: MutableList<MealLunchInfo>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_meal_lunch)

        val bottomSheet = findViewById<FrameLayout>(R.id.sheetLunch)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        // Thiết lập chiều cao tối thiểu (peek height) và trạng thái ban đầu
        bottomSheetBehavior.peekHeight = 80
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        // Khởi tạo UI và danh sách thực phẩm
        initUi()
        initUi_Storage()

        // Bắt sự kiện khi người dùng nhấn nút ImageView
        val myImageViewLunch = findViewById<ImageView>(R.id.myImageViewLunch)
        myImageViewLunch.setOnClickListener { v: View? ->
            val intent = Intent(this@DetailMealLunchActivity, MealMenuActivity::class.java)
            startActivity(intent)
        }
        // Lấy danh sách thực phẩm từ cơ sở dữ liệu Firebase
        listFoodLunchDatabase
        listFoodStorageLunchDatabase
    }

    private fun initUi() {
        // Khởi tạo RecyclerView và Adapter cho DetailMeal
        val recyclerViewLunch: RecyclerView = findViewById(R.id.recyclerViewFoodLunch)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerViewLunch.layoutManager = linearLayoutManager
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerViewLunch.addItemDecoration(dividerItemDecoration)


        // Khởi tạo danh sách thực phẩm
        mListFoodLunch = ArrayList()
        mDetailMealLunchAdapter = DetailMealLunchAdapter(mListFoodLunch)
        recyclerViewLunch.setAdapter(mDetailMealLunchAdapter)


    }

    private fun initUi_Storage() {
        // Khởi tạo RecyclerView và Adapter cho DetailMeal
        val recyclerViewFoodLunchAdd: RecyclerView = findViewById(R.id.recyclerViewFoodAddLunch)
        val linearLayoutManagerFoodAdd = LinearLayoutManager(this)
        recyclerViewFoodLunchAdd.layoutManager = linearLayoutManagerFoodAdd
        val dividerItemDecorationFoodAdd =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerViewFoodLunchAdd.addItemDecoration(dividerItemDecorationFoodAdd)

        // Khởi tạo danh sách thực phẩm add
        mListStorageLunch = ArrayList()
        mMealStorageLunchAdapter = MeaStorageLunchAdapter(mListStorageLunch, onDeleteClickListener)
        recyclerViewFoodLunchAdd.setAdapter(mMealStorageLunchAdapter)
    }

    private val listFoodLunchDatabase: Unit
        private get() {
            // Kết nối đến cơ sở dữ liệu Firebase
            val database = FirebaseDatabase.getInstance()
            val myRef = database.reference.child("food")

            // Lắng nghe sự thay đổi trong dữ liệu
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Xóa danh sách cũ trước khi cập nhật
                    mListFoodLunch!!.clear()

                    // Duyệt qua dữ liệu mới và thêm vào danh sách
                    for (dataSnapshot in snapshot.children) {
                        val food = dataSnapshot.getValue(FoodInfo::class.java)
                        if (food != null) {
                            food.img_food = dataSnapshot.child("img_food").getValue(
                                String::class.java
                            )
                            mListFoodLunch!!.add(food)
                        }
                    }


                    // Thông báo cho Adapter biết là dữ liệu đã thay đổi
                    mDetailMealLunchAdapter!!.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Xử lý khi có lỗi xảy ra
                    Toast.makeText(
                        this@DetailMealLunchActivity,
                        "Lấy danh sách thực phẩm thất bại!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

    private val listFoodStorageLunchDatabase: Unit
        private get() {
            // Kết nối đến cơ sở dữ liệu Firebase
            val database = FirebaseDatabase.getInstance()
            val myRef = database.reference.child("Meal_Lunch")

            // Lắng nghe sự thay đổi trong dữ liệu
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Xóa danh sách cũ trước khi cập nhật
                    mListStorageLunch!!.clear()

                    // Duyệt qua dữ liệu mới và thêm vào danh sách
                    for (dataSnapshot in snapshot.children) {
                        val foodStorage = dataSnapshot.getValue(MealLunchInfo::class.java)
                        if (foodStorage != null) {
//                            foodStorage.img_food = dataSnapshot.child("img_food").getValue(String::class.java) ?: ""
                            mListStorageLunch!!.add(foodStorage)
                        }
                    }


                    // Thông báo cho Adapter biết là dữ liệu đã thay đổi
                    mMealStorageLunchAdapter!!.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Xử lý khi có lỗi xảy ra
                    Toast.makeText(
                        this@DetailMealLunchActivity,
                        "Lấy danh sách thực phẩm thất bại!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    private val onDeleteClickListener = object : MeaStorageLunchAdapter.OnDeleteClickListener {
        override fun onDeleteClick(id: String) {
            deleteDataInFirebase(id)
        }
    }

    private fun deleteDataInFirebase(id: String) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child("Meal_Lunch")

        // Thực hiện một truy vấn để tìm nút con có id tương ứng
        val query = myRef.orderByChild("id").equalTo(id.toDouble())

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    // Xóa nút cha của nút con được tìm thấy
                    dataSnapshot.ref.removeValue()
                        ?.addOnSuccessListener {
                            // Xóa item trên giao diện
                            mListStorageLunch?.removeAll { it.id.toString() == id }
                            mMealStorageLunchAdapter?.notifyDataSetChanged()

                            Toast.makeText(
                                this@DetailMealLunchActivity,
                                "Xóa thành công",
                                Toast.LENGTH_SHORT
                            ).show()
                        }?.addOnFailureListener {
                            Toast.makeText(
                                this@DetailMealLunchActivity,
                                "Xóa thất bại",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu
                Toast.makeText(
                    this@DetailMealLunchActivity,
                    "Không tìm thấy mục để xóa",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}