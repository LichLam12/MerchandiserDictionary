package com.example.onlyo.merchandiserdictionary.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.onlyo.merchandiserdictionary.R
import com.example.onlyo.merchandiserdictionary.database.DictionaryEntity
import kotlinx.android.synthetic.main.fragmentchild_favorite.view.*
import android.text.method.TextKeyListener.clear
import java.util.*
import kotlin.collections.ArrayList


class SearchListAdapter (private var searchList: ArrayList<DictionaryEntity>,
                         private val onItemClickListener : (Context, TextView, String, String, String, String, String, String, String, String, Int)-> Unit )
    : RecyclerView.Adapter<SearchListAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragmentchild_favorite, parent, false)
        searchList_temp = searchList
        //searchList.addAll(searchList_temp)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    //assigning date from listTransportation to ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(searchList[position])
    }



    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //private val imgView_transportation = itemView.findViewById<View>(R.id.imgView_transportation) as AppCompatImageView
        val tv_favword = itemView.findViewById<View>(R.id.tv_favword) as TextView
        val imgv_favword = itemView.findViewById<View>(R.id.imgv_favword) as ImageView

        fun bind(fav : DictionaryEntity) {
            tv_favword.text = fav.word
            imgv_favword.setBackgroundResource(R.drawable.ic_search_24dp)
            //Lập trình bất đồng bộ
            //Set cho imgView_transportation trên recycleviewer 1 lắng nghe (nhận id bất kỳ để nhận dạng loại ptien)
            tv_favword.setOnClickListener {
                onItemClickListener(itemView.context,tv_favword,fav.spelling,fav.wordkind,fav.meaning,fav.vietmean,fav.engmean
                        ,fav.imagelink,fav.favorite,fav.id.toString(),adapterPosition) }

        }
    }

    private var searchList_temp: ArrayList<DictionaryEntity> = searchList

    // Filter Class
    fun filter(charText: ArrayList<DictionaryEntity>) {
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
        searchList = charText
        notifyDataSetChanged()
    }


}