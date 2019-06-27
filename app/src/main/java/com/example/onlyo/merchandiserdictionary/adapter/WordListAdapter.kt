package com.example.onlyo.merchandiserdictionary.adapter

import android.content.Context
import android.os.AsyncTask
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.onlyo.merchandiserdictionary.R
import com.example.onlyo.merchandiserdictionary.database.DictionaryEntity
import com.example.onlyo.merchandiserdictionary.database.MerchandiseDicDB
import com.example.onlyo.merchandiserdictionary.fragment.DictionaryFragment
import com.example.onlyo.merchandiserdictionary.model.DictionaryItemDbO
import kotlinx.android.synthetic.main.fragmentchild_favorite.view.*

class WordListAdapter (private var wordList: ArrayList<DictionaryEntity>,
                       private val onItemClickListener : (Context, TextView, String,String, String, String,String, String, String, String, Int)-> Unit )
    : RecyclerView.Adapter<WordListAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragmentchild_favorite, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        Log.e("ket qua2 : ", wordList.size.toString())
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

        fun bind(fav : DictionaryEntity) {
            tv_favword.text = fav.word
            imgv_favword.setBackgroundResource(R.drawable.ic_collections_24dp)
            //Lập trình bất đồng bộ
            //Set cho imgView_transportation trên recycleviewer 1 lắng nghe (nhận id bất kỳ để nhận dạng loại ptien)
            itemView.tv_favword.setOnClickListener {
                onItemClickListener(itemView.context,itemView.tv_favword,fav.spelling,fav.wordkind,fav.meaning,fav.vietmean,fav.engmean
                        ,fav.imagelink,fav.favorite,fav.id,adapterPosition) }

        }
    }


}