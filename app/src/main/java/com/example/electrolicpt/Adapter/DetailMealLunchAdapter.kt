package com.example.electrolicpt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.electrolicpt.R
import com.example.electrolicpt.ObjectInfor.FoodInfo

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.NumberFormat


class DetailMealLunchAdapter(private val mListFoodLunch: List<FoodInfo>?) :
    RecyclerView.Adapter<DetailMealLunchAdapter.DetailFoodLunchHolder>() {

    private val quantityMap = mutableMapOf<Int, Int>()
    private val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailFoodLunchHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return DetailFoodLunchHolder(view)
    }

    override fun onBindViewHolder(holder: DetailFoodLunchHolder, position: Int) {
        val food = mListFoodLunch!![position] ?: return
        holder.tvNameFood.text = food.name_food
        holder.tvNumKcal.text = food.kcal_food.toString()



        // Kiểm tra xem đường dẫn ảnh có giá trị hay không trước khi sử dụng Glide
        if (food.img_food != null && !food.img_food!!.isEmpty()) {
            Glide.with(holder.itemView.context)
                .load(food.img_food)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgFood)
        } else {
            // Nếu không có đường dẫn ảnh, có thể đặt ảnh mặc định hoặc xử lý theo ý muốn của bạn
        }


        holder.tvPlus.setOnClickListener {
            updateQuantityAndKcal(holder, position, 1)
        }

        holder.tvMinus.setOnClickListener {
            updateQuantityAndKcal(holder, position, -1)
        }


        // Hiển thị số lượng thực phẩm trong EditText
        val quantity = quantityMap[position] ?: 0
        holder.amount.setText(quantity.toString())

        holder.addFood.setOnClickListener {
            // Lấy dữ liệu liên quan
            val id = food.id
            val name = (food.name_food).toString()
            val newQuantity = quantityMap[position] ?: 0
            val totalKcal = newQuantity * (food.kcal_food?.toDouble() ?: 0.0)

            // Thêm dữ liệu vào Firebase
            addDataToFirebase(food, id, name, newQuantity, totalKcal)
            // Bạn cũng có thể cung cấp phản hồi cho người dùng nếu cần
            Toast.makeText(holder.itemView.context, "Đã thêm thực phẩm vào Firebase", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addDataToFirebase(food: FoodInfo, id: Int, name: String, quantity: Int, totalKcal: Double) {
        // Bạn cần định nghĩa cấu trúc cơ sở dữ liệu Firebase và đường dẫn tương ứng
        val mealRef = databaseRef.child("Meal_Lunch") // Đường dẫn ví dụ
        val imgFood = food.img_food
        // Tạo một nút con mới với một khóa duy nhất
        mealRef.orderByChild("name_food").equalTo(name).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Mục đã tồn tại, cập nhật số lượng
                    for (snapshot in dataSnapshot.children) {
                        val existingQuantity = snapshot.child("quantity").getValue(Int::class.java) ?: 0
                        val newTotalQuantity = existingQuantity + quantity

                        // Cập nhật số lượng và tổng Kcal
                        snapshot.child("quantity").ref.setValue(newTotalQuantity)
                        snapshot.child("totalKcal").ref.setValue(newTotalQuantity * (food.kcal_food?.toDouble() ?: 0.0))
                    }
                } else {
                    // Mục chưa tồn tại, thêm mới
                    val newMealRef = mealRef.push()

                    // Đặt giá trị cho nút mới
                    newMealRef.child("id").setValue(id)
                    newMealRef.child("name_food").setValue(name)
                    newMealRef.child("quantity").setValue(quantity)
                    newMealRef.child("totalKcal").setValue(totalKcal)

                    if (imgFood != null && imgFood.isNotEmpty()) {
                        newMealRef.child("img_food").setValue(imgFood)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Xử lý lỗi nếu cần
            }
        })
    }

    override fun getItemCount(): Int {
        return mListFoodLunch?.size ?: 0
    }

    inner class DetailFoodLunchHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNameFood: TextView
        val tvNumKcal: TextView
        val amount: EditText
        val imgFood: ImageView
        val tvPlus: TextView
        val tvMinus: TextView
        val addFood: ImageView

        init {
            tvNameFood = itemView.findViewById(R.id.nameFood)
            tvNumKcal = itemView.findViewById(R.id.numKcal)
            imgFood = itemView.findViewById(R.id.imgfood)
            amount = itemView.findViewById(R.id.amount)
            tvPlus  = itemView.findViewById(R.id.tvplus)
            tvMinus = itemView.findViewById(R.id.tvminus)
            addFood = itemView.findViewById(R.id.addFood)
        }
    }

    private fun updateQuantityAndKcal(holder: DetailFoodLunchHolder, position: Int, change: Int) {
        val currentQuantity = quantityMap[position] ?: 0
        var newQuantity = currentQuantity + change

        // Đảm bảo số lượng không âm
        if (newQuantity < 1) {
            newQuantity = 1
        }

        // Cập nhật số lượng trong Map
        quantityMap[position] = newQuantity

        // Cập nhật EditText và tính toán lại tổng kcal mới
        holder.amount.setText(newQuantity.toString())

        // Tính toán tổng kcal mới và cập nhật TextView
        val food = mListFoodLunch!![position]
        val totalKcal = newQuantity * (food.kcal_food?.toDouble() ?: 0.0)
        val formattedKcal = formatKcal(totalKcal)
        holder.tvNumKcal.text = formattedKcal
    }


    private fun formatKcal(value: Double): String {
        val numberFormat = NumberFormat.getInstance()
        return if (value.isWhole()) {
            numberFormat.format(value.toLong())
        } else {
            numberFormat.format(value)
        }
    }

    private fun Double.isWhole(): Boolean {
        return this % 1.0 == 0.0
    }

}