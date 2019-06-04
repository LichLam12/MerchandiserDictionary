package com.example.onlyo.merchandiserdictionary.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.onlyo.merchandiserdictionary.R
import com.example.onlyo.merchandiserdictionary.model.DictionaryItemDbO
import kotlinx.android.synthetic.main.fragmentchild_favorite.view.*

class WordListAdapter (private var wordList: List<DictionaryItemDbO>,
                       private val onItemClickListener : (Context, TextView, Int)-> Unit )
    : RecyclerView.Adapter<WordListAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragmentchild_favorite, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return wordList.size
    }

    //assigning date from listTransportation to ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(wordList[position])
    }



    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //private val imgView_transportation = itemView.findViewById<View>(R.id.imgView_transportation) as AppCompatImageView
        val tv_favword = itemView.findViewById<View>(R.id.tv_favword) as TextView
        val imgv_favword = itemView.findViewById<View>(R.id.imgv_favword) as ImageView

        fun bind(fav : DictionaryItemDbO) {
            tv_favword.text = fav.word
            imgv_favword.setBackgroundResource(R.drawable.ic_history_24dp)
            //Lập trình bất đồng bộ
            //Set cho imgView_transportation trên recycleviewer 1 lắng nghe (nhận id bất kỳ để nhận dạng loại ptien)
            itemView.tv_favword.setOnClickListener {
                onItemClickListener(itemView.context,itemView.tv_favword,adapterPosition) }

        }
    }


}