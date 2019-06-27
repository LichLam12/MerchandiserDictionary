package com.example.onlyo.merchandiserdictionary.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.onlyo.merchandiserdictionary.R
import com.example.onlyo.merchandiserdictionary.database.DictionaryEntity
import com.example.onlyo.merchandiserdictionary.database.VietAnhEntity
import kotlinx.android.synthetic.main.fragmentchild_favorite.view.*
import kotlinx.android.synthetic.main.fragmentchild_vietanh.view.*

class VietAnhListAdapter (private var wordList: ArrayList<VietAnhEntity>,
                          private val onItemClickListener : (String)-> Unit )
    : RecyclerView.Adapter<VietAnhListAdapter.ViewHolder>()
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

        fun bind(word : VietAnhEntity) {
            tv_vietword.text = word.vietword
            tv_engword.text = word.engword

            itemView.imgv_volume.setOnClickListener {
                onItemClickListener(tv_engword.text as String) }
        }
    }

    // Filter Class
    fun filter(charText: ArrayList<VietAnhEntity>) {
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