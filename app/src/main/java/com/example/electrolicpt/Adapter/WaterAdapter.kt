package com.example.electrolicpt.adapter



import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.electrolicpt.R
import com.example.electrolicpt.WaterHistoryActivity
import com.example.electrolicpt.ObjectInfor.WaterInfo

class WaterAdapter(private val dataList: MutableList<WaterInfo>, private val context: Context)  :
    RecyclerView.Adapter<WaterAdapter.ViewHolder>() {



    fun removeItem(position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val waterAmountTextView: TextView = itemView.findViewById(R.id.waterAmountTextView)
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)


        init {
            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    removeItem(position)
                    // Cập nhật ProgressBar sau khi xóa card
                    (context as WaterHistoryActivity).updateProgressBar()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.time_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]

        // Set dữ liệu cho các thành phần trong layout time_card
        holder.waterAmountTextView.text = item.water
        holder.timeTextView.text = item.time

        // Xử lý sự kiện click trên Button
        holder.deleteButton.setOnClickListener {
            // Xóa mục khi người dùng click vào nút delete
            dataList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}