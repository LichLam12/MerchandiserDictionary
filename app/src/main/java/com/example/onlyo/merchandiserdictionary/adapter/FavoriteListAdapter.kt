package com.example.onlyo.merchandiserdictionary.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.onlyo.merchandiserdictionary.R
import com.example.onlyo.merchandiserdictionary.model.FavoriteDbO
import kotlinx.android.synthetic.main.fragmentchild_favorite.view.*

/**
 * Created by onlyo on 4/12/2019.
 */
class FavoriteListAdapter (private var favoriteList: List<FavoriteDbO>,
                           private val onItemClick: (String)-> Unit )
    : RecyclerView.Adapter<FavoriteListAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.fragmentchild_favorite, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return favoriteList.size
        }

        //assigning date from listTransportation to ViewHolder
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(favoriteList[position])
        }



        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            //private val imgView_transportation = itemView.findViewById<View>(R.id.imgView_transportation) as AppCompatImageView
            val tv_favword = itemView.findViewById<View>(R.id.tv_favword) as TextView
            val imgv_favword = itemView.findViewById<View>(R.id.imgv_favword) as ImageView

            fun bind(fav : FavoriteDbO) {
                tv_favword.text = fav.word
                imgv_favword.setBackgroundResource(R.drawable.ic_history_24dp)
                //Lập trình bất đồng bộ
                //Set cho imgView_transportation trên recycleviewer 1 lắng nghe (nhận id bất kỳ để nhận dạng loại ptien)
                itemView.tv_favword.setOnClickListener { onItemClick(itemId.toString()) }

            }
        }


}