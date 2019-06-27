package com.example.onlyo.merchandiserdictionary.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.content.Context
import android.widget.TextView
import com.example.onlyo.merchandiserdictionary.R
import com.example.onlyo.merchandiserdictionary.database.YourWordEntity
import kotlinx.android.synthetic.main.fragmentchild_vietanh.view.*

class YourWordAdapter (private var wordList: ArrayList<YourWordEntity>,
                       private val onItemClickListener : (String)-> Unit,
                       private val onItemClickListener2 : (Context,TextView,String,Int)-> Unit)
    : RecyclerView.Adapter<YourWordAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragmentchild_vietanh, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        //Log.e("ket qua2 : ", wordList.size.toString())
        return wordList.size
    }

    //assigning date from listTransportation to ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(wordList[position])
    }



    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //private val imgView_transportation = itemView.findViewById<View>(R.id.imgView_transportation) as AppCompatImageView
        val tv_vietword = itemView.findViewById<View>(R.id.tv_vietword) as TextView
        val tv_engword = itemView.findViewById<View>(R.id.tv_engword) as TextView
        val imgv_volume = itemView.findViewById<View>(R.id.imgv_volume) as ImageView
        //val linear_yourword = itemView.findViewById<View>(R.id.linear_yourword) as LinearLayout
        val imgv_favword = itemView.findViewById<View>(R.id.imgv_favword) as ImageView

        fun bind(word : YourWordEntity) {
            tv_vietword.text = word.vietword
            tv_engword.text = word.engword
            imgv_favword.setBackgroundResource(R.drawable.ic_yourword_24dp)

            imgv_volume.setOnClickListener {
                onItemClickListener(tv_vietword.text as String) }

            itemView.tv_vietword.setOnClickListener{
                onItemClickListener2(itemView.context,tv_vietword,tv_engword.text as String,adapterPosition)
            }
        }
    }

    // Filter Class
    fun filter(charText: ArrayList<YourWordEntity>) {
        /*ar charText = charText
        charText = charText.toLowerCase(Locale.getDefault())
        searchList.clear()
        if (charText.length == 0) {
            searchList.addAll(searchList_temp)
        } else {
            for (wp in searchList_temp) {
                if (wp.word.toLowerCase(Locale.getDefault()).contains(charText)) {
                    searchList.add(wp)
                }
            }
        }*/
        wordList = charText
        notifyDataSetChanged()
    }
}